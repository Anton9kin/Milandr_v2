package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import milandr_ex.model.BasicController;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
		if (!groups.containsKey("each")) return clone;
		for (McuBlockProperty item : groups.get("each")) {
			clone.add(item.clone().setGroup(group));
		}

		return clone;
	}

	public List<McuBlockProperty> getGroup(String group) {
		if (group.equals("all")) return props;
		if (!groups.containsKey(group) || groups.get(group).isEmpty()) {
			groups.put(group, cloneGroup(group));
		}
		return groups.get(group);
	}

	public Map<String, McuBlockProperty> getGroupMap(String group) {
		Map<String, McuBlockProperty> result = Maps.newLinkedHashMap();
		List<McuBlockProperty> list = getGroup(group);
		if (list == null || list.isEmpty()) return result;
		for(McuBlockProperty prop: list) result.put(prop.getName(), prop);
		return result;
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
		clearProps(getPair().name());
		return clearProps("all");
	}
	public McuBlockModel clearGroup(String group) {
		List<McuBlockProperty> props = getGroup(group);
		if (props.isEmpty()) return this;
		for(McuBlockProperty prop: props) prop.clear();
		props.clear();
		return this;
	}
	public McuBlockModel clearProps(String group) {
		if (group.equals("all")) groups.clear();
		else if (!groups.containsKey(group)) return this;
		clearGroup(group);
		return this;
	}
	public List<McuBlockProperty> getProps() {
		if (props.isEmpty()) return props;
		return getGroup(getPair().name());
	}

	public McuBlockProperty getProp(String name) {
		return getProp(getPair().name(), name);
	}
	public McuBlockProperty getProp(String group, String name) {
		return getProp(name, getGroup(group));
	}
	public McuBlockProperty getProp(String name, List<McuBlockProperty> props) {
		for(McuBlockProperty prop: props) {
			if (prop.getName().equals(name)) return prop;
		}
		return McuBlockProperty.get(getPair(), name, "0");
	}

	public Set<String> getPinsList() {
		Set<String> keys = Sets.newLinkedHashSet();
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
		save(toSave, props);
		for(String grp: groups.keySet()) {
			save(toSave, getGroup(grp));
		}
	}
	public void save(List<String> toSave, List<McuBlockProperty> props) {
		for(McuBlockProperty prop: props) {
			for(int i = 0; i < prop.getValuesCount(); i++) {
				String valueToSave = String.format("mbm.%s-%s-%s.%d=%d:%s", pair,
						prop.getName(), prop.getGroup(), i, prop.getIntValue(i), prop.getStrValue(i));
				if (valueToSave.matches(".*(\\.0=0:).*")) continue;
				toSave.add(valueToSave);
			}
		}
	}

	public void load(List<String> toLoad) {
		for(String loadStr: toLoad) {
			String[] loadItms = loadStr.split("\\.");
			if (loadItms.length < 3) continue;
			loadOneProp(loadItms[1], loadItms[2], loadItms[2].split("=")[1]);
		}
	}

	public void loadOneProp(String pairName, String valPart, String values) {
		String[] names = pairName.split("-");
		if (names.length < 2) return;
		String pair = names[0], name = names[1], group = names[names.length - 1];
		if (!this.pair.name().equals(pair)) return;
		loadOneGroupProp(props, valPart, values, name);
		if (group.equals("all") || group.equals("each")) return;
		loadOneGroupProp(getGroup(group), valPart, values, name);
	}

	private void loadOneGroupProp(List<McuBlockProperty> props, String valPart, String values, String name) {
		for(McuBlockProperty prop: props) {
            if (!prop.getName().equals(name)) continue;
            String[] valParts = valPart.split("=");
            int valueInd = Integer.parseInt(valParts[0]);
            String[] valVals = values.split(":");
            int intValue = Integer.parseInt(valVals[0]);
            String strValue = valVals[valVals.length - 1];
            prop.setIntValue(intValue, valueInd);
            prop.setStrValue(strValue, valueInd);
        }
	}

	public void load(Map<String, String> toLoad) {
		for(String loadStr: toLoad.keySet()) {
			String[] loadItms = loadStr.split("\\.");
			if (loadItms.length < 2) continue;
			loadOneProp(loadItms[0], loadItms[1], toLoad.get(loadStr));
		}
	}
}
