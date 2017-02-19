package milandr_ex.model;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import milandr_ex.MilandrEx;
import milandr_ex.data.Constants;
import milandr_ex.data.Device;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.DeviceFactory;
import milandr_ex.data.PinoutsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static milandr_ex.data.Constants.*;

public class MainMCUComtroller implements PinoutsModel.Observer {
	private static final Logger	log	= LoggerFactory.getLogger(MainMCUComtroller.class);

	private Background backgroundDefault = null;
	private Background backgroundIO = new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	private Background backgroundPeriph = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	
	@FXML
	private GridPane boxCont;
	@FXML
	private Button clrBtn;
	@FXML
	private VBox lcContIn;
	@FXML
	private VBox lcContEx;
	@FXML
	private GridPane clckCont;

	private ResourceBundle messages;
	private Map<String, Label> labMap = Maps.newHashMap();
	private Map<String, ComboBox> comboMap = Maps.newHashMap();
	private Map<String, ObservableList<String>> pxList = Maps.newHashMap();
	private Map<String, VBox> vboxMap = Maps.newHashMap();
	private Map<String, CheckBox> cboxMap = Maps.newHashMap();
	private PinoutsModel pinoutsModel;
	private Map<String, ComboBox> clkMap = Maps.newHashMap();
	@FXML
	private TabPane mainTabPane;

	public MainMCUComtroller() {
		MilandrEx.addObserver("pinouts", this);
	}
	
	public void mainMCUStart(String pack) {
		// TODO Auto-generated constructor stub

		MilandrEx.primaryStage.setTitle(messages.getString("main.title") + " - " + MilandrEx.mcuMain.getProp("type"));

		showItems(0, 0, 0, 0, 0, 0);

		Device device = DeviceFactory.getDevice(pack);
		hideItems(device.getPortSizesArr());
		hideItems(true, device.getExludedPortsArr());
	}
	
	@FXML
	private void initialize() {
		messages = Constants.loadBundle("messages", "ru");
		String pack = MilandrEx.mcuMain.getProp("pack");
		setItems(pack);
		mainMCUStart(pack);
	}

	@Override
	public void observe(PinoutsModel model) {
		if (model == null) return;
		pinoutsModel = model;
		Platform.runLater(() -> {
			Map<String, String> pins = pinoutsModel.getSelectedPins();
			iterateComboMap(pins, comboMap);
			iterateComboMap(pins, clkMap);
		});
	}

	private void iterateComboMap(Map<String, String> pins, Map<String, ComboBox> map) {
		for(String key: map.keySet()) {
			String pin = pins.get(key);
			if (pin == null) continue;
			//noinspection unchecked
			map.get(key).getSelectionModel().select(pin);
		}
	}

	@FXML
	private void changeData(){
//		changeCombo();
	}

	private void initItem(Region node, int k) {
		node.setVisible(Boolean.TRUE);
		node.setMinWidth(100.0);
		node.setPrefWidth(100.0);
		node.setMinHeight(25 * k);
		node.setPrefHeight(25 * k);
	}

	private void setItems(String pack){
		Device device = DeviceFactory.getDevice(pack);

		setupPinCombos();
		setupPairsCombos(device);
		fillClockGrid();
	}

	private void setupPairsCombos(Device device) {
		Device.EPairNames[] ePairs = Device.EPairNames.values();
		Integer[] pairs = device.getPairCountsArr();
		for(int i = 0; i < ePairs.length; i++) {
			Device.EPairNames ePair = ePairs[i];
			String pairName = ePair.name();
			int pairSize = pairs[i];
			if (pairSize < 1) continue;
			VBox vvBox = new VBox();
			TitledPane tPane = new TitledPane(pairName, vvBox);
			tPane.setExpanded(false);
			for(int j = 0; j < pairSize; j++) {
				String pName = pairName + (pairSize > 1 ? "-" + j: "");
				CheckBox cBox = getCheckBox(pName);
				cboxMap.put(pName, cBox);
				VBox vBox = makePairs(pairName + (j + 1), pName, ePair.getSize());
				vvBox.getChildren().add(cBox);
				vvBox.getChildren().add(vBox);
//				GridPane.setColumnIndex(tPane, l);
//				GridPane.setRowIndex(tPane, k++);
			}
//			if (k >= 4) { k = 0; l++; }
			makeListener(pairName, tPane);
			(ePair.ext() ? lcContEx  : lcContIn).getChildren().add(tPane);
		}
	}

