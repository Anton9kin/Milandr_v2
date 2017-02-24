package milandr_ex.model;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import milandr_ex.data.AppScene;
import milandr_ex.model.mcu.*;
import milandr_ex.utils.GuiUtils;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMCUController extends BasicController {
	private static final Logger	log	= LoggerFactory.getLogger(MainMCUController.class);

	@FXML
	private GridPane clckCont;
	@FXML
	private AnchorPane tmrPane;

	@FXML
	private Parent mcuPins;
	@FXML
	private MCUPinsController mcuPinsController ;
	@FXML
	private Parent mcuPower;
	@FXML
	private MCUPowerController mcuPowerController;
	@FXML
	private Parent mcuSystick;
	@FXML
	private MCUSystickController mcuSystickController ;
	@FXML
	private Parent mcuAdc;
	@FXML
	private MCUAdcController mcuAdcController;
	@FXML
	private Parent mcuCan;
	@FXML
	private MCUCanController mcuCanController;
	@FXML
	private Parent mcuIWdg;
	@FXML
	private MCUIwdgController mcuIwdgController;
	@FXML
	private Parent mcuWWdg;
	@FXML
	private MCUWwdgController mcuWwdgController;
	private MCUClockController mcuClockController;

	@FXML
	private VBox cfg_vbox;
	public MainMCUController() {
		mcuClockController = new MCUClockController();
	}

	@Override
	protected void postInit(AppScene scene) {
		mcuClockController.setClckCont(clckCont);
		initSubControllers(mcuPinsController, mcuPowerController, mcuSystickController,
				mcuClockController,
				mcuAdcController, mcuCanController, mcuIwdgController, mcuWwdgController);

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
		for(Node node: cfg_vbox.getChildren()) {
			if (node instanceof TitledPane) {
				GuiUtils.makeListener(node.getId(), (TitledPane)node, changeCallback);
			}
		}
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		if (comboKey == null) return;
		if (value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(log, String.format("#callGuiListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		}
		String subKey = comboKey.substring(2);
		Boolean inValue = value != null && value.equals("true");
		if (comboKey.startsWith("t-")) {
			if (!inValue) return;
			for (Node node : cfg_vbox.getChildren()) {
				if (node instanceof TitledPane && !node.getId().equals(subKey)) {
					((TitledPane) node).setExpanded(false);
				}
			}
		}
	}
}
