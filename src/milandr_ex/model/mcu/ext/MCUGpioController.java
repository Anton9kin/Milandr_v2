package milandr_ex.model.mcu.ext;

import com.google.common.collect.Maps;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.textToKey;

public class MCUGpioController extends MCUExtPairController {
	@FXML
	private GridPane gpio_gpio;
	private Map<String, VBox> gpio_vbox;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.GPIO);
		addModelProps(new String[]{"gpio_dir", "gpio_kind", "gpio_funx", "gpio_mode"},
				"each", (List) null, null, null, null);
		addModelProps(new String[]{"gpio_ifin", "gpio_filt"}, "each", "BB");
		addModelProps(new String[]{"gpio_spd"}, "each", (List) null);
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
	protected ObservableList<Node> clearGpioProps() {
//		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
//		for(String key: gpio_vbox.keySet()) {
//			getDevicePair().model().clearProps(key);
//		}
//		gpio_vbox.clear();
		return super.clearGpioProps();
	}

	@Override
	protected Node getPropsForGpio(VBox vbox, String pinName) {
		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
		gpio_vbox.put(pinName, vbox);
		return super.getPropsForGpio(vbox, pinName);
	}

	@Override
	protected Pane getPropControl(String group) {
		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
		if (!gpio_vbox.containsKey(group)) return getPropControl();
		return gpio_vbox.get(group);
	}
}