	private void setupPinCombos() {
		for(int i = 0; i < 6; i++) { for(int j = 0; j < 16; j++) {
			final String key = "cb" + (i) + (j < 10 ? "0" : "") + j;
			makePxItem(i, j, key);
			VBox vBox = makePairs(key);
			boxCont.getChildren().add(vBox);
			if ( i > 0) GridPane.setColumnIndex(vBox, i);
			if ( j > 0) GridPane.setRowIndex(vBox, j);
		}}
		;
	}

	private void fillClockGrid() {
		clckCont.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
		makePaddings(clckCont);
		String[][] combostrs = Constants.combostrs;
		for(int i = 0; i < combostrs.length - 1; i++) {
			for(int j = 0; j < combostrs[i].length; j++) {
				String namestr = combostrs[i][j];
				if (namestr.isEmpty()) continue;
				String combostr = combostrs[combostrs.length - 1][i * combostrs[i].length + j];
				makeClockGridItem(namestr, combostr, j, i);
			}
		}
	}

	private void makeClockGridItem(String caption, String combostr, int col, int row) {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		makePaddings(gridPane);
		addRowToGrid(gridPane, new Label(caption), 0, 0, 1);
		ComboBox<String> box = new ComboBox<>(combostr.split("\\|").length > 5 ? clItem : clItem2);
		box.getSelectionModel().selectFirst();
		makeListener("k-" + caption + "-0", box);
		clkMap.put("k-" + caption + "-0", box);
		addRowToGrid(gridPane, box, 1, 0, 1);
		makeComboGrid(gridPane, caption, combostr, 3);
//		addRowToGrid(gridPane, new Label("output"), 0, 2, 1);
		String exitStr = combostr.split("\\\\")[1];
		addRowToGrid(gridPane, makeGridsCombo(exitStr), 1, 2, 1);
		addRowToGrid(gridPane, new CheckBox("Allow"), 2, 2, 1);
		addRowToGrid(clckCont, new VBox(gridPane), col, row, 1);
	}

	private void makePaddings(GridPane gridPane) {
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		GridPane.setMargin(gridPane, new Insets(10.0, 10.0, 10.0, 10.0));
	}

	private void addRowToGrid(GridPane gridPane, Node label, int row) {
		addRowToGrid(gridPane, label, 0, row, 1);
	}
	private void addColToGrid(GridPane gridPane, Node label, int col) {
		addRowToGrid(gridPane, label, col, 0, 1);
	}
	private void addRowToGrid(GridPane gridPane, Node label, int col, int row, int span) {
		GridPane.setColumnIndex(label, col);
		GridPane.setRowIndex(label, row);
		GridPane.setColumnSpan(label, span);
		gridPane.getChildren().add(label);
	}

	private GridPane makeComboGrid(GridPane gridPane, String prefix, String combostr, int span) {
		GridPane gridPane1 = new GridPane();
		makePaddings(gridPane1);
		String[] combos = combostr.split("\\|");
		addComboToGrid(gridPane1, prefix, combos[0], 0,0);
		addComboToGrid(gridPane1, prefix, combos[1], 1, 0);
		if (combos.length > 3) {
			addComboToGrid(gridPane1, prefix, combos[2], 0, 1);
			addComboToGrid(gridPane1, prefix, combos[3], 1, 1);
		}
		if (combos.length > 5) {
			addComboToGrid(gridPane1, prefix, combos[4], 2, 0);
			addComboToGrid(gridPane1, prefix, combos[5], 3, 0);
		}
		if (combos.length > 7) {
			addComboToGrid(gridPane1, prefix, combos[6], 2, 1);
			addComboToGrid(gridPane1, prefix, combos[7], 3, 1);
		}
		addRowToGrid(gridPane, gridPane1, 0, 1, span);
		return gridPane1;
	}

