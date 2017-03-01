package milandr_ex.data;

import com.google.common.collect.Lists;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import milandr_ex.model.BasicController;

import java.util.List;
import java.util.ResourceBundle;

import static milandr_ex.data.Constants.textToKey;

/**
 * Created by lizard2k1 on 28.02.2017.
 */
public class McuBlockModel {
	private final Device.EPairNames pair;
	private Pane parent;
	private final List<CheckBox> ckBoxes;
	private final List<ComboBox> cmBoxes;
	private final List<String> cbKeys;
	private final List<McuBlockProperty> props;
	private Pane propsPane;
	private BasicController controller;
	private ResourceBundle bundle;

	public McuBlockModel(Device.EPairNames pair) {
		this.pair = pair;
		ckBoxes = Lists.newArrayList();
		cmBoxes = Lists.newArrayList();
		cbKeys = Lists.newArrayList();
		props = Lists.newArrayList();
	}

	public Device.EPairNames getPair() {
		return pair;
	}

	public McuBlockModel setParent(Pane parent) {
		this.parent = parent;
		return this;
	}

	public McuBlockModel addCheckBox(CheckBox checkBox) {
		this.ckBoxes.add(checkBox);
		return this;
	}

	public McuBlockModel addComboBox(ComboBox comboBox) {
		this.cmBoxes.add(comboBox);
		return this;
	}

	public McuBlockModel addModelProp(McuBlockProperty prop) {
		return addModelProp(prop, prop.getName());
	}
	public McuBlockModel addModelProp(McuBlockProperty prop, String... msgKeys) {
		this.props.add(prop);
		if (bundle != null && msgKeys != null) {
			for(String msgKey: msgKeys) {
				String[] propPath = msgKey.split("\\.");
				String propKey = propPath[0];
				String propKeyMsg = propPath[propPath.length - 1];
				prop.setMsgKey(propKey, propKeyMsg);
				String fullMsgKey = "mcu_prop." + propKeyMsg;
				if (bundle.containsKey(fullMsgKey)) {
					prop.setMsgTxt(propKey, bundle.getString(fullMsgKey));
				}
			}
		}
		return this;
	}

	public McuBlockModel setPropsPane(Pane propsPane) {
		this.propsPane = propsPane;
		return this;
	}

	public BasicController getController() {
		return controller;
	}

	public void setController(BasicController controller) {
		this.controller = controller;
	}

	public List<McuBlockProperty> getProps() {
		return props;
	}

	public List<String> getPinsList() {
		List<String> keys = Lists.newArrayList();
		for(ComboBox comboBox: cmBoxes) {
			String key = comboBox.getId();
			SingleSelectionModel model = comboBox.getSelectionModel();
			if (model != null && model.getSelectedIndex() >= 0) {
				String item = (String) model.getSelectedItem();
				if (!controller.filterGpio(key, item)) continue;
				boolean hasSpace = item.contains(" ");
				keys.add(hasSpace ? item //Constants.textToKey(item.split("\\s")[1])
						:  Constants.keyToText(key) + " " + item);
			}
		}
		return keys;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}
