package milandr_ex.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.data.Device;
import milandr_ex.data.DeviceFactory;
import milandr_ex.model.mcu.*;
import milandr_ex.model.mcu.ext.*;
import milandr_ex.model.mcu.inn.*;
import milandr_ex.utils.GuiUtils;
import milandr_ex.utils.SyntaxHighlighter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainMCUController extends BasicController {
	private static final Logger	log	= LoggerFactory.getLogger(MainMCUController.class);

	@FXML
	private GridPane clckCont;
	@FXML
	private AnchorPane tmrPane;
	@FXML
	private TabPane mcuTabPane;

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
	private Parent mcuBkp;
	@FXML
	private MCUBkpController mcuBkpController;
	@FXML
	private Parent mcuEbc;
	@FXML
	private MCUEbcController mcuEbcController;
	@FXML
	private Parent mcuDma;
	@FXML
	private MCUDmaController mcuDmaController;
	@FXML
	private Parent mcuMpu;
	@FXML
	private MCUMpuController mcuMpuController;
	@FXML
	private Parent mcuUsb;
	@FXML
	private MCUUsbController mcuUsbController;
	@FXML
	private Tab mcuOtherSTab;
	@FXML
	private Parent mcuOtherS;
	@FXML
	private MCUOtherSController mcuOtherSController;

	@FXML
	private VBox cfg_vbox_in;
	@FXML
	private VBox cfg_vbox_ex;
	@FXML
	private AnchorPane cfg_vbox_ad;
	private Map<String, TitledPane> cfgMap = Maps.newHashMap();

	public MainMCUController() {
//		mcuClockController = new MCUClockController();
	}

	@Override
	protected void postInit(AppScene scene) {
		mcuCpuController.setClckCont(clckCont);
		initSubControllers(mcuPinsController, mcuPowerController, mcuSystickController,
				mcuBkpController, mcuEbcController, mcuUsbController, mcuDmaController, mcuMpuController,
				mcuCpuController, mcuI2CController, mcuSpiController, mcuUartController,
				mcuGpioController, mcuDacController, mcuTimerController, mcuCompController,
				mcuOtherSController,
				mcuAdcController, mcuCanController, mcuIwdgController, mcuWwdgController);
		if (!scene.isTestMode()) {
			mcuTabPane.getTabs().remove(mcuOtherSTab);
		}
		if (!scene.isDebugMode()) {
			mcuOtherSTab.setDisable(true);
		}

		makeListeners(cfg_vbox_in.getChildren());
		makeListeners(cfg_vbox_ex.getChildren());
		cfg_vbox_ad.getChildren().add(SyntaxHighlighter.get(getScene()));
		GuiUtils.setAnchors(15.0, cfg_vbox_in, cfg_vbox_ex, cfg_vbox_ad);
//		scene.stopSetupProcess();
		Platform.runLater(scene::stopSetupProcess);
		Platform.runLater(()->log.debug("#postInit - initialized"));
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
		if (comboKey.startsWith("t-cfg")) {
			if (!inValue) return;
			setLastPair(subKey.substring(4));
			boolean prevent = collapseChildren(subKey, cfg_vbox_in.getChildren());
			prevent &= collapseChildren(subKey, cfg_vbox_ex.getChildren());
			if (!prevent) updateCodeGenerator(getPairForComboKey(comboKey));
		} else if (comboKey.startsWith("c-")) {
			subKey = "cfg_" + subKey.toLowerCase().replace("wdg", "wd");
			if (subKey.contains("-")) subKey = subKey.substring(0, subKey.indexOf("-"));
			disableChildren(subKey, cfg_vbox_in.getChildren());
			disableChildren(subKey, cfg_vbox_ex.getChildren());
		}
	}

	private void disableChildren(String subKey, List<Node> children) {
		String cboxKey = subKey.substring(4).toUpperCase();
		for (Node node : children) {
			boolean isCurentTab = node.getId().equals(subKey);
			if (isCurentTab && node instanceof TitledPane) {
				boolean oneChecked = checkOneChecked(cboxKey);
				((TitledPane) node).setExpanded(false);
				node.setDisable(!oneChecked);
			}
		}
	}
	private boolean collapseChildren(String subKey, List<Node> children) {
		String cboxKey = subKey.substring(4).toUpperCase();
		boolean oneChecked = false;
		for (Node node : children) {
			boolean isCurentTab = node.getId().equals(subKey);
			if (node instanceof TitledPane) {
				if (isCurentTab) {
					oneChecked = checkOneChecked(cboxKey);
					if (!oneChecked) ((TitledPane) node).setExpanded(false);
				} else ((TitledPane) node).setExpanded(false);
			}
		}
		return !oneChecked;
	}

	private boolean checkOneChecked(String cboxKey) {
		if (cboxKey.equals("CPU") || cboxKey.equals("GPIO")) return true;
		if (cboxKey.endsWith("WD")) cboxKey += "G";
		CheckBox cb = mcuPinsController.cboxMap().get(cboxKey);
		if (cb != null) return cb.isSelected();
		boolean oneChecked = false;
		for (int i = 0; i < 9; i++) {
			cb = mcuPinsController.cboxMap().get(cboxKey + "-" + i);
			oneChecked |= cb != null && cb.isSelected();
		}
		return oneChecked;
	}
	protected void hideChildPane(Device.EPairNames pair) {
		Boolean pairBlockVisibility = !pair.real() || DeviceFactory.getDevice(getScene()
				.getPinoutsModel().getSelectedBody())
				.getPairCountsArr()[pair.ordinal()] > 0;
		if (checkPairForHide(pair.name())) pairBlockVisibility = false;
		VBox cfg_vbox = pair.ext() ? cfg_vbox_ex : cfg_vbox_in;
		for(Node node: cfg_vbox.getChildren()) {
			String lowPairName = pair.name().toLowerCase();
			if (lowPairName.contains(node.getId().substring(4))) {
				hideTPaneNode(pairBlockVisibility, node);
				break;
			}
		}
	}

	private void hideTPaneNode(Boolean pairBlockVisibility, Node node) {
		node.setVisible(pairBlockVisibility);
		if (pairBlockVisibility) return;
		((Region)node).setMinHeight(0.0);
		((Region)node).setPrefHeight(0.0);
		((Region)node).setMaxHeight(0.0);
	}

	private Map<FN, List<String>> outClasses = Maps.newLinkedHashMap();
	private enum FN {NONE, INIT_H, INIT_C, MAIN_H, MAIN_C, ERR}
	private enum KN {
		NONE {
			public FN h() { return FN.NONE; }
			public FN c() { return FN.NONE; }
		}, INIT {
			public FN h() { return FN.INIT_H; }
			public FN c() { return FN.INIT_C; }
		}, MAIN {
			public FN h() { return FN.MAIN_H; }
			public FN c() { return FN.MAIN_C; }
		}, ERR {
			public FN h() { return FN.ERR; }
			public FN c() { return FN.ERR; }
		};
		public abstract FN h();
		public abstract FN c();
	}
	private void addCode(FN fn, String codeLine) {
		if (!outClasses.containsKey(fn)) {
			outClasses.put(fn, Lists.newArrayList());
		}
		outClasses.get(fn).add(codeLine);
	}
	private void saveCode() {
		List<String> fullCode = Lists.newArrayList();
		for(FN fn: outClasses.keySet()) {
			String fPath = "./" + fn.name().toLowerCase().replace("_", ".");
			Constants.saveTxtList(new File(fPath), outClasses.get(fn), true);
			fullCode.add("");
			fullCode.add("/* " + fPath + " */");
			fullCode.addAll(outClasses.get(fn));
		}
		SyntaxHighlighter.set(getScene(), fullCode);
	}
	public void genAllCode(ActionEvent actionEvent) {
		outClasses.clear();
		addCode(FN.INIT_H, "int init ( void );");

		addCode(FN.INIT_C, "#include init.h;");
		List<String> funcList = Lists.newArrayList();
		for(String pairName: Device.showPairNames()) {
			if (checkPairForHide(pairName)) continue;
			Device.EPairNames pair = Device.EPairNames.valueOf(pairName);
			genCodeIfNedeed(funcList, pairName, pair);
		}
		addCode(FN.INIT_C, "int init ( void ) {");
		addCodeFunc(KN.INIT, "CPU_init");
		for(String funcName: funcList) {
			if (funcName.startsWith("CPU")) continue;
			addCodeFunc(KN.INIT, funcName);
		}
		addCode(FN.INIT_C, "}");

		addCode(FN.MAIN_H, "int main ( void );");

		addCode(FN.MAIN_C, "#include init.h;");
		addCode(FN.MAIN_C, "#include main.h;");
		addCode(FN.MAIN_C, "int main ( void ) {");
		addCode(FN.MAIN_C, "\tinit();");
		addCode(FN.MAIN_C, "}");
		saveCode();
	}

	private void genCodeIfNedeed(List<String> funcList, String pairName, Device.EPairNames pair) {
		getScene().genKind(pair);
		List<String> codeList = pair.model().getCodeList();
		if (codeList.size() > 5) {
            funcList.add(pairName + "_init");
            for(String codeLine: codeList) {
                addCode(FN.INIT_C, codeLine);
            }
        }
	}

	private void addCodeFunc(KN kn, String funcName) {
		addCode(kn.h(), "void " + funcName + "( void );");
		addCode(kn.c(), "\t" + funcName + "();");
	}
}
