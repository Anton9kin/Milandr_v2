package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUAdcController extends BasicController {

	@FXML
	private GridPane adc_grid;

	@Override
	protected Pane getPropControl() {
		return adc_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.ADC);
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "sw_chn", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "temp_sens", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "lst_chn", ""));
	}

	@Override
	public List<String> generateCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s, %s, %s)", device, pairBlock, model));
		return super.generateCode(device, pairBlock, model, oldCode);
	}
}