	private Region addComboToGrid(GridPane gridPane1, String prefix, String items, int col, int row) {
		Region cb = col % 2 == 0 ? new Label(items) : makeGridsCombo(items);
		GridPane.setColumnIndex(cb, col);
		GridPane.setRowIndex(cb, row);
		gridPane1.getChildren().add(cb);
		if (cb instanceof ComboBox) {
			String key = "k-" + prefix + "-" + col + row;
			makeListener(key, (ComboBox) cb);
			clkMap.put(key, (ComboBox) cb);
		}
		return cb;
	}

	private Region makeGridsCombo(String items) {
		Region cb;
		if (items.matches("\\d+\\s[MK]Hz")) {
			int max = Integer.parseInt(items.substring(0, items.indexOf(' ')));
			cb = new Spinner<Integer>(2, max, 2);
		} else cb = !items.matches("[\\/\\*] \\d+") ? new TextField(items) :
				new ComboBox<String>(Constants.getDvMlItems(items));
		if (cb instanceof ComboBox) {
			((ComboBox) cb).getSelectionModel().selectLast();
		} else if (cb instanceof TextField) {
			((TextField) cb).setEditable(false);
			cb.setDisable(true);
		}
		cb.setPrefWidth(80.0);
		return cb;
	}

	private CheckBox getCheckBox(String pName) {
		CheckBox cBox = new CheckBox(pName);
		initItem(cBox, 1);
		makeListener(pName, cBox);
		cBox.setSelected(false);
		return cBox;
	}

	private VBox makePairs(String key) {
		return makePairs(key, 1);
	}
	private VBox makePairs(String key, int pairCnt) {
		return makePairs(key, key, pairCnt);
	}
	private VBox makePairs(String sub, String key, int pairCnt) {
		if (!key.startsWith("cb")) {
			pxList.put(key, FXCollections.observableArrayList(Constants.genLists(sub)));
		}
		List<Node> result = Lists.newArrayList();
		Integer pxSize = pxList.get(key).size();
		if (pxSize > 1) genVboxPair(key, pairCnt, result);
		VBox vBox = new VBox((Node[]) result.toArray(new Node[]{}));
		if (pairCnt > 0) vboxMap.put(key, vBox);
		initItem(vBox, pxSize > 1 ? (pairCnt * 2 + 1) : 0);
		return vBox;
	}

	private void genVboxPair(String key, int pairCnt, List<Node> result) {
		for(int i = 0; i < pairCnt; i++) {
			String sKey = pairCnt > 1 ? key + "-" + (i > 9 ? "" : "0") + i : key;
			ComboBox<String> newCombo = makeCombo(sKey);
			Label newLabel = makeLabel(sKey);
			result.add(newLabel);
			result.add(newCombo);
			labMap.put(sKey, newLabel);
			comboMap.put(sKey, newCombo);
			newCombo.setItems(pxList.get(key));
			makeListener(sKey, newCombo);
		}
	}

	private Label makeLabel(String key) {
		return new Label(keyToText(key));
	}

	private ComboBox<String> makeCombo(String pText) {
		ComboBox<String> newCombo = new ComboBox<>();
		newCombo.setBackground(backgroundDefault);
		newCombo.setPromptText(keyToText(pText));
		initItem(newCombo, 1);
		return newCombo;
	}

