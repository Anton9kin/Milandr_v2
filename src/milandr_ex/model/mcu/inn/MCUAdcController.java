package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
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
		addModelProps(new String[]{"sw_chn", "temp_sens", "lst_chn"}, "BBS");
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s)", device));
		return super.generateCode(device, oldCode);
	}

	@Override
	protected void checkSelectedPin(String comboKey, String value) {
		super.checkSelectedPin(comboKey, value);
		if (comboKey.startsWith(getDevicePair().name())) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			setModelProp("lst_chn", Integer.parseInt(ind), value);
		}
	}
}
