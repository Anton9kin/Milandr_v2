package milandr_ex.data;

import com.google.common.collect.Lists;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import milandr_ex.model.BasicController;

import java.util.List;
/**
 * Created by lizard2k1 on 28.02.2017.
 */
public class McuBlockModel {
	private final Device.EPairNames pair;
	private Pane parent;
	private final List<CheckBox> ckBoxes;
	private final List<ComboBox> cmBoxes;
	private final List<String> cbKeys;
	private BasicController controller;

	public McuBlockModel(Device.EPairNames pair) {
		this.pair = pair;
		ckBoxes = Lists.newArrayList();
		cmBoxes = Lists.newArrayList();
		cbKeys = Lists.newArrayList();
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

	public BasicController getController() {
		return controller;
	}

	public void setController(BasicController controller) {
		this.controller = controller;
	}

	public List<String> getPinsList() {
		List<String> keys = Lists.newArrayList();
		for(ComboBox comboBox: cmBoxes) {
			String key = comboBox.getId();
			SingleSelectionModel model = comboBox.getSelectionModel();
			if (model != null && model.getSelectedIndex() >= 0) {
				String item = (String) model.getSelectedItem();
				if (!controller.filterGpio(key, item)) continue;
				keys.add(Constants.keyToText(key));
//				item = item.split("\\s")[1];
//				keys.add(Constants.textToKey(item));
			}
		}
		return keys;
	}
}
