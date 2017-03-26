package milandr_ex.model.mcu.ext;

import com.google.common.collect.Maps;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import milandr_ex.model.BasicController;

import java.util.Map;

/**
 * Implementation for gpio tabs of pairs from extend group
 * Created by lizard2k1 on 28.02.2017.
 */
public abstract class MCUExtPairController extends BasicController {

	private Map<String, VBox> gpio_vbox;
	private Map<String, TitledPane> gpio_tpane;
	@Override
	protected boolean isExtPair() { return true; }
	@Override
	protected ObservableList<Node> clearGpioProps() {
		if (gpio_tpane == null) gpio_tpane = Maps.newHashMap();
		gpio_tpane.clear();
//		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
//		for(String key: gpio_vbox.keySet()) {
//			getDevicePair().model().clearProps(key);
//		}
//		gpio_vbox.clear();
		return super.clearGpioProps();
	}

	@Override
	protected Node getPropsForGpio(TitledPane parent, VBox vbox, String pinName) {
		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
		if (gpio_tpane == null) gpio_tpane = Maps.newHashMap();
		gpio_tpane.put(pinName, parent);
		gpio_vbox.put(pinName, vbox);
		return super.getPropsForGpio(parent, vbox, pinName);
	}

	@Override
	protected Pane getPropControl(String group) {
		if (gpio_vbox == null) gpio_vbox = Maps.newHashMap();
		if (!gpio_vbox.containsKey(group)) return getPropControl();
		return gpio_vbox.get(group);
	}
	@Override
	public Map<String, ? extends Node> nodeMap() {
		return gpio_vbox;
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		if (comboKey == null || value == null) return;
		// do not valid values for clock combo-boxes
		if (value.equals("null") || value.equals("RESET")) return;
		super.callGuiListener(comboKey, prev, value);
		log_debug(log, String.format("#callGuiListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		if (comboKey.startsWith("t-gp-")) {
			collapseOtherTPanes(gpio_tpane, comboKey.substring(5), value.equals("true"));
		}
	}
}
