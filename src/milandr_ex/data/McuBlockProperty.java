package milandr_ex.data;

import com.google.common.collect.Lists;
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

import java.util.List;

/**
 * Created by lizard on 01.03.17 at 12:37.
 */
public class McuBlockProperty {
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
	private String alias;
	private final String name;
	private String msgKey;
	private String msgTxt;
	private final PropKind kind;
	private List<McuBlockProperty> subProps;
	private ObservableList<String> subItems;
	private int intValue;
	private int intDefValue;
	private String strValue;
	private String strDefValue;
	private int minValue;
	private int maxValue;

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

	public McuBlockProperty setIntValue(int intValue) {
		this.intValue = intValue;
		return this;
	}

	public McuBlockProperty setIntDefValue(int intDefValue) {
		this.intDefValue = intDefValue;
		return this;
	}

	public McuBlockProperty setStrValue(String strValue) {
		this.strValue = strValue;
		return this;
	}

	public McuBlockProperty setStrDefValue(String strDefValue) {
		this.strDefValue = strDefValue;
		return this;
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
	public McuBlockProperty makeUI(Pane pane, int gridIndex) {
		Node node = null;
		switch (kind) {
			case CMP: for(McuBlockProperty prop: subProps) prop.makeUI(pane, gridIndex++); break;
			case CHK: node = new CheckBox(""); break;
			case STR: node = new TextField(strDefValue); break;
			case INT: node = new TextField(intDefValue + ""); break;
			case LST: node = new ListView(subItems); break;
			case CMB: node = new ComboBox(subItems); break;
		}
		if (node != null) {
			Label nodeLbl = new Label(getMsgTxt());
			pane.getChildren().add(nodeLbl);
			GridPane.setRowIndex(nodeLbl, gridIndex);
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
