package milandr_ex.data;

import com.google.common.collect.Lists;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lizard on 01.03.17 at 12:37.
 */
public class McuBlockProperty {
	private static final Logger log	= LoggerFactory.getLogger(McuBlockProperty.class);
	public static ObservableList<String> opUList = FXCollections.observableArrayList("Внутренее", "Внешнее");
	public static ObservableList<String> typeStartList = FXCollections.observableArrayList("Одиночное", "Последовательное");
	public static ObservableList<String> div8List = FXCollections.observableArrayList("/1", "/2", "/4", "/8");
	public static ObservableList<String> div512List = FXCollections.observableArrayList("/4", "/8", "/16", "/32", "/64", "/128", "/256", "/512");
	public static ObservableList<String> unitList = FXCollections.observableArrayList("с", "мс", "мкс", "Гц", "кГц", "МГц");
	public static ObservableList<String> istList = FXCollections.observableArrayList("LSI", "HCLK");
	public static ObservableList<String> modeList = FXCollections.observableArrayList("Таймер", "Задержка");
	public static ObservableList<String> div128List = FXCollections.observableArrayList("/1", "/2", "/4", "/8", "/16", "/32", "/64", "/128");
	public static ObservableList<String> speedList = FXCollections.observableArrayList("10 000", "20 000", "50 000", "100 000", "125 000", "250 000", "500 000", "1000 000");

	public enum PropKind {
		NONE,
		INT, STR, CHK, CMB, LST, CMP,
		ERROR;
	}

	private static class PropValue {
		private int intValue;
		private String strValue;

		public PropValue(int intValue, String strValue) {
			this.intValue = intValue;
			this.strValue = strValue;
		}

		public int getIntValue() {
			return intValue;
		}

		public String getStrValue() {
			return strValue;
		}
	}

	private String alias;
	private final String name;
	private String msgKey;
	private String msgTxt;
	private final PropKind kind;
	private List<McuBlockProperty> subProps;
	private ObservableList<String> subItems;
	private int intDefValue;
	private String strDefValue;
	private int minValue;
	private int maxValue;
	private List<PropValue> values;
	private int valueInd;
	private Node obsNode;
	private ObservableValue obsValue;

