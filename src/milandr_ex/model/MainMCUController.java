package milandr_ex.model;

import com.google.common.collect.Maps;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import milandr_ex.data.AppScene;
import milandr_ex.model.mcu.*;
import milandr_ex.utils.GuiUtils;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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
	private VBox cfg_vbox_in;
	@FXML
	private VBox cfg_vbox_ex;
	private Map<String, TitledPane> cfgMap = Maps.newHashMap();

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
		makeLiteners(cfg_vbox_in.getChildren());
		makeLiteners(cfg_vbox_ex.getChildren());
	}

	private void makeLiteners(List<Node> cfg_vbox) {
		for(Node node: cfg_vbox) {
			String key = node.getId();
			if (node instanceof TitledPane) {
				cfgMap.put("t-" + key, (TitledPane) node);
				GuiUtils.makeListener(key, (TitledPane)node, changeCallback);
			}
		}
	}

	@Override
	public Map<String, ? extends Node> nodeMap() {
		return cfgMap;
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
			collapseChildren(subKey, cfg_vbox_in.getChildren());
			collapseChildren(subKey, cfg_vbox_ex.getChildren());
		}
	}

	private void collapseChildren(String subKey, List<Node> children) {
		for (Node node : children) {
			if (node instanceof TitledPane && !node.getId().equals(subKey)) {
				((TitledPane) node).setExpanded(false);
			}
		}
	}
}
