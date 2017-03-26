package milandr_ex.model.mcu.ext;

import com.google.common.collect.Lists;
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

public class MCUGpioController extends MCUExtPairController {
	private static final Logger log	= LoggerFactory.getLogger(MCUGpioController.class);
	@FXML
	private GridPane gpio_gpio;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.GPIO);
		addModelProps(new String[]{"gpio_dir", "gpio_kind", "gpio_funx", "gpio_mode"},
				"each", (List) null, null, null, null);
		addModelProps(new String[]{"gpio_ifin", "gpio_filt"}, "each", "BB");
		addModelProps(new String[]{"gpio_spd"}, "each", (List) null);
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
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		List<String> pinList = getPinList();
		Set<String> pinPorts = Sets.newLinkedHashSet();
		Set<String> pinConfs = Sets.newLinkedHashSet();
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
		for(String pinConf: pinConfs) {
			g().addCodeStr(oldCode, "//gpio config selected: %s", pinConf);
		}
		return oldCode;
	}
}
