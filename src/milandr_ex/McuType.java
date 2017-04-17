package milandr_ex;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import milandr_ex.data.PropsFactory;
import milandr_ex.utils.guava.Maps;

import java.util.Map;

public class McuType {

	private final Map<String, StringProperty> props = Maps.newHashMap();

	/**
	 * Default constructor
	 */
	public McuType(){
		this(null,null,null,null,null);
	}
	
	public McuType(String type, String pack){
		this(type,pack,null,null,null);
	}

	/**
	 * Constructor with some initial data
	 */
	public McuType(String type, String pack, String flash,
                   String ram, String io){
		initProps();
		setProp("type", type);
		setProp("pack", pack);
		setProp("flash", flash);
		setProp("ram", ram);
		setProp("io", io);
	}
	
	private StringProperty newSimpleStringProperty(String name) {
		return newSimpleStringProperty(name, name);
	}
	private StringProperty newSimpleStringProperty(String name, String value) {
		setProp(name, value);
		return props.get(name);
	}
	public StringProperty getSProp(String name) {
		return props.get(name);
	}
	public String getProp(String name) {
		if (!props.containsKey(name)) return "null";
		return props.get(name).getValue();
//		return Constants.encodeStr(props.get(name).getValue());
	}

	public McuType setProp(String name, String value) {
		if (!props.containsKey(name)) {
			props.put(name, new SimpleStringProperty(value));
		}
		props.get(name).setValue(value);
		return this;
	}

	private McuType initProps() {
		for(PropsFactory.Basic prop: PropsFactory.Basic.values()) {
			newSimpleStringProperty(prop.name());
		}
		return this;
	}
}
