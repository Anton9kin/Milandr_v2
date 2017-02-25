package milandr_ex.model.mcu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import impl.org.controlsfx.skin.CheckComboBoxSkin;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import milandr_ex.data.*;
import milandr_ex.model.BasicController;
import milandr_ex.utils.ChangeCallBackImpl;
import milandr_ex.utils.ChangeCallback;
import milandr_ex.utils.ChangeCallbackChecker;
import milandr_ex.utils.GuiUtils;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static milandr_ex.data.Constants.*;
import static milandr_ex.utils.GuiUtils.*;

/**
 * Created by lizard on 20.02.17 at 16:38.
 */
public class MCUPinsController extends BasicController
		implements PinoutsModel.Observer, ChangeCallbackChecker {
	private static final Logger log	= LoggerFactory.getLogger(MCUPinsController.class);
	@FXML
	private GridPane boxCont;
	@FXML
	private Button clrBtn;
	@FXML
	private VBox lcContIn;
	@FXML
	private VBox lcContEx;

	private Map<String, Label> labMap = Maps.newHashMap();
	private Map<String, ComboBox> comboMap = Maps.newHashMap();
	private Map<String, ObservableList<String>> pxList = Maps.newHashMap();
	private Map<String, VBox> vboxMap = Maps.newHashMap();
	private Map<String, CheckBox> cboxMap = Maps.newHashMap();
	private Map<String, TitledPane> tpaneMap = Maps.newHashMap();
	private Map<String, Button> tbtnMap = Maps.newHashMap();

	public MCUPinsController() {
		log.debug("mcuPins Controller initialized");
	}

	public void mainMCUStart(String pack) {
		getScene().getAppStage().setTitle(getMessages().getString("main.title") + " - " +
				getScene().getMcuMain().getProp("type"));

		showItems(0, 0, 0, 0, 0, 0);

		Device device = DeviceFactory.getDevice(pack);
		hideItems(device.getPortSizesArr());
		hideItems(true, device.getExludedPortsArr());
	}

	@Override
	protected void postInit(AppScene scene) {
		scene.addObserver("pinouts", this);
		String pack = scene.getMcuMain().getProp("pack");
		Device device = DeviceFactory.getDevice(pack);
		setupPinCombos(device);
		mainMCUStart(pack);
		setupPairsCombos(device);
		log.debug("#postInit - initialized");
	}

	@Override
	public void observe(PinoutsModel pinoutsModel) {
		if (pinoutsModel == null) return;
		Platform.runLater(() -> {
			Map<String, String> pins = pinoutsModel.getSelectedPins();
			iterateComboMap("", pins, comboMap);
			iterateComboMap("t-", pins, tpaneMap);
			iterateComboMap("c-", pins, cboxMap);
		});
	}

	private CheckBox getCheckBox(String pName) {
		CheckBox cBox = new CheckBox(pName);
		initItem(cBox, 1);
		GuiUtils.makeListener(pName, cBox, changeCallback);
		cBox.setSelected(false);
		return cBox;
	}

	private void genCustPair(String key, List<Node> result) {
		if (key.startsWith("ADC")) {
			ObservableList<String> observableList = getScene().getSetsGenerator().genObsList(key, true);
			CheckComboBox<String> ccb = new CheckComboBox<>(observableList);
			Platform.runLater(()->{
				if (ccb.getSkin() == null) ccb.setSkin(new ComboBox().getSkin());
				GuiUtils.makeListener(key, ccb, changeCallback);
			});
//			ccb.focusedProperty().addListener((observable, oldValue, newValue) -> System.out.println("CheckComboBox focused: "+newValue));
			result.add(ccb);
		}
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
			GuiUtils.makeListener(sKey, newCombo, changeCallback);
		}
	}

	private void makeListener(final String key, TitledPane tPane) {
		tpaneMap.put(key, tPane);
		GuiUtils.makeListener(key, tPane, changeCallback);
	}

	private void callListener(final String key, String value) {
		if (value.equals("null") || value.equals("RESET")) {
			Platform.runLater(() -> {
				if (!comboMap.containsKey(key)) return;
//				if (!comboMap.get(key).isVisible()) return;
				comboMap.get(key).getSelectionModel().selectFirst();
			});
		} else Platform.runLater(() -> {
			if (!comboMap.containsKey(key)) return;
//			if (!comboMap.get(key).isVisible()) return;
			comboMap.get(key).getSelectionModel().select(value);
		});
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

	private void setupPairsCombos(Device device) {
		Set<String> portKeys = filterPortKeys();
		getScene().setSetsGenerator(new SetsGenerator(portKeys));
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
			for(int j = 1; j <= pairSize; j++) {
				String pName = pairName + (pairSize > 1 ? "-" + j: "");
				CheckBox cBox = getCheckBox(pName);
				Button cBtn = new Button("clear");
				GuiUtils.makeListener(pName, cBtn, changeCallback);
				HBox hBox =new HBox(cBox, cBtn);
				cboxMap.put(pName, cBox);
				VBox vBox = makePairs(pairName + (j), pName, ePair.getSize());
				vvBox.getChildren().add(hBox);
				vvBox.getChildren().add(vBox);
//				GridPane.setColumnIndex(tPane, l);
//				GridPane.setRowIndex(tPane, k++);
			}
//			if (k >= 4) { k = 0; l++; }
			makeListener(pairName, tPane);
			(ePair.ext() ? lcContEx  : lcContIn).getChildren().add(tPane);
		}
	}

	private Set<String> filterPortKeys() {
		Set<String> portKeys = Sets.newHashSet(comboMap.keySet());
		Iterator<String> pkit = portKeys.iterator();
		while (pkit.hasNext()) {
			ComboBox cb = comboMap.get(pkit.next());
			if (!cb.isVisible()) pkit.remove();
		}
		return portKeys;
	}

	private void setupPinCombos(Device device) {
		int maxPort = device.getMaxPortSizes();
		for(int i = 0; i < 6; i++) { for(int j = 0; j < maxPort; j++) {
			final String key = "cb" + (i) + (j < 10 ? "0" : "") + j;
			makePxItem(i, j, key);
			VBox vBox = makePairs(key);
			boxCont.getChildren().add(vBox);
			if ( i > 0) GridPane.setColumnIndex(vBox, i);
			GridPane.setRowIndex(vBox, j + 1);
		}}
	}

	private VBox makePairs(String key) {
		return makePairs(key, 1);
	}
	private VBox makePairs(String key, int pairCnt) {
		return makePairs(key, key, pairCnt);
	}
	private VBox makePairs(String sub, String key, int pairCnt) {
		if (!key.startsWith("cb")) {
			pxList.put(key, getScene().getSetsGenerator().genObsList(sub));
		}
		List<Node> result = Lists.newArrayList();
		Integer pxSize = pxList.get(key).size();
		if (pxSize > 1) genVboxPair(key, pairCnt, result);
		else genCustPair(key, result);
		VBox vBox = new VBox((Node[]) result.toArray(new Node[]{}));
		if (pairCnt > 0) vboxMap.put(key, vBox);
		initItem(vBox, pxSize > 1 ? (pairCnt * 2 + 1) : 0);
		return vBox;
	}

