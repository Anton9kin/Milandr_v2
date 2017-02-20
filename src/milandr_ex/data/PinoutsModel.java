package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import milandr_ex.model.ModelObserver;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by lizard2k1 on 18.02.2017.
 */
public class PinoutsModel {
	private String selectedBody;
	private Map<String, String> selectedPins = Maps.newHashMap();
	private boolean hasUnsavedChanges = Boolean.FALSE;
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

	public Map<String, String> getSelectedPins() {
		return selectedPins;
	}

	public boolean isHasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
		this.hasUnsavedChanges = hasUnsavedChanges;
	}

	public void setSelectedPin(String key, String value) {
		this.selectedPins.put(key, value);
		hasUnsavedChanges = true;
	}

	public void save(File file) {
		save(file, true);
	}
	public void save(File file, boolean override) {
		if (file == null || !override && file.exists()) return;
		List<String> toSave = Lists.newArrayList();
		toSave.add("body=" + selectedBody);
		for(String key: selectedPins.keySet()) {
			toSave.add(String.format("pin.%s=%s", key, selectedPins.get(key)));
		}
		Constants.saveTxtList(file, toSave, override);
		hasUnsavedChanges = false;
	}
	public static PinoutsModel load(File file) {
		if (file == null || !file.exists()) return null;
		List<String> strings = Constants.loadTxtStrings(file);
		if (strings == null || strings.isEmpty()) return null;
		PinoutsModel pinoutsModel = new PinoutsModel();
		for(String str: strings) {
			if (!str.contains("=")) continue;
			String[] props = str.split("=");
			if (props.length < 2) continue;
			if (props[0].equals("body")) pinoutsModel.setSelectedBody(props[1]);
			if (props[0].startsWith("pin.")) pinoutsModel.
					setSelectedPin(props[0].substring(4), props[1]);
		}
		pinoutsModel.setHasUnsavedChanges(false);
		return pinoutsModel;
	}
}
