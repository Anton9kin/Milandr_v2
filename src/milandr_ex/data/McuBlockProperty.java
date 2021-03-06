package milandr_ex.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
//import jfxtras.scene.control.ListView;
import milandr_ex.utils.guava.Lists;
import milandr_ex.utils.guava.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Mcu Block Property implementation
 * Created by lizard on 01.03.17 at 12:37.
 */
public class McuBlockProperty implements Cloneable {
	private static final Logger log	= LoggerFactory.getLogger(McuBlockProperty.class);
	public static ObservableList<String> opUList = FXCollections.observableArrayList("Внутренее", "Внешнее");
	public static ObservableList<String> typeStartList = FXCollections.observableArrayList("Одиночное", "Последовательное");
	public static ObservableList<String> div8List = FXCollections.observableArrayList("/1", "/2", "/4", "/8");
	public static ObservableList<String> div512List = FXCollections.observableArrayList("/4", "/8", "/16", "/32", "/64", "/128", "/256", "/512");
	public static ObservableList<String> div2048List = FXCollections.observableArrayList("/1", "/2", "/4", "/8", "/16", "/32", "/64", "/128", "/256", "/512", "/1024", "/2048");
	public static ObservableList<String> unitList = FXCollections.observableArrayList("с", "мс", "мкс", "Гц", "кГц", "МГц");
	public static ObservableList<String> istList = FXCollections.observableArrayList("LSI", "HCLK");
	public static ObservableList<String> adcSrcList = FXCollections.observableArrayList("ADC_CLK", "HCLK");
	public static ObservableList<String> modeList = FXCollections.observableArrayList("Таймер", "Задержка");
	public static ObservableList<String> div128List = FXCollections.observableArrayList("/1", "/2", "/4", "/8", "/16", "/32", "/64", "/128");
	public static ObservableList<String> speedList = FXCollections.observableArrayList("10 000", "20 000", "50 000", "100 000", "125 000", "250 000", "500 000", "1000 000");
	public static ObservableList<String> uartList = FXCollections.observableArrayList("2 400 бит/с", "4 800 бит/с", "9 600 бит/с",
					"14 400 бит/с","19 200 бит/с","28 800 бит/с","38 400 бит/с",
					"57 600 бит/с","76 800 бит/с","115 200 бит/с","230 400 бит/с",
					"460 800 бит/с","921 600 бит/с");
	public static ObservableList<String> uartStopList = FXCollections.observableArrayList("нет", "2", "3", "4", "5", "6", "7", "8");
	public static ObservableList<String> uartParityList = FXCollections.observableArrayList("нет", "2", "3", "4", "5", "6", "7", "8");
	public static ObservableList<String> uccList = FXCollections.observableArrayList("2.0 B", "2.2 B", "2.4 B", "2.6 B", "2.8 B", "3.0 B", "3.2 B", "3.4 B");
	public static ObservableList<String> buccList = FXCollections.observableArrayList("1.8 B", "2.2 B", "2.6 B", "3.0 B");

	public static ObservableList<String> usbModeList = FXCollections.observableArrayList("Device", "Host");
	public static ObservableList<String> usbSpeedList = FXCollections.observableArrayList("1.5 Мбит/с", "12 Мбит/с");
	public static ObservableList<String> usbPolarList = FXCollections.observableArrayList("Low Speed", "High Speed");
	public static ObservableList<String> usbPushPullList = FXCollections.observableArrayList("Подтяжки нет", "Подтяжка к GND", "Подтяжка к VCC", "Обе две");
	public static ObservableList<String> gpioInOutList = FXCollections.observableArrayList("In (Вход)", "Out (Выход)");
	public static ObservableList<String> gpioKindList = FXCollections.observableArrayList("Аналоговый", "Цифровой");
	public static ObservableList<String> gpioMode1List = FXCollections.observableArrayList("Выключен", "Включен");
	public static ObservableList<String> gpioMode2List = FXCollections.observableArrayList("Управляемый драйвер", "Открытый сток");
	public static ObservableList<String> gpioFuncList = FXCollections.observableArrayList("Порт", "Основная", "Альтернативная", "Переопределенная");
	public static ObservableList<String> gpioSpeedList = FXCollections.observableArrayList("Медленная", "Быстрая", "Максимальная ");

