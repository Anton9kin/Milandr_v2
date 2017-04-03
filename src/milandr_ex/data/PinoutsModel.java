package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import milandr_ex.McuType;
import milandr_ex.model.ModelObserver;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lizard2k1 on 18.02.2017.
 */
public class PinoutsModel {
	private String selectedBody;
	private Map<String, String> selectedPins = Maps.newHashMap();
	private Map<String, String> selectedProps = Maps.newHashMap();
	private Map<String, String> clockProps = Maps.newHashMap();
	private Map<Device.EPairNames, McuBlockModel> mcuBlocks = Maps.newHashMap();
	private boolean hasUnsavedChanges = Boolean.FALSE;
	private ClockModel clockModel;
	private String lastSelectedPin = "";
	private String lastSelPinValue = "";
	public interface Observer extends ModelObserver {
		public void observe(PinoutsModel model);
	}

	public String getSelectedBody() {
		return selectedBody;
	}

	public PinoutsModel setSelectedBody(String selectedBody) {
		this.selectedBody = selectedBody;
		return this;
	}

	public ClockModel getClockModel() {
		return clockModel;
	}

	public PinoutsModel setClockModel(ClockModel clockModel) {
		this.clockModel = clockModel;
		return this;
	}

	public McuBlockModel getBlockModel(String name) {
		return mcuBlocks.get(getBlockKey(name));
	}
	public Collection<McuBlockModel> getBlockModels() {
		return mcuBlocks.values();
	}

	public PinoutsModel setBlockModel(McuBlockModel mcuBlockModel) {
		this.mcuBlocks.put(mcuBlockModel.getPair(), mcuBlockModel);
		return this;
	}

	public List<String> getBlockCode(String name) {
		if (getBlockModel(name) == null) return null;
		return getBlockModel(name).getCodeList();
	}

	public PinoutsModel setBlockCode(String name, List<String> codeList) {
		getBlockModel(name).setCodeList(codeList);
		return this;
	}

	private Device.EPairNames getBlockKey(String name) {
		return Device.EPairNames.valueOf(name.toUpperCase());
	}

	public PinoutsModel clearSelected() {
		getSelectedPins().clear();
		getSelectedProps().clear();
		return this;
	}
	public Map<String, String> getSelectedPins() {
		return selectedPins;
	}
	public Map<String, String> getSelectedProps() {
		return selectedProps;
	}

	public Map<String, String> getClockProps() {
		return clockProps;
	}

	public boolean isHasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
		this.hasUnsavedChanges = hasUnsavedChanges;
	}

	public void setSelectedPin(String key, String value) {
		this.selectedPins.put(key, value);
		lastSelectedPin = key;
		lastSelPinValue = value;
		hasUnsavedChanges = true;
	}

	public void setClockProp(String key, String value) {
		this.clockProps.put(key, value);
		hasUnsavedChanges = true;
	}

	public void setSelectedProp(String key, String value) {
		this.selectedProps.put(key, value);
		hasUnsavedChanges = true;
	}

	public String getLastSelectedPin() {
		return lastSelectedPin;
	}
	public String getLastSelectedValue() {
		return lastSelectedPin;
	}

	public void save(File file) {
		save(file, true);
	}
	public void save(File file, boolean override) {
		if (file == null || !override && file.exists()) return;
		List<String> toSave = Lists.newArrayList();
		toSave.add("body=" + selectedBody);
		for(String key: selectedPins.keySet()) {
			String value = selectedPins.get(key);
			if (value.equals("RESET")) continue;
			toSave.add(String.format("pin.%s=%s", key, value));
		}
		clockModel.save(toSave);
		for(McuBlockModel blockModel: mcuBlocks.values()) blockModel.save(toSave);
		Constants.saveTxtList(file, toSave, override);
		hasUnsavedChanges = false;
	}
	public static PinoutsModel load(File file) {
		if (file == null || !file.exists()) return null;
		List<String> strings = Constants.loadTxtStrings(file);
		if (strings == null || strings.isEmpty()) return null;
		PinoutsModel pinoutsModel = new PinoutsModel();
		loadModelParams(strings, pinoutsModel);
		loadSelectedPins(strings, pinoutsModel);
		pinoutsModel.setHasUnsavedChanges(false);
		return pinoutsModel;
	}

	private static void loadModelParams(List<String> strings, PinoutsModel pinoutsModel) {
		for(McuBlockModel blockModel: pinoutsModel.getBlockModels()) blockModel.load(strings);
	}

	public void loadClockParams() {
		if (getClockModel() == null) return;
		List<String> strings = Lists.newArrayList();
		for(String key: clockProps.keySet()) {
			strings.add(String.format("clk.%s=%s", key, clockProps.get(key)));
		}
		getClockModel().load(strings);
	}

	private static void loadSelectedPins(List<String> strings, PinoutsModel pinoutsModel) {
		for(String str: strings) {
			if (!str.contains("=")) continue;
			String[] props = str.split("=");
			if (props.length < 2) continue;
			if (props[0].equals("body")) pinoutsModel.setSelectedBody(props[1]);
			if (props[0].startsWith("pin.")) pinoutsModel.
					setSelectedPin(props[0].substring(4), props[1]);
			if (props[0].startsWith("mbm.")) pinoutsModel.
					setSelectedProp(props[0].substring(4), props[1]);
			if (props[0].startsWith("clk.")) pinoutsModel.
					setClockProp(props[0].substring(4), props[1]);
		}
	}

	@Override
	public String toString() {
		return "PinModel{" +
				"selBody='" + selectedBody + '\'' +
				", selPins=" + selectedPins +
				", selProps=" + selectedProps +
				", clkProps=" + clockProps +
				", mcuBlocks=" + mcuBlocks +
				", hasUnsavedChanges=" + hasUnsavedChanges +
				", clockModel=" + clockModel +
				'}';
	}

	public String toStr() {
		return "PINMdl{" +
				"selectedBody='" + selectedBody + '\'' +
//				", mcuBlocks=" + mcuBlocks +
				", hasUnsavedChanges=" + hasUnsavedChanges +
//				", clockModel=" + clockModel.toStr("out") +
				'}';
	}

	public static PinoutsModel get(McuType mcuType) {
		return get(mcuType.getProp("pack"));
	}

	public static PinoutsModel get(String body) {
		return new PinoutsModel().setSelectedBody(body);//.setClockModel(ClockModel.get(body));
	}
}
