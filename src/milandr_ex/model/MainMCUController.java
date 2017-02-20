package milandr_ex.model;

import com.google.common.collect.Maps;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import milandr_ex.data.*;
import milandr_ex.model.mcu.MCUPinsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static milandr_ex.data.Constants.clItem;
import static milandr_ex.data.Constants.clItem2;
import static milandr_ex.utils.GuiUtils.*;

public class MainMCUController extends BasicController implements PinoutsModel.Observer {
	private static final Logger	log	= LoggerFactory.getLogger(MainMCUController.class);

	@FXML
	private GridPane clckCont;

	private Map<String, ComboBox> clkMap = Maps.newHashMap();

	@FXML
	private Parent mcuPins;
	@FXML
	private MCUPinsController mcuPinsController ;

	public MainMCUController() {
	}
	
	@Override
	protected void postInit(AppScene scene) {
		mcuPinsController.setScene(scene);
		mcuPinsController.postInit();
		scene.addObserver("pinouts", this);
		fillClockGrid();
		log.debug("#postInit - initialized");
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
		ComboBox<String> box = new ComboBox<>(combostr.split("\\|").length > 5 ? clItem : clItem2);
		box.getSelectionModel().selectFirst();
//todo		makeListener("k-" + caption + "-0", box);
		clkMap.put("k-" + caption + "-0", box);
		addRowToGrid(gridPane, box, 1, 0, 1);
		makeComboGrid(gridPane, caption, combostr, 3);
//		addRowToGrid(gridPane, new Label("output"), 0, 2, 1);
		String exitStr = combostr.split("\\\\")[1];
		addRowToGrid(gridPane, makeGridsCombo(exitStr), 1, 2, 1);
		addRowToGrid(gridPane, new CheckBox("Allow"), 2, 2, 1);
		addRowToGrid(clckCont, new VBox(gridPane), col, row, 1);
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
//todo			makeListener(key, (ComboBox) cb);
			clkMap.put(key, (ComboBox) cb);
		}
		return cb;
	}
}