	private McuBlockProperty(String name, PropKind kind) {
		this.name = name;
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public McuBlockProperty setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public McuBlockProperty setMsgKey(String msgKey) {
		this.msgKey = msgKey;
		return this;
	}

	public McuBlockProperty setMsgTxt(String msgTxt) {
		this.msgTxt = msgTxt;
		return this;
	}

	public McuBlockProperty setMsgKey(String prop, String msgKey) {
		if (prop.equals(name)) return setMsgKey(msgTxt);
		for(McuBlockProperty sprop: subProps) {
			if (prop.equals(sprop.name)) sprop.setMsgKey(msgKey);
		}
		return this;
	}

	public McuBlockProperty setMsgTxt(String prop, String msgTxt) {
		if (prop.equals(name)) return setMsgTxt(msgTxt);
		for(McuBlockProperty sprop: subProps) {
			if (prop.equals(sprop.name)) sprop.setMsgTxt(msgTxt);
		}
		return this;
	}

	public String getMsgTxt() {
		if (name.equals("-")) return "";
		return msgTxt == null ? name + "-" + msgKey : msgTxt;
	}

	public McuBlockProperty setMinValue(int minValue) {
		this.minValue = minValue;
		return this;
	}

	public McuBlockProperty setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	private void checkValues() {
		if (valueInd < 0) throw new IllegalArgumentException("illegal value index");
		if (values == null) values = Lists.newArrayList();
		while (values.size() <= valueInd) values.add(new PropValue(intDefValue, strDefValue));
	}

	public void setValueInd(int valueInd) {
		this.valueInd = valueInd;
		switch (kind) {
			case INT: setIntValue(getIntValue()); break;
			default: setStrValue(getStrValue()); break;
		}
		if (subProps == null || subProps.isEmpty()) return;
		for(McuBlockProperty sProp: subProps) {
			sProp.setValueInd(valueInd);
		}
	}

	public McuBlockProperty setIntDefValue(int intDefValue) {
		this.intDefValue = intDefValue;
		setIntValue(intDefValue);
		return this;
	}

	public McuBlockProperty setStrDefValue(String strDefValue) {
		this.strDefValue = strDefValue;
		setStrValue(strDefValue);
		return this;
	}

	public McuBlockProperty setIntValue(int intValue) {
		return setIntValue(intValue, false);
	}
	public McuBlockProperty setIntValue(int intValue, boolean inner) {
		checkValues();
		this.values.get(valueInd).intValue = intValue;
		if (!inner) updateObservable(intValue + "");
		return this;
	}

	private void updateObservable(String obsValue) {
		if (this.obsValue == null) return;
		if (obsValue == null || obsValue.equals("null")) return;
		if (this.obsValue instanceof StringProperty) {
			((StringProperty) this.obsValue).setValue(obsValue + "");
		} else if (this.obsValue instanceof BooleanProperty) {
			if (!obsValue.matches("\\d+")) {
				obsValue = obsValue.equals("true") ? "1" : "0";
			}
			((BooleanProperty) this.obsValue).setValue(Integer.parseInt(obsValue) > 0);
		} else if (this.obsValue instanceof ObjectProperty) {
			((ObjectProperty) this.obsValue).setValue(obsValue + "");
		}
	}

	public McuBlockProperty setStrValue(String strValue) {
		return setStrValue(strValue, false);
	}
	public McuBlockProperty setStrValue(String strValue, boolean inner) {
		checkValues();
		String newValue = getNewStrValueWithDef(strValue);
		this.values.get(valueInd).strValue = newValue;
		if (!inner) updateObservable(newValue);
		return this;
	}

	private String getNewStrValueWithDef(String strValue) {
		String newValue = strValue == null ? strDefValue : strValue;
		if (newValue == null) newValue = "";
		if (newValue.isEmpty() && subItems != null && !subItems.isEmpty()) {
			newValue = subItems.get(0);
		}
		return newValue;
	}

	public int getIntValue() {
		checkValues();
		return this.values.get(valueInd).getIntValue();
	}

	public String getStrValue() {
		checkValues();
		return this.values.get(valueInd).getStrValue();
	}

	public void setSubItems(List<String> subItems) {
		if (subItems == null) subItems = Lists.newArrayList();
		this.subItems = FXCollections.observableArrayList(subItems);
	}

	public McuBlockProperty addProp(McuBlockProperty property) {
		if (subProps == null) subProps = Lists.newArrayList();
		subProps.add(property);
		return this;
	}

	public McuBlockProperty makeUI(Pane pane) {
		return makeUI(pane, 0);
	}
	public void makeListener(Node node, ObservableValue property) {
		if (property == null) return;
		obsNode = node;
		obsValue = property;
		property.addListener((e, t1, t2) -> {
			log_debug(String.format("#listen(%s) %s -> %s", getMsgTxt(), t1, t2));
			String t2s = String.valueOf(t2);
			if (t2s.equals("null")) t2s = "";
			if (kind.equals(PropKind.INT) && t2s.matches("\\d+")) {
				setIntValue(Integer.parseInt(t2s), true);
			} else setStrValue(t2s, true);
		});
	}
	private static void log_debug(String text) {
		log.debug(text);
//		System.out.println(text);
	}
	public McuBlockProperty makeUI(Pane pane, int gridIndex) {
		Node node = null;
		switch (kind) {
			case CMP: for(McuBlockProperty prop: subProps) prop.makeUI(pane, gridIndex++); break;
			case CHK: node = new CheckBox("");
						makeListener(node, ((CheckBox) node).selectedProperty()); break;
			case STR: node = new TextField(strDefValue);
						makeListener(node, ((TextField) node).textProperty()); break;
			case INT: node = new TextField(intDefValue + "");
						makeListener(node, ((TextField) node).textProperty()); break;
			case LST: node = new ListView<>(subItems);
						makeListener(node, ((ListView) node).selectedItemProperty()); break;
			case CMB: node = new ComboBox<>(subItems);
						makeListener(node, ((ComboBox) node).valueProperty()); break;
		}
		if (node != null) {
			Label nodeLbl = new Label(getMsgTxt());
			pane.getChildren().add(nodeLbl);
			GridPane.setRowIndex(nodeLbl, gridIndex);
			if (subItems != null && !subItems.isEmpty()) {
				setStrValue(subItems.get(0));
			}
			if (!name.equals("-")) {
				if (node instanceof ComboBox) {
					((ComboBox) node).getSelectionModel().selectFirst();
				}
				pane.getChildren().add(node);
				GridPane.setRowIndex(node, gridIndex);
				GridPane.setColumnIndex(node, 1);
			}
		}
		return this;
	}
	public static class BoolProp extends McuBlockProperty {
		BoolProp(String name) {
			super(name, PropKind.CHK);
		}
	}

	public static class ComboProp extends McuBlockProperty {
		ComboProp(String name) {
			this(name, null);
		}
		ComboProp(String name, List<String> subItems) {
			super(name, PropKind.CMB);
			setSubItems(subItems);
		}
	}

	public static class ListProp extends McuBlockProperty {
		ListProp(String name) {
			this(name, Lists.newArrayList());
		}
		ListProp(String name, List<String> subItems) {
			super(name, PropKind.LST);
			setSubItems(subItems);
		}
	}

	public static class IntProp extends McuBlockProperty {
		IntProp(String name) {
			super(name, PropKind.INT);
		}
		IntProp(String name, int max) {
			this(name);
			setMaxValue(max);
		}
		IntProp(String name, int min, int max) {
			this(name, max);
			setMinValue(min);
		}
	}

	public static class TextProp extends McuBlockProperty {
		TextProp(String name) {
			super(name, PropKind.STR);
		}
		TextProp(String name, String defValue) {
			this(name);
			setStrValue(defValue);
			setStrDefValue(defValue);
		}
	}

	public static class FreqDividor extends McuBlockProperty {
		FreqDividor(String name) {
			this(name, true);
		}
		FreqDividor(String name, boolean simple) {
			this(name, null, null);
		}
		FreqDividor(String name, List<String> divs, List<String> units) {
			super(name, PropKind.CMP);
			addProp(new ComboProp("f_div", divs));
			if (units != null) {
				addProp(new IntProp("f_time"));
				addProp(new ComboProp("f_units", units));
			}
		}
	}

	public static McuBlockProperty get(String name, boolean value) {
		return new McuBlockProperty.BoolProp(name).setIntValue(value ? 1 : 0);
	}

	public static McuBlockProperty get(String name, int value) {
		return new McuBlockProperty.IntProp(name).setIntValue(value);
	}

	public static McuBlockProperty get(String name, int max, int value) {
		return new McuBlockProperty.IntProp(name, max).setIntValue(value);
	}

	public static McuBlockProperty get(String name, int min, int max, int value) {
		return new McuBlockProperty.IntProp(name, min, max).setIntValue(value);
	}

	public static McuBlockProperty get(String name, String value) {
		return new McuBlockProperty.TextProp(name, value);
	}

	public static McuBlockProperty getF(String name, List<String> divs, List<String> units) {
		return new McuBlockProperty.FreqDividor(name, divs, units);
	}

	public static McuBlockProperty getL(String name, List<String> values) {
		return new McuBlockProperty.ListProp(name, values);
	}

	public static McuBlockProperty getC(String name, List<String> values) {
		return new McuBlockProperty.ComboProp(name, values);
	}
}