	public static boolean USE_HBOX = Boolean.FALSE;
	public enum PropKind {
		NONE,
		INT, STR, CHK, CMB, LST, CMP,
		ERROR;
	}

	public interface PropAction {
		void exec(McuBlockProperty prop);
	}
	private static class PropValue {
		private boolean innerChange;
		private int intValue;
		private String strValue;

		public PropValue(int intValue, String strValue) {
			this.intValue = intValue;
			this.strValue = strValue;
		}

		public int getIntValue() {
			if (intValue < 0 && strValue != null && strValue.matches("\\d+")) {
				return Integer.parseInt(strValue);
			}
			return intValue;
		}

		public String getStrValue() {
			if (strValue == null) {
				return "";
			}
			return strValue;
		}

		@Override
		public String toString() {
			return "PropValue{" +
					"intValue=" + intValue +
					", strValue='" + strValue + '\'' +
					'}';
		}
	}

	private String alias;
	private String group;
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
	private Node nodeLbl;
	private Pane obsPane;
	private ObservableValue obsValue;
	private Device.EPairNames pair;
	private boolean readOnly;

	protected McuBlockProperty(String name, PropKind kind) {
		this.name = name;
		this.kind = kind;
	}

	public Device.EPairNames getPair() {
		return pair;
	}

	public McuBlockProperty setPair(Device.EPairNames pair) {
		if (pair == null) throw new NullPointerException("invalid pair for model property");
		this.pair = pair;
		if (subProps == null) return this;
		for(McuBlockProperty sprop: subProps) {
			sprop.setPair(pair);
		}
		return this;
	}

	public String getName() {
		return name;
	}

	public McuBlockProperty setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public boolean hasGroup() {
		return group != null && !group.isEmpty();
	}

	public McuBlockProperty setGroup(String group) {
		this.group = group;
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
		checkValues(valueInd);
	}
	private void checkValues(int valueInd) {
		if (valueInd < 0) throw new IllegalArgumentException("illegal value index");
		if (values == null) values = Lists.newArrayList();
		while (values.size() <= valueInd) values.add(new PropValue(intDefValue, strDefValue));
	}

