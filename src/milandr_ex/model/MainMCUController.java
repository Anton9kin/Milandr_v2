package milandr_ex.model;

import com.google.common.collect.Maps;
import impl.org.controlsfx.skin.CheckComboBoxSkin;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import milandr_ex.data.*;
import milandr_ex.model.mcu.MCUPinsController;
import milandr_ex.utils.ChangeCallBackImpl;
import milandr_ex.utils.ChangeCallback;
import milandr_ex.utils.ChangeCallbackChecker;
import milandr_ex.utils.GuiUtils;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static milandr_ex.data.Constants.clItem;
import static milandr_ex.data.Constants.clItem2;
import static milandr_ex.data.Constants.clItem3;
import static milandr_ex.utils.GuiUtils.*;

public class MainMCUController extends BasicController
		implements PinoutsModel.Observer, ChangeCallbackChecker {
	private static final Logger	log	= LoggerFactory.getLogger(MainMCUController.class);

	@FXML
	private GridPane clckCont;
	@FXML
	private AnchorPane tmrPane;
	private Map<String, ComboBox> clkMap = Maps.newHashMap();

	@FXML
	private Parent mcuPins;
	@FXML
	private MCUPinsController mcuPinsController ;
	private ChangeCallback changeCallback;

	public MainMCUController() {
	}

	@Override
	public boolean check() {
		return false;
	}

	@Override
	protected void postInit(AppScene scene) {
		changeCallback = new ChangeCallBackImpl(this, clkMap) {
			@Override
			public void callListener(String key, String prev, String value) {
				//changeCombo(key, prev, value);
			}
		};

		mcuPinsController.setScene(scene);
		mcuPinsController.postInit();
		scene.addObserver("pinouts", this);
		fillClockGrid();
		log.debug("#postInit - initialized");
		ObservableList<String> observableList = getScene().getSetsGenerator().genObsList("ADC", true);
		CheckComboBox<String> ccb = new CheckComboBox<>(observableList);
//		((Control) ccb.getSkin().getSkinnable()).setEd
		Platform.runLater(() -> {
			ccb.setSkin(new ComboBox<>().getSkin());
//			Skinnable skinnable = ccb.getSkin().getSkinnable();
//			((ComboBoxBase) skinnable).setEditable(true);
		});
		tmrPane.getChildren().add(new VBox(ccb));
	}

	@Override
	public void observe(PinoutsModel pinoutsModel) {
		if (pinoutsModel == null) return;
		Platform.runLater(() -> {
			Map<String, String> pins = pinoutsModel.getSelectedPins();
			iterateComboMap("k-", pins, clkMap);
		});
	}

	@FXML
	private void changeData(){
//		changeCombo();
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
		String exitStr = combostr.split("\\\\")[1];
		if (!exitStr.equals("Not")) {
			int splitLen = combostr.split("\\|").length;
			ComboBox<String> box = new ComboBox<>(splitLen > 5 ? (splitLen > 7 ? clItem3 : clItem) : clItem2);
			box.getSelectionModel().selectFirst();
//xtodo		makeListener("k-" + caption + "-0", box);
			GuiUtils.makeListener("k-" + caption + "-0", box, changeCallback);
			clkMap.put("k-" + caption + "-0", box);
			addRowToGrid(gridPane, box, 1, 0, 1);
		}
		makeComboGrid(gridPane, caption, combostr, 3);
//		addRowToGrid(gridPane, new Label("output"), 0, 2, 1);
		addRowToGrid(gridPane, makeGridsCombo(exitStr), 1, 2, 1);
		if (!exitStr.equals("Sim") && !exitStr.equals("Not"))
		addRowToGrid(gridPane, new CheckBox("Allow"), 2, 2, 1);
		addRowToGrid(clckCont, new VBox(gridPane), col, row, 1);
	}

	private GridPane makeComboGrid(GridPane gridPane, String prefix, String combostr, int span) {
		GridPane gridPane1 = new GridPane();
		makePaddings(gridPane1);
		String[] combos = combostr.split("\\|");
		int i = 0;
		while (combos.length > i) {
			addComboToGrid(gridPane1, prefix, combos[i], i % 4, i / 4);
			i++;
		}

		addRowToGrid(gridPane, gridPane1, 0, 1, span);
		return gridPane1;
	}

	private Region addComboToGrid(GridPane gridPane1, String prefix, String items, int col, int row) {
		if (items.startsWith("\\")) return null;
		Region cb = col % 2 == 0 ? new Label(items) : makeGridsCombo(items);
		GridPane.setColumnIndex(cb, col);
		GridPane.setRowIndex(cb, row);
		gridPane1.getChildren().add(cb);
		if (cb instanceof ComboBox) {
			String key = "k-" + prefix + "-" + col + row;
//todo			makeListener(key, (ComboBox) cb);
			clkMap.put(key, (ComboBox) cb);
		}
		return cb;
	}
}
