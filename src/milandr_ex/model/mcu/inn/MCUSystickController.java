package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;
import static milandr_ex.data.McuBlockProperty.*;

public class MCUSystickController extends BasicController {

	@FXML
	private GridPane syst_grid;


	@Override
	protected Pane getPropControl() {
		return syst_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.SYST);
		getDevicePair().model().addModelProp(McuBlockProperty.getC("sign_src", istList));
		getDevicePair().model().addModelProp(McuBlockProperty.get("intrp", true));
		getDevicePair().model().addModelProp(McuBlockProperty.getC("wrk_kind", modeList));
		getDevicePair().model().addModelProp(McuBlockProperty.get("time_freq", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.getC("tf_units", unitList));
	}
}
