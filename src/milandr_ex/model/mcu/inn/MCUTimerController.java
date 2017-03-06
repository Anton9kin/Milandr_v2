package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;

import static milandr_ex.data.McuBlockProperty.div128List;
import static milandr_ex.data.McuBlockProperty.div512List;
import static milandr_ex.data.McuBlockProperty.unitList;

public class MCUTimerController extends BasicController {

	@FXML
	private GridPane tmr_grid;

	@Override
	protected Pane getPropControl() {
		return tmr_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.TMR);
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "pre_div", div128List));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "tim_clk", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.getF(getDevicePair(), "freq_div", div512List, unitList),
				"f_div.main_div", "f_time.time_freq", "f_units.tf_units");
	}
}
