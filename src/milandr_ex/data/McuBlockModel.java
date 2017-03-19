package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import milandr_ex.model.BasicController;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Mcu Block Model
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
	private final List<String> codeList;
	private final Map<String, List<McuBlockProperty>> groups;

	public McuBlockModel(Device.EPairNames pair) {
		this.pair = pair;
		ckBoxes = Lists.newArrayList();
		cmBoxes = Lists.newArrayList();
		cbKeys = Lists.newArrayList();
		props = Lists.newArrayList();
		codeList = Lists.newArrayList();
		groups = Maps.newHashMap();
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
		this.props.add(prop.setPair(pair));
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
		if (!prop.hasGroup()) prop.setGroup(pair.name());
		addPropToGroup(prop);
		return this;
	}

	private McuBlockModel addPropToGroup(McuBlockProperty prop) {
		String group = prop.getGroup();
		if (!groups.containsKey(group)) {
			groups.put(group, Lists.newArrayList());
		}
		List<McuBlockProperty> props = groups.get(group);
		props.add(prop);
		return this;
	}

	private List<McuBlockProperty> cloneGroup(String group) {
		List<McuBlockProperty> clone = Lists.newArrayList();
		if (!groups.containsKey(group)) return clone;
		for (McuBlockProperty item : groups.get(group)) {
			clone.add(item.clone());
		}
		return clone;
	}

	public List<McuBlockProperty> getGroup(String group) {
		if (!groups.containsKey(group)) {
			return cloneGroup("each");
		}
		return groups.get(group);
	}

	public McuBlockModel setPropsPane(Pane propsPane) {
		this.propsPane = propsPane;
		return this;
	}

	public BasicController getController() {
		return controller;
	}

	public McuBlockModel setController(BasicController controller) {
		this.controller = controller;
		return this;
	}

	public McuBlockModel clearProps() {
		return clearProps(pair.name());
	}
	public McuBlockModel clearAllProps() {
		return clearProps("all");
	}
	public McuBlockModel clearProps(String group) {
		if (props.isEmpty()) return this;
		if (!groups.containsKey(group)) return this;
		if (group.equals("all")) {
			groups.clear();
			props.clear();
		}
		getGroup(group).clear();
		return this;
	}
	public List<McuBlockProperty> getProps() {
		if (props.isEmpty()) return props;
		return getGroup(pair.name());
	}

	public McuBlockProperty getProp(String name) {
		for(McuBlockProperty prop: props) {
			if (prop.getName().equals(name)) return prop;
		}
		return McuBlockProperty.get(getPair(), name, "0");
	}

	public List<String> getPinsList() {
		List<String> keys = Lists.newArrayList();
		for(ComboBox comboBox: cmBoxes) {
			String key = comboBox.getId();
			SingleSelectionModel model = comboBox.getSelectionModel();
			if (model != null && model.getSelectedIndex() >= 0) {
				String item = (String) model.getSelectedItem();
				if (!controller.filterGpio(key, item)) continue;
				boolean hasSpace = item.contains(" ") && !item.startsWith("IO");
				keys.add(hasSpace ? item //Constants.textToKey(item.split("\\s")[1])
						:  Constants.keyToText(key) + " " + item);
			}
		}
		return keys;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		this.props.clear();
	}

	public boolean togglePropValues(final int index) {
		for(McuBlockProperty prop: props) {
			prop.setValueInd(index);
		}
		return true;
	}

	public McuBlockModel setCodeList(List<String> codeList) {
		this.codeList.clear();
		this.codeList.addAll(codeList);
		return this;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	@Override
	public String toString() {
		return "McuBlockModel{" +
				"pair=" + pair +
				", parent=" + parent +
				", cbKeys=" + cbKeys +
				", props=" + props +
				'}';
	}

	public void save(List<String> toSave) {
		for(McuBlockProperty prop: props) {
			for(int i = 0; i < prop.getValuesCount(); i++) {
				toSave.add(String.format("mbm.%s.%s.%d=%d:%s", pair, prop.getName(),
						i, prop.getIntValue(i), prop.getStrValue(i)));
			}
		}
	}

	public void load(List<String> toLoad) {
		for(String loadStr: toLoad) {
			String[] loadItms = loadStr.split("\\.");
			if (!pair.name().equals(loadItms[1])) continue;
			for(McuBlockProperty prop: props) {
				if (!prop.getName().equals(loadItms[2])) continue;
				String[] valParts = loadItms[3].split("=");
				int valueInd = Integer.parseInt(valParts[0]);
				int intValue = Integer.parseInt(valParts[1].split(":")[0]);
				String strValue = valParts[1].split(":")[1];
				prop.setIntValue(intValue, valueInd);
				prop.setStrValue(strValue, valueInd);
			}
		}
	}
}