	public void setValueInd(int valueInd) {
		setValueInd(valueInd, false);
	}
	public void setValueInd(int valueInd, boolean inner) {
		this.valueInd = valueInd;
		switch (kind) {
			case INT: setIntValue(getIntValue(), inner); break;
			default: setStrValue(getStrValue(), inner); break;
		}
		if (subProps == null || subProps.isEmpty()) return;
		for(McuBlockProperty sProp: subProps) {
			sProp.setValueInd(valueInd, inner);
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

	public McuBlockProperty setRO(boolean readOnly) {
		this.readOnly = readOnly;
		if (obsNode != null) {
			obsNode.setDisable(readOnly);
		}
		return this;
	}

	public McuBlockProperty setIntValue(int intValue) {
		return setIntValue(intValue, valueInd);
	}
	public McuBlockProperty setIntValue(int intValue, int valueInd) {
		return setIntValue(intValue, valueInd, false);
	}
	public McuBlockProperty setIntValue(int intValue, boolean inner) {
		return setIntValue(intValue, valueInd, inner);
	}
	public McuBlockProperty setIntValue(int intValue, int valueInd, boolean inner) {
		checkValues(valueInd);
		this.values.get(valueInd).intValue = intValue;
		if (valueInd != this.valueInd) return this;
		if (!inner) updateObservable(intValue + "");
		return this;
	}

	private void updateObservable(String obsValue) {
		if (this.obsValue == null) return;
		this.values.get(valueInd).innerChange = true;
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
		this.values.get(valueInd).innerChange = false;
	}

	public McuBlockProperty setStrValue(String strValue) {
		return setStrValue(strValue, valueInd);
	}
	public McuBlockProperty setStrValue(String strValue, int valueIndex) {
		return setStrValue(strValue, valueIndex, false);
	}
	public McuBlockProperty setStrValue(String strValue, boolean inner) {
		return setStrValue(strValue, valueInd, inner);
	}
	public McuBlockProperty setStrValue(String strValue, int valueInd, boolean inner) {
		checkValues(valueInd);
		String newValue = getNewStrValueWithDef(strValue);
		this.values.get(valueInd).strValue = newValue;
		this.values.get(valueInd).intValue = getNewIntValue(newValue);
		if (valueInd != this.valueInd) return this;
		if (!inner) updateObservable(newValue);
		return this;
	}

	private String getNewStrValueWithDef(String strValue) {
		String newValue = strValue == null ? strDefValue : strValue;
		if (newValue == null) newValue = "";
		boolean isNumStr = newValue.matches("\\d+");
		if ((newValue.isEmpty() || isNumStr)
				&& subItems != null && !subItems.isEmpty()) {
			int ind = isNumStr ? Integer.parseInt(newValue) : 0;
			if (ind >= subItems.size()) ind = 0;
			newValue = subItems.get(ind);
		}
		return newValue;
	}

	private Integer getNewIntValue(String strValue) {
		Integer newValue = intDefValue;
		if (newValue == 0 && subItems != null && !subItems.isEmpty()) {
			newValue = subItems.indexOf(strValue);
		} else if (strValue.equals("true")) newValue = 1;
		return newValue;
	}

	public int getIntValue() {
		return getIntValue(valueInd);
	}
	public int getIntValue(int valueInd) {
		checkValues(valueInd);
		return this.values.get(valueInd).getIntValue();
	}
	public String getStrValue() {
		return getStrValue(valueInd);
	}
	public String getStrValue(int valueInd) {
		checkValues(valueInd);
		PropValue propValue = this.values.get(valueInd);
		return "" + (kind.equals(PropKind.INT) ? propValue.getIntValue()
				: propValue.getStrValue());
	}
	public int getValuesCount() {
		checkValues();
		return values.size();
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

	public McuBlockProperty getProp(String name) {
		if (subProps == null) return this;
		for(McuBlockProperty prop: subProps) {
			if (prop.getName().equals(name)) return prop;
		}
		return this;
	}

	public boolean isBul() { return kind.equals(PropKind.CHK); }
	public boolean isCmb() { return kind.equals(PropKind.CMB); }
	public boolean isInt() { return kind.equals(PropKind.INT); }
	public boolean isLst() { return kind.equals(PropKind.LST); }
	public boolean isStr() { return kind.equals(PropKind.STR); }
	public PropKind getKind() { return kind; }
	public McuBlockProperty hide() { return swich(false); }
	public McuBlockProperty show() { return swich(true); }
	private McuBlockProperty swich(boolean visible) {
		if (obsNode != null) {
			obsNode.setVisible(visible);
			nodeLbl.setVisible(visible);
			((Region)obsNode).setPrefHeight(visible ? 12.0 : 0);
			((Region)nodeLbl).setPrefHeight(visible ? 12.0 : 0);
		}
		return this;
	}
	public McuBlockProperty setRow(int row) {
		if (obsNode != null) {
			GridPane.setRowIndex(obsNode, row);
			GridPane.setRowIndex(nodeLbl, row);
		}
		return this;
	}

	public McuBlockProperty makeUI(Pane pane) {
		return makeUI(null, pane, 0, true);
	}
	public Node getUI() { return obsNode; }
	public Pane getPane() { return obsPane; }
	public McuBlockProperty clear() {
		if (obsPane != null) {
			obsPane.getChildren().removeAll(obsNode, nodeLbl);
			obsPane = null;
		}
		obsNode = null;
		nodeLbl = null;
		return this;
	}
	public void makeListener(AppScene scene, Node node, ObservableValue property) {
		if (property == null) return;
		obsNode = node;
		obsValue = property;
		property.addListener((e, t1, t2) -> {
			if (t1 == null || scene.isSetupInProcess()) return;
			if (values.get(valueInd).innerChange) return;
			String propKey = String.format("bp-%s-%s-%s", getPair().name(), getGroup(), getName());
			log_debug(String.format("#listen(%s = %s) %s -> %s", propKey, getMsgTxt(), t1, t2));
			String t1s = String.valueOf(t1);
			if (t1s.equals("null")) return;
			String t2s = String.valueOf(t2);
			if (t2s.equals("null")) t2s = "";
			if (kind.equals(PropKind.INT) && t2s.matches("\\d+")) {
				setIntValue(Integer.parseInt(t2s), true);
			} else setStrValue(t2s, true);
			scene.getMainController().callGuiListener(propKey, String.valueOf(t1), String.valueOf(t2));
			scene.getCodeGenerator().listenPinsChanges(scene.getDevice(), getPair(), scene.getPinoutsModel());
			if (scene.getMainController() != null && getPair() != null) {
				scene.getMainController().updateCodeGenerator(getPair().name());
			}
		});
	}
	private static void log_debug(String text) {
		log.debug(text);
//		System.out.println(text);
	}

	@Override
	protected McuBlockProperty clone() {
		McuBlockProperty clone = new McuBlockProperty(name, kind);
		clone.setGroup(getGroup());
		clone.setAlias(alias);
		clone.setMsgKey(msgKey);
		clone.setMsgTxt(msgTxt);
//		private List<McuBlockProperty> subProps;
		if (subItems != null) {
			clone.setSubItems(FXCollections.observableArrayList(subItems));
		}
		clone.setIntDefValue(intDefValue);
		clone.setStrDefValue(strDefValue);
		clone.setMinValue(minValue);
		clone.setMaxValue(maxValue);
//		private List<PropValue> values;
		clone.setValueInd(valueInd);
//		private Node obsNode;
//		private ObservableValue obsValue;
		clone.setPair(pair);
		clone.setRO(readOnly);
		return clone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		McuBlockProperty that = (McuBlockProperty) o;
		return intDefValue == that.intDefValue &&
				minValue == that.minValue &&
				maxValue == that.maxValue &&
				readOnly == that.readOnly &&
				Objects.equal(alias, that.alias) &&
				Objects.equal(group, that.group) &&
				Objects.equal(name, that.name) &&
				Objects.equal(msgKey, that.msgKey) &&
				Objects.equal(msgTxt, that.msgTxt) &&
				kind == that.kind &&
				Objects.equal(strDefValue, that.strDefValue) &&
				pair == that.pair;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(alias, group, name, msgKey, msgTxt, kind,
				intDefValue, strDefValue, minValue, maxValue, pair, readOnly);
	}

	@Override
	public String toString() {
		return "McuBlockProperty{" +
				"alias='" + alias + '\'' +
				", name='" + name + '\'' +
				", msgKey='" + msgKey + '\'' +
				", msgTxt='" + msgTxt + '\'' +
				", kind=" + kind +
				", subProps=" + subProps +
				", subItems=" + subItems +
				", intDefValue=" + intDefValue +
				", strDefValue='" + strDefValue + '\'' +
				", minValue=" + minValue +
				", maxValue=" + maxValue +
				", values=" + values +
				", valueInd=" + valueInd +
				", obsValue=" + obsValue +
				", pair=" + pair +
				'}';
	}

	public McuBlockProperty makeUI(AppScene scene, Pane pane, int gridIndex, boolean force) {
		if (!force && obsNode != null) return this;
		if (obsPane == null) obsPane = pane;
		if (force && obsPane != null) obsPane.getChildren().removeAll(obsNode, nodeLbl);
		boolean separator = name.equals("-");
		Node node = null;
		switch (kind) {
			case CMP: for(McuBlockProperty prop: subProps) prop.makeUI(scene, pane, gridIndex++, force); break;
			case CHK: node = new CheckBox("");
				makeListener(scene, node, ((CheckBox) node).selectedProperty()); break;
			case STR: node = new TextField(strDefValue);
						makeListener(scene, node, ((TextField) node).textProperty()); break;
			case INT: node = new TextField(intDefValue + "");
						makeListener(scene, node, ((TextField) node).textProperty()); break;
//			case LST: node = new ListView<>(subItems);
//						makeListener(scene, node, ((ListView) node).selectedItemProperty()); break;
			case CMB: node = new ComboBox<>(subItems);
						makeListener(scene, node, ((ComboBox) node).valueProperty()); break;
		}
		if (node != null) {
			nodeLbl = new Label(getMsgTxt());
			String value = getStrValue();
			if (value.isEmpty() && subItems != null && !subItems.isEmpty()) {
				setStrValue(subItems.get(0));
			}
			if (!separator) {
				if (node instanceof ComboBox) setStrValue(getStrValue());
				if (kind.equals(PropKind.CHK)) {
					GridPane.setHalignment(node, HPos.RIGHT);
					setStrValue(getStrValue());
				}
				else ((Control)node).setMaxWidth(Double.MAX_VALUE);
			}
			node.setDisable(readOnly);
			setValueInd(0, true);
			if (USE_HBOX) {
				HBox hBox = new HBox();
				hBox.getChildren().add(nodeLbl);
				if (!separator) hBox.getChildren().add(node);
				GridPane.setRowIndex(hBox, gridIndex);
				pane.getChildren().add(hBox);
			} else {
				pane.getChildren().add(nodeLbl);
				if (!separator) pane.getChildren().add(node);
				GridPane.setRowIndex(nodeLbl, gridIndex);
				if (!separator) GridPane.setRowIndex(node, gridIndex);
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

	public static McuBlockProperty get(Device.EPairNames pair, String name, boolean value) {
		return new McuBlockProperty.BoolProp(name).setIntValue(value ? 1 : 0).setPair(pair);
	}

	public static McuBlockProperty get(Device.EPairNames pair, String name, int value) {
		return new McuBlockProperty.IntProp(name).setIntValue(value).setPair(pair);
	}

	public static McuBlockProperty get(Device.EPairNames pair, String name, int max, int value) {
		return new McuBlockProperty.IntProp(name, max).setIntValue(value).setPair(pair);
	}

	public static McuBlockProperty get(Device.EPairNames pair, String name, int min, int max, int value) {
		return new McuBlockProperty.IntProp(name, min, max).setIntValue(value).setPair(pair);
	}

	public static McuBlockProperty get(Device.EPairNames pair, String name, String value) {
		return new McuBlockProperty.TextProp(name, value).setPair(pair);
	}

	public static McuBlockProperty get(Device.EPairNames pair, String name, String value, boolean ro) {
		return new McuBlockProperty.TextProp(name, value).setPair(pair).setRO(ro);
	}

	public static McuBlockProperty getF(Device.EPairNames pair, String name, List<String> divs, List<String> units) {
		return new McuBlockProperty.FreqDividor(name, divs, units).setPair(pair);
	}

	public static McuBlockProperty getL(Device.EPairNames pair, String name, List<String> values) {
		return new McuBlockProperty.ListProp(name, values).setPair(pair);
	}

	public static McuBlockProperty getC(Device.EPairNames pair, String name, List<String> values) {
		return new McuBlockProperty.ComboProp(name, values).setPair(pair);
	}
}
