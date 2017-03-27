package milandr_ex.model.mcu.ext;

import com.google.common.collect.Sets;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static milandr_ex.data.Constants.textToKey;
import static milandr_ex.data.McuBlockProperty.*;
import static milandr_ex.utils.StringUtils.strHasAnySubstr;

public class MCUGpioController extends MCUExtPairController {
	private static final Logger log	= LoggerFactory.getLogger(MCUGpioController.class);
	@FXML
	private GridPane gpio_gpio;

	@SuppressWarnings("unchecked")
	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.GPIO);
		addModelProps(new String[]{"gpio_dir", "gpio_kind", "gpio_funx", "gpio_spo", "gpio_mode1", "gpio_mode2"},
				"each", gpioInOutList, gpioKindList, gpioFuncList, usbPushPullList, gpioMode1List, gpioMode2List);
		addModelProps(new String[]{"gpio_filt"}, "each", "B");
		addModelProps(new String[]{"gpio_spd"}, "each", gpioSpeedList);
		log.debug("#postInit - initialized");
	}

	@Override
	protected Parent getGPIOControl() {
		return gpio_gpio;
	}

	@Override
	public boolean filterGpio(String key, String item) {
		if (item != null && !item.contains(" ") && !item.equals("-") && item.length() > 3) {
			if (textToKey(item).substring(0, 3).startsWith("TMR")) return false;
			if (textToKey(item).substring(0, 3).startsWith("ADC")) return false;
			if (textToKey(item).substring(0, 4).startsWith("COMP")) return false;
			if (Device.extPairNames().contains(textToKey(item).substring(0, 3)) ||
					Device.extPairNames().contains(textToKey(item).substring(0, 4))) return false;
		}
		return key != null && key.startsWith("cb")
				&& item != null && !item.equals("RESET");
	}

	@Override
	protected void controlPropByPinGroup(Device.EPairNames pair, List<McuBlockProperty> props,
										 McuBlockProperty prop, String group) {
		super.controlPropByPinGroup(pair, props, prop, group);
		if (!getDevicePair().equals(pair)) return;
		if (group.equals(pair.name())) return;
		if (prop.getName().equals("gpio_dir")) {
			if (strHasAnySubstr(group, "IO in", "SIRIN")) {
				prop.setStrValue(gpioInOutList.get(0)).setRO(true);
			} else if (strHasAnySubstr(group, "IO out", "SIROUT")) {
				prop.setStrValue(gpioInOutList.get(1)).setRO(true);
			}
		}
		if (getNamedProp(props, "gpio_dir").getIntValue() == 0) {
			getNamedProps(props, McuBlockProperty::show, "gpio_mode1", "gpio_spd", "gpio_filt");
			getNamedProps(props, McuBlockProperty::hide, "gpio_mode2");
		} else {
			getNamedProps(props, McuBlockProperty::show, "gpio_mode2");
			getNamedProps(props, McuBlockProperty::hide, "gpio_mode1", "gpio_spd", "gpio_filt");
		}
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		List<String> pinList = getPinList();
		Set<String> pinPorts = Sets.newLinkedHashSet();
		Set<String> pinConfs = Sets.newLinkedHashSet();
		String[] portsInpStrs = new String[]{};
		String[] portsOutStrs = new String[]{};
		String[] portsAnlgStrs = new String[]{};
		String[] portsDigStrs = new String[]{};
		String[] portsPullStrs = new String[]{};
		String[] portsPushStrs = new String[]{};
		String[] portsFuncStrs = new String[]{};
		for(String pinText: pinList) {
			g().addCodeStr(oldCode, "//gpio pin selected: %s", pinText);
			String parts[] = pinText.split("\\s");
			String pinPort = "Port_Unk";
			if (parts[0].matches("P[A-F]\\d{1,2}")) {
				pinPort = "Port_" + parts[0].charAt(1) + "";
				pinPorts.add(pinPort);
			}
			StringBuilder pinConf = new StringBuilder("\n//Conf: \t").append(pinPort);
			List<McuBlockProperty> group = getDevicePair().model().getGroup(pinText);
			int count = 0;
			for(McuBlockProperty prop: group) {
				Integer intVal = prop.getIntValue();
				String strVal = prop.getStrValue();
				String confValue = strVal;
				switch (prop.getKind()) {
					case INT: confValue = intVal + ""; break;
					case LST: confValue = intVal + ""; break;
					case CMB: confValue = intVal + ""; break;
					case CHK: confValue = strVal.equals("true") ? "true" : "false"; break;
				}
				pinConf.append(String.format("[%s=%s]", prop.getName(), confValue));
				if (count++ == 3) { count = 0; pinConf.append("\n//\t"); }
			}
			pinConfs.add(pinConf.toString());
		}
		for(String pinPort: pinPorts) {
			g().addCodeStr(oldCode, "//gpio port selected: %s", pinPort);
		}
		g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (");
		String portsEnableStr = "";
		for(String pinPort: pinPorts) {
			portsEnableStr += String.format("|(1UL << %d)", Device.EPortNames.valueOf(pinPort.substring(5)).clk());
		}
		g().addCodeStrR(oldCode, "\t" + portsEnableStr.substring(1) + ");");
		g().addCodeStrL(oldCode, "");
		for(String pinConf: pinConfs) {
			g().addCodeStr(oldCode, "//gpio config selected: %s", pinConf);
		}

		return oldCode;
	}
}