	private void makeListener(final String key, TitledPane tPane) {
		makeListener("t-" + key, tPane.expandedProperty());
	}
	private void makeListener(final String key, CheckBox newBox) {
		makeListener("c-" + key, newBox.selectedProperty());
	}
	private void makeListener(final String key, ComboBox newCombo) {
		makeListener(key, newCombo.valueProperty());
	}
	private void makeListener(final String key, ReadOnlyProperty property) {
		property.addListener((ov, t, t1) ->  {
//			log_debug(String.format("#listen ov=%s t=%s t1=%s", ov, t, t1));
			Platform.runLater(() -> changeCombo(key, String.valueOf(t), String.valueOf(t1)));
		});
		if (comboMap.containsKey(key)) {
			Platform.runLater(() -> comboMap.get(key).getSelectionModel().selectFirst());
		}
	}

	private void makePxItem(int i, int j, String key) {
		//"RESET","-","IO out","IO in","DATA1","TMR1_CH1","TMR2_CH1"
		String[] texts  = Constants.comboTexts[i * 16 + j];
		ObservableList<String> pxItem = FXCollections.observableArrayList("RESET",
				texts[0], "IO out", "IO in", texts[1], texts[2], texts[3]);
//		ObservableList<String> pxItem = FXCollections.observableArrayList("RESET","-","IO out","IO in",
//				"DATA" + i, "CAN"+j+"_TX","UART"+j+"_RXD");
		pxList.put(key, pxItem);
	}

	private void changeCombo(){
		for(String comboKey: comboMap.keySet()) {
			changeCombo(comboKey, "", "");
		}
	}
	private void changeCombo(String comboKey, String prev, String value){
		switchComboIndex(comboKey, comboMap.get(comboKey), prev, value);
	}
	private void changeSets(String[] comboKey, Boolean value){
		for(String key: comboKey) {
			switchObjects(key, vboxMap, value, true);
		}
	}
	private void changeSet(String comboKey, Boolean value){
		switchObjects(comboKey, vboxMap, value, true);
	}

	private void hideItems(Integer... idx){
		if (idx == null) return;
		switchLabels(idx, false);
		switchCombos(idx, false);
	}

	private void showItems(Integer... idx){
		if (idx == null) return;
		switchLabels(idx, true);
		switchCombos(idx, true);
	}

	private void hideItems(boolean single, Integer... idx){
		if (idx == null) return;
		switchLabels(idx, false, single);
		switchCombos(idx, false, single);
	}

	private void showItems(boolean single, Integer... idx){
		if (idx == null) return;
		switchLabels(idx, true, single);
		switchCombos(idx, true, single);
	}

	private void hideCombos(Integer... idx){
		switchCombos(idx, false);
	}

	private void showCombos(Integer... idx){
		switchCombos(idx, true);
	}

	private void hideLabels(Integer... idx){
		switchLabels(idx, false);
	}

	private void showLabels(Integer... idx){
		switchLabels(idx, true);
	}

	private void switchLabels(Integer[] idx, boolean visible){
		switchObjects(idx, labMap, visible, false);
	}

	private void switchCombos(Integer[] idx, boolean visible){
		switchObjects(idx, comboMap, visible, false);
	}

	private void switchLabels(Integer[] idx, boolean visible, boolean single){
		switchObjects(idx, labMap, visible, single);
	}

	private void switchCombos(Integer[] idx, boolean visible, boolean single){
		switchObjects(idx, comboMap, visible, single);
	}

	private void switchObjects(Integer[] idx, Map<String, ? extends Node> nodeMap, boolean visible){
		switchObjects(idx, nodeMap, visible, false);
	}
	private void switchObjects(Integer[] idx, Map<String, ? extends Node> nodeMap, boolean visible, boolean single){
		for(String comboKey: nodeMap.keySet()) {
			if (!comboKey.matches("cb\\d+")) continue;
			Node node = nodeMap.get(comboKey);
			boolean outOfRange = single;
			for(int i = 0; i < idx.length; i++) {
				String comboNum = comboKey.substring(2);
				if (!comboNum.startsWith("" + (idx[i] / 100))) continue;
				int parse = Integer.parseInt(comboNum.substring(1));
				outOfRange = single ? parse != (idx[i] % 100) : parse < (idx[i] % 100);
				if (single && outOfRange) continue;
				if (single || outOfRange) break;
			}
			if (outOfRange) continue;
			node.setVisible(visible);
		}
	}

