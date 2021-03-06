package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;

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
		//noinspection unchecked
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "sw_chn", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "temp_sens", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "lst_chn", ""));
//		getDevicePair().model().addModelProp(McuBlockProperty.getF("freq"));
	}
}
