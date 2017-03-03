package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;
import static milandr_ex.data.McuBlockProperty.*;

public class MCUIwdgController extends BasicController {

	@FXML
	private GridPane iwdg_grid;

	@Override
	protected Pane getPropControl() {
		return iwdg_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.IWDG);
		getDevicePair().model().addModelProp(McuBlockProperty.getF("freq_div", div512List, unitList),
				"f_div.freq_div", "f_time.time_freq", "f_units.tf_units");
	}
}
