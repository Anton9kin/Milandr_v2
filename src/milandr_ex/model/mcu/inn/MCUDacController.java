package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.data.PinoutsModel;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUDacController extends BasicController {
	@FXML
	private GridPane dac_grid;

	@Override
	protected Pane getPropControl() {
		return dac_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.DAC);
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "sw_chn", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "temp_sens", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "lst_chn", ""));
//		getDevicePair().model().addModelProp(McuBlockProperty.getF("freq"));
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateDACCode(%s)", device));
		return super.generateCode(device, oldCode);
	}
}