	private void switchObjects(String pref, Map<String, ? extends Node> nodeMap, boolean visible, boolean single){
		for(String comboKey: nodeMap.keySet()) {
			if (!comboKey.startsWith(pref)) continue;
			Node node = nodeMap.get(comboKey);
			node.setVisible(visible);
			((VBox)node).setMinHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setPrefHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setMaxHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
		}
	}

	private void switchComboIndex(String comboKey, ComboBox<String> comboBox, String prev, String value) {
		if(value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(String.format("#switchComboIndex(%s, %s -> %s)", comboKey, prev, value));
		}
		String subKey = comboKey.substring(2);
		Boolean inValue = value != null && value.equals("true");
		if (comboKey.startsWith("c-")) {
			switchObjects(subKey, vboxMap, inValue, true);
			return;
		}
		if (comboKey.startsWith("t-") && inValue) {
			CheckBox cBox = cboxMap.get(subKey);
			checkCBoxHiding(subKey, cBox);
			for(int i = 0; i < 10; i++) {
				String ssKey = subKey + "-" + i;
				checkCBoxHiding(ssKey, cboxMap.get(ssKey));
			}
			return;
		}
//		if (!comboKey.startsWith("cb")) return;
		if (comboBox == null || comboBox.getSelectionModel() == null) return;
		SelectionModel model = comboBox.getSelectionModel();
		if (model.getSelectedItem() == null || model.getSelectedIndex() < 0) return;
		if (pinoutsModel != null) pinoutsModel.setSelectedPin(comboKey, value);
		switchLinkedComboboxes(prev, value);
		Background newBack = backgroundDefault;
		Label label = labMap.get(comboKey);
		label.setText(keyToText(comboKey));
		label.setVisible(true);
		switch(model.getSelectedIndex()) {
			case 0:
				model.clearSelection(0);
				label.setVisible(false);
			case 1: break;
		case 2: case 3: newBack = backgroundIO; break;
		case 4: case 5: case 6: newBack = backgroundPeriph; break;
		}
		if (comboKey.startsWith("cb")) {
			comboBox.setBackground(newBack);
			label.setBackground(newBack);
		}
	}

	private void switchLinkedComboboxes(String prev, String value) {
		if (value != null) {
			if (value.equals("RESET")) {
				resetPrevLinkedCombobox(prev);
				return;
			}
			String[] values = value.split("\\s");
			ComboBox<String> target = comboMap.get(textToKey(values[values.length - 1]));
			if (target != null && target.getSelectionModel() != null) {
				SelectionModel selMod = target.getSelectionModel();
				resetPrevLinkedCombobox(prev);
				selMod.select(values[0]);
			}
		}
	}

	private void resetPrevLinkedCombobox(String prev) {
		if (prev.contains(" ")) {
			ComboBox<String> pTarget = comboMap.get(textToKey(prev.split("\\s")[1]));
			if (pTarget != null) pTarget.getSelectionModel().selectFirst();
		}
	}

	private void checkCBoxHiding(String subKey, CheckBox cBox) {
		if (cBox != null) {
			Boolean val = cBox.selectedProperty().getValue();
			switchObjects(subKey, vboxMap, val, true);
		}
	}

	private void log_debug(String logText) {
		log.debug(logText);
		System.out.println(logText);
	}

	@FXML
	public void clearAll(ActionEvent event) {
		Platform.runLater(() -> {
		for(String cmbKey: comboMap.keySet()) {
			comboMap.get(cmbKey).getSelectionModel().selectFirst();
		}});
	}
}
