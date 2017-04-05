package milandr_ex.model.mcu.ext;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
		addModelProps(new String[]{"gpio_dir", "gpio_kind", "gpio_funx", "gpio_spo", "gpio_mode1"},
				"each", gpioInOutList, gpioKindList, gpioFuncList, usbPushPullList, gpioMode1List);
		addModelProps(new String[]{"gpio_filt"}, "each", "B");
		addModelProps(new String[]{"gpio_spd", "gpio_mode2"}, "each", gpioSpeedList, gpioMode2List);
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
	protected boolean checkPropByPinGroup(Device.EPairNames pair, List<McuBlockProperty> props,
										  McuBlockProperty prop, String group) {
		if (strHasAnySubstr(prop.getName(), "gpio_mode1", "gpio_mode2", "gpio_spd", "gpio_filt")) {
			if (strHasAnySubstr(group, "IO in", "SIRIN")) {
				return !prop.getName().equals("gpio_mode2");
			} else if (strHasAnySubstr(group, "IO out", "SIROUT")) {
				return prop.getName().equals("gpio_mode2");
			} else {
				switch (getNamedProp(props, "gpio_dir").getIntValue()) {
					case 0:
						return !prop.getName().equals("gpio_mode2");
					case 1:
						return prop.getName().equals("gpio_mode2");
				}
			}
		}
		return super.checkPropByPinGroup(pair, props, prop, group);
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
			switchPropsVis(props);
		}
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		super.callGuiListener(comboKey, prev, value);
		if (!prev.equals("null") && comboKey.endsWith("gpio_dir")) {
			String group = comboKey.substring(8, comboKey.lastIndexOf("-"));
			resetModelProps(group);
			makeUI(getDevicePair(), group);
//			reCreatePinProps(getDevicePair(), group, getPropControl(group), 0);
		}
	}

	private void switchPropsVis(List<McuBlockProperty> props) {
		if (getNamedProp(props, "gpio_dir").getIntValue() == 0) {
			getNamedProps(props, McuBlockProperty::show, "gpio_mode1", "gpio_spd", "gpio_filt");
			getNamedProps(props, McuBlockProperty::hide, "gpio_mode2");
			getNamedProp(props, "gpio_mode1", p->p.setRow(4));
		} else {
			getNamedProps(props, McuBlockProperty::show, "gpio_mode2");
			getNamedProps(props, McuBlockProperty::hide, "gpio_mode1", "gpio_spd", "gpio_filt");
			getNamedProp(props, "gpio_mode2", p->p.setRow(4));
		}
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		Set<String> propList = getDevicePair().model().getGroupMap("each").keySet();
		Set<String> pinList = getPinList();
		if (pinList.isEmpty()) return oldCode;
		Set<String> pinPorts = Sets.newLinkedHashSet();
		Set<String> pinConfs = Sets.newLinkedHashSet();
		Map<Device.EPortNames, Map<String, String[]>> portsStrs = Maps.newLinkedHashMap();
		for(Device.EPortNames port: Device.EPortNames.values()) {
			portsStrs.put(port, Maps.newLinkedHashMap());
		}
		for(String pinText: pinList) {
			g().addCodeStr(oldCode, "//gpio pin selected: %s", pinText);
			String parts[] = pinText.split("\\s");
			String pinPort = "Port_Unk";
			if (parts[0].matches("P[A-F]\\d{1,2}")) {
				pinPort = "Port_" + parts[0].charAt(1) + "";
				pinPorts.add(pinPort);
			}
			Device.EPortNames portName = getePortName(pinPort);
			StringBuilder pinConf = new StringBuilder("\n//Conf: \t").append(pinPort);
			List<McuBlockProperty> group = getDevicePair().model().getGroup(pinText);
			int count = 0;
			boolean isOutp = false;
			for(McuBlockProperty prop: group) {
				Integer intVal = prop.getIntValue();
				String strVal = prop.getStrValue();
				String confValue = getConfValue(prop, intVal, strVal);
				String[] propStrs = computeIfAbsent(portsStrs, portName, prop);
				if (prop.getName().endsWith("dir")) isOutp = intVal > 0;
				updatePropStrs(pinText, prop, propStrs, isOutp);

				pinConf.append(String.format("[%s=%s]", prop.getName(), confValue));
				if (count++ == 3) { count = 0; pinConf.append("\n//\t"); }
			}
			pinConfs.add(pinConf.toString());
		}
		g().addCodeStr(oldCode, "");
		g().addCodeStr(oldCode, "//gpio port selected: %s", Arrays.toString(pinPorts.toArray()));
		g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (");
		String portsEnableStr = "";
		for(String pinPort: pinPorts) {
			portsEnableStr += String.format("|(1UL << %d)", getePortName(pinPort).clk());
		}
		g().addCodeStrR(oldCode, "\t" + portsEnableStr.substring(1) + ");");
		g().addCodeStrL(oldCode, "");
//		for(String pinConf: pinConfs) {
//			g().addCodeStr(oldCode, "//gpio config selected: %s", pinConf);
//		}

		for(Device.EPortNames portName: portsStrs.keySet()) {
			if (portsStrs.get(portName).isEmpty()) continue;
			g().addCodeStr(oldCode, "");
			for(String propName: propList) {
				String[] propVals = portsStrs.get(portName).get(propName);
//				g().addCodeStr(oldCode, "//gpio %s config %s separated: %s", portName, propName, Arrays.toString(propVals));
				updateCodeStrWithProps(oldCode, portName.name(), propName, propVals);
			}
		}
		return oldCode;
	}

	private String getConfValue(McuBlockProperty prop, Integer intVal, String strVal) {
		String confValue = strVal;
		switch (prop.getKind()) {
			case INT: confValue = intVal + ""; break;
			case LST: confValue = intVal + ""; break;
			case CMB: confValue = intVal + ""; break;
			case CHK: confValue = strVal.equals("true") ? "true" : "false"; break;
		}
		return confValue;
	}

	private void updateCodeStrWithProps(List<String> code, String port, String func, String[] values) {
		checkPropFuncs(func);
		updateCodeStrWithProps(code, port, propFuncs.get(func), values,
				!func.endsWith("dir") && !func.endsWith("kind"));
	}

	private Map<String, String> propFuncs = Maps.newLinkedHashMap();
	private void checkPropFuncs(String func) {
		if (propFuncs.isEmpty()) {
			for(String propFunc: Constants.propFuncs) {
				String[] propParts = propFunc.split(":");
				propFuncs.put(propParts[0], propParts[1]);
			}
		}
		if (!propFuncs.containsKey(func)) propFuncs.put(func, func);
	}

	private void updateCodeStrWithProps(List<String> code, String port, String func, String[] values, boolean skipFirst) {
		int ind = 0;
		for(String value: values) {
			if (ind == 0 && skipFirst) {ind++; continue;}
			updateCodeStrWithProps(code, port, func, (ind++ == 0 ? "&=~" : "|="), value);
		}
	}
	private void updateCodeStrWithProps(List<String> code, String port, String func, String opp, String values) {
		if (values.isEmpty()) return;
		if (func.length() <= 4) func += "\t";
		g().addCodeStr(code, "MDR_PORT%s->%s %s\t(%s)", port, func, opp, values);
	}
	private void updatePropStrs(String pinText, McuBlockProperty prop, String[] propStrs, boolean isOutp) {
		String pinStrs = propStrs[prop.getIntValue()];
//		pinStrs += "[" + pinText.split("\\s")[0] + "]";
		Integer propInt = prop.getIntValue();
		String propEnd = prop.getName().substring(prop.getName().lastIndexOf("_") + 1);
		int portIndex = Integer.parseInt(pinText.split("\\s")[0].charAt(2) + "");
		String pref = genPropPref(propInt, propEnd);
		String suff = genPropSuff(isOutp, propInt, propEnd, portIndex);
		pinStrs += pref + " << " + suff;
		if (pinStrs.startsWith("|")) pinStrs = pinStrs.substring(1);
		propStrs[prop.getIntValue()] = pinStrs;
	}

	private String genPropSuff(boolean isOutp, Integer propInt, String propEnd, int portIndex) {
		String suff = "";
		if (!propEnd.equals("mode2")) isOutp = false;
		if (propEnd.equals("spo")) isOutp = propInt < 2;
//		int portInt = portIndex;
		switch(propEnd) {
			case "spo" : case "mode1" :case "mode2" :
				if (!isOutp) {
					suff = " << 16" + suff;
//					portInt <<= 16;
				}
				break;
			case "funx" : case "spd" :
				suff = " * 2" + suff;
//				portInt *= 2;
				break;
		}
		suff = portIndex + suff + ")";
		if (propEnd.equals("spo") && propInt == 3) {
			suff += "|(1 << " + portIndex + ")";
		}
		return suff;
	}

	private String genPropPref(Integer propIntVal, String propEnd) {
		int propInt = 0;
		switch (propEnd) {
			case "spd" : propInt = 1;
			case "funx" :
				propInt += propIntVal;
				break;
			case "spo" :
				propInt = propIntVal > 0 ? 1 : 0;
				break;
			default: propInt = 1; break;
		}
		return "|(" + propInt;
	}

	private String[] computeIfAbsent(Map<Device.EPortNames, Map<String, String[]>> portsStrs,
									 Device.EPortNames portName, McuBlockProperty prop) {
		return portsStrs.get(portName).computeIfAbsent(prop.getName(), k -> {
			String[] result = new String[9];
			Arrays.fill(result, "");
			return result;
		});
	}

	@Override
	protected boolean isCboxChecked(int index) {
		return true;
	}

	private Device.EPortNames getePortName(String pinPort) {
		return Device.EPortNames.valueOf(pinPort.substring(5));
	}
}
