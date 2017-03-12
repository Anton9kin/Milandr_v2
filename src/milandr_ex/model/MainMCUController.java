package milandr_ex.model;

import com.google.common.collect.Maps;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.DeviceFactory;
import milandr_ex.model.mcu.*;
import milandr_ex.model.mcu.ext.*;
import milandr_ex.model.mcu.inn.*;
import milandr_ex.utils.GuiUtils;
import milandr_ex.utils.SyntaxHighlighter;
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
	private Parent mcuTimer;
	@FXML
	private MCUTimerController mcuTimerController;
	@FXML
	private Parent mcuSystick;
	@FXML
	private MCUSystickController mcuSystickController ;
	@FXML
	private Parent mcuAdc;
	@FXML
	private MCUAdcController mcuAdcController;
	@FXML
	private Parent mcuDac;
	@FXML
	private MCUDacController mcuDacController;
	@FXML
	private Parent mcuCan;
	@FXML
	private MCUCanController mcuCanController;
	@FXML
	private Parent mcuI2C;
	@FXML
	private MCUI2CController mcuI2CController;
	@FXML
	private Parent mcuSpi;
	@FXML
	private MCUSpiController mcuSpiController;
	@FXML
	private Parent mcuUart;
	@FXML
	private MCUUartController mcuUartController;
	@FXML
	private Parent mcuGpio;
	@FXML
	private MCUGpioController mcuGpioController;
	@FXML
	private Parent mcuIwdg;
	@FXML
	private MCUIwdgController mcuIwdgController;
	@FXML
	private Parent mcuWwdg;
	@FXML
	private MCUWwdgController mcuWwdgController;
	@FXML
	private Parent mcuCpu;
	@FXML
	private MCUClockController mcuCpuController;
	@FXML
	private Parent mcuComp;
	@FXML
	private MCUCompController mcuCompController;

	@FXML
	private VBox cfg_vbox_in;
	@FXML
	private VBox cfg_vbox_ex;
	@FXML
	private VBox cfg_vbox_ad;
	private Map<String, TitledPane> cfgMap = Maps.newHashMap();

	public MainMCUController() {
//		mcuClockController = new MCUClockController();
	}

	@Override
	protected void postInit(AppScene scene) {
		mcuCpuController.setClckCont(clckCont);
		initSubControllers(mcuPinsController, mcuPowerController, mcuSystickController,
				mcuCpuController, mcuI2CController, mcuSpiController, mcuUartController,
				mcuGpioController, mcuDacController, mcuTimerController, mcuCompController,
				mcuAdcController, mcuCanController, mcuIwdgController, mcuWwdgController);

		makeListeners(cfg_vbox_in.getChildren());
		makeListeners(cfg_vbox_ex.getChildren());
		cfg_vbox_ad.getChildren().add(new VBox(SyntaxHighlighter.get(getScene())));
		log.debug("#postInit - initialized");
	}

	private void makeListeners(List<Node> cfg_vbox) {
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
		super.callGuiListener(comboKey, prev, value);
		if (value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(log, String.format("#callGuiListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		}
		String subKey = comboKey.substring(2);
		Boolean inValue = value != null && value.equals("true");
		if (comboKey.startsWith("t-")) {
			if (!inValue) return;
			collapseChildren(subKey, cfg_vbox_in.getChildren());
			collapseChildren(subKey, cfg_vbox_ex.getChildren());
			updateCodeGenerator(getPairForComboKey(comboKey));
		}
	}

	private void collapseChildren(String subKey, List<Node> children) {
		for (Node node : children) {
			if (node instanceof TitledPane && !node.getId().equals(subKey)) {
				((TitledPane) node).setExpanded(false);
			}
		}
	}

	protected void hideChildPane(Device.EPairNames pair) {
		Boolean pairBlockVisibility = !pair.real() || DeviceFactory.getDevice(getScene()
				.getPinoutsModel().getSelectedBody())
				.getPairCountsArr()[pair.ordinal()] > 0;
		VBox cfg_vbox = pair.ext() ? cfg_vbox_ex : cfg_vbox_in;
		for(Node node: cfg_vbox.getChildren()) {
			if (node.getId().equals("cfg_" + pair.name().toLowerCase())) {
				node.setVisible(pairBlockVisibility);
				break;
			}
		}
	}
}