//	private void changeCombo(){
//		for(String comboKey: comboMap.keySet()) {
//			changeCombo(comboKey, "", "");
//		}
//	}

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

	@Override
	public boolean check() {
		return refillInProgress > 0;
	}

	@Override
	public Map<String, ? extends Node> nodeMap() {
		return comboMap;
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		switchComboIndex(comboKey, comboMap.get(comboKey), prev, value);
	}

	private void switchComboIndex(String comboKey, ComboBox comboBox, String prev, String value) {
		if (comboKey == null) return;
		if (value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(log, String.format("#switchComboIndex[%d](%s, %s -> %s)", refillInProgress, comboKey, prev, value));
		}
		String subKey = comboKey.substring(2);
		Boolean inValue = value != null && value.equals("true");
		saveSelectedPin(comboKey, value);
		if (comboKey.startsWith("b-")) {
			uncheckObjects(subKey, comboMap);
			return;
		}
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
			collapseOtherTPanes(subKey, inValue);
			return;
		}
//		if (!comboKey.startsWith("cb")) return;
		if (comboBox == null || comboBox.getSelectionModel() == null) return;
		SelectionModel model = comboBox.getSelectionModel();
		if (model.getSelectedItem() == null || model.getSelectedIndex() < 0) return;
		if (!switchLinkedComboboxes(comboKey, prev, value)){
			comboBox.getSelectionModel().clearSelection();
			return;
		}
		if (!labMap.containsKey(comboKey)) return;
		switchLinkedLabel(comboKey, comboBox, model);
	}

	private void collapseOtherTPanes(String subKey, Boolean inValue) {
		if (inValue) for(String tKey: tpaneMap.keySet()) {
			if (tKey.equals(subKey)) continue;
			tpaneMap.get(tKey).setExpanded(false);
		}
	}

	private void switchLinkedLabel(String comboKey, ComboBox comboBox, SelectionModel model) {
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

	private boolean switchLinkedComboboxes(String srcKey, String prev, String value) {
		boolean result = true;
		if (prev == null) return result;
		if (refillInProgress > 0) return result;
		if (value != null && !value.equals("null")) {
			refillInProgress++;
			refillLinkedPairCombos(srcKey, value);
			changeTargetCBoxAndLabel(srcKey, prev, "RESET");
			result = changeTargetCBoxAndLabel(srcKey, value, value);
			refillInProgress--;
		}
		return result;
	}

	private boolean changeTargetCBoxAndLabel(String srcKey, String crVal, String newVal) {
		if (crVal.equals("null") || crVal.equals("RESET")) return true;
		String[] values = crVal.split("\\s");
		String cKey = textToKey(values[values.length - 1]);
		CBoxAndKey cbkey = refindTargetCBox(srcKey, cKey, crVal, !newVal.equals("RESET"));
		if (cbkey.cBox == null && cKey.contains("-")) {
			String cKey1 = cKey.substring(0, cKey.lastIndexOf("-"));
			cbkey = refindTargetCBox(srcKey, cKey1, crVal, !newVal.equals("RESET"));
			if (cbkey.cBox == null) cbkey.setKey(cKey);
		}
		return switchTargetBoxAndLabel(srcKey, newVal, cbkey);
	}

	private boolean switchTargetBoxAndLabel(String srcKey, String value1, CBoxAndKey cbkey) {
		ComboBox target = cbkey.cBox;
		selectAndExpandPairGroup(cbkey.key);
		if (target != null && target.getSelectionModel() != null) {
			SelectionModel selMod = target.getSelectionModel();
			String cKey = cbkey.key;
			resetPrevLinkedCombobox(target, cKey);
			if (!value1.equals("RESET")) {
				String selItem = value1.split("\\s")[0];
				if (srcKey.startsWith("cb")) {
					selItem += " " + keyToText(srcKey);
					refillLinkedPairCombos(cKey, selItem);
				}
				selMod.select(selItem);
				switchLinkedLabel(cKey, target, selMod);
			}
			return true;
		}
		return false;
	}

	private void selectAndExpandPairGroup(String cKey) {
		if (cKey.contains("-")) {
			String subCKey = cKey.substring(0, cKey.lastIndexOf("-"));
			String subTKey = cKey.substring(0, cKey.indexOf("-"));
			if (!tpaneMap.containsKey(subTKey)) return;
			tpaneMap.get(subTKey).setExpanded(true);
			collapseOtherTPanes(subTKey, true);
			if (subCKey.equals(subTKey)) {
				if (cboxMap.containsKey(cKey)) subCKey = cKey;
				if (!cboxMap.containsKey(subCKey)) subCKey += "-1";
			}
			cboxMap.get(subCKey).setSelected(true);
			switchObjects(subCKey, vboxMap, true, true);
		}
	}

	private static class CBoxAndKey {
		String key;
		ComboBox cBox;

		public CBoxAndKey(String key, ComboBox cBox) {
			this.key = key;
			this.cBox = cBox;
		}

		public CBoxAndKey setKey(String key) {
			this.key = key;
			return this;
		}

		public CBoxAndKey setcBox(ComboBox cBox) {
			this.cBox = cBox;
			return this;
		}
	}

	@SuppressWarnings("unchecked")
	private CBoxAndKey refindTargetCBox(String srcKey, String cKey, String crVal, boolean forNew) {
		ComboBox<String> target = comboMap.get(cKey);
		CBoxAndKey result = new CBoxAndKey(cKey, target);
		String lKey = cKey;
		if (target == null) {
			int i  = 0;
			lKey = cKey;
			while (i <= 9 && target == null) {
				lKey = cKey + "-0" + i++;
				target = comboMap.get(lKey);
				if (forNew && isTargetHasAnySelection(target, crVal + " " + keyToText(srcKey))) target = null;
				else if (!forNew && !isTargetHasSelection(target, crVal)) target = null;
			}
		}
		return result.setKey(lKey).setcBox(target);
	}

	private boolean isTargetHasAnySelection(ComboBox<String> target, String crVal) {
		if (target == null) return false;
		if (target.getSelectionModel() == null) return false;
		SingleSelectionModel model = target.getSelectionModel();
		if (model.getSelectedItem() != null) return true;
		if (target.getItems() == null) return false;
		return (!target.getItems().contains(crVal));
	}

	private boolean isTargetHasSelection(ComboBox<String> target, String crVal) {
		if (target == null) return false;
		String selectedItem = target.getSelectionModel().getSelectedItem();
		if (selectedItem == null) return false;
		return selectedItem.startsWith(crVal);
	}

	private void resetPrevLinkedCombobox(String prev) {
		if (prev.contains(" ")) {
			String cKey = textToKey(prev.split("\\s")[1]);
			//noinspection unchecked
			resetPrevLinkedCombobox(comboMap.get(cKey), cKey);
		}
	}
	private void resetPrevLinkedCombobox(ComboBox pTarget, String cKey) {
		if (pTarget != null) {
			//noinspection unchecked
			SingleSelectionModel<String> selMod = pTarget.getSelectionModel();
			selMod.selectFirst();
			switchLinkedLabel(cKey, pTarget, selMod);
		}
	}

	private int refillInProgress = 0;
	private void refillLinkedPairCombos(String key, String value) {
		if (key.matches("\\w{3}-\\d-\\d{2}")) {
			String link = key.substring(0, 7);
			refillLinkedPairCombos(key, link, value);
		} else if (key.matches("\\w{4}-\\d-\\d{2}")) {
			String link = key.substring(0, 8);
			refillLinkedPairCombos(key, link, value);
		}
	}
	private void refillLinkedPairCombos(String key, String link, String value) {
		List<ComboBox> tempCb = Lists.newArrayList();
		List<String> tempVals = Lists.newArrayList();
		List<String> cbVals = Lists.newArrayList();
		List<String> cbKeys = Lists.newArrayList();
		String skip = value.split("\\s")[0];

		for(int i = 0; i < 10; i++) {
			if ((link + i).equals(key)) continue;
			ComboBox cb = comboMap.get(link + i);
			if (cb == null) continue;
			tempCb.add(cb);
			Object item = cb.getSelectionModel().getSelectedItem();
			tempVals.add(String.valueOf(item).split("\\s")[0]);
			cbVals.add(String.valueOf(item));
			cbKeys.add(link + i);
//				if (item != null) tempVals[i] = item.toString().split("\\s")[0];
		}
		for (String itm: tempVals){
			if (itm == null || itm.isEmpty()) continue;
			if (itm.equals("RESET")) continue;
			skip += "," + itm;
		}
		for(ComboBox cb: tempCb) {
			if (cb == null) return;
			String itm = cbVals.get(tempCb.indexOf(cb));
			String keep = itm == null || itm.equals("null") ? "none" : tempVals.get(tempCb.indexOf(cb));
			String skp = skip.replaceAll("," + keep, "")
					.replaceAll(keep + ",", "")
					.replaceAll(",,", ",");
			int linkLen2 = link.length() / 2;
			cb.setItems(getScene().getSetsGenerator().genObsList(key.substring(0, linkLen2)
					+ key.substring(linkLen2 + 1, linkLen2 + 2), skp));
			if (itm == null || itm.isEmpty() || itm.equals("null")) {
				itm = "RESET";
				cb.getSelectionModel().selectFirst();
			} else  cb.getSelectionModel().select(itm);
//				changeCombo(cbKeys[tempCb.indexOf(cb)], "itm", itm);
			String cbKey = cbKeys.get(tempCb.indexOf(cb));
			if (cbKey == null) continue;
			switchComboIndex(cbKey, cb, "itm", itm);
		}
	}

	private void checkCBoxHiding(String subKey, CheckBox cBox) {
		if (cBox != null) {
			Boolean val = cBox.selectedProperty().getValue();
			switchObjects(subKey, vboxMap, val, true);
		}
	}

	@FXML
	public void clearAll(ActionEvent event) {
		Platform.runLater(() -> {
			for(String cmbKey: comboMap.keySet()) {
				comboMap.get(cmbKey).getSelectionModel().selectFirst();
			}});
	}
}
