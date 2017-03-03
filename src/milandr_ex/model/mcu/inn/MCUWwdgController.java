package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;
import static milandr_ex.data.McuBlockProperty.*;

public class MCUWwdgController extends BasicController {

	@FXML
	private GridPane wwdg_grid;

	@Override
	protected Pane getPropControl() {
		return wwdg_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.WWDG);
		getDevicePair().model().addModelProp(McuBlockProperty.getF("freq_div", div8List, null), "f_div.freq_div");
		getDevicePair().model().addModelProp(McuBlockProperty.get("cnt_val", 1));
		getDevicePair().model().addModelProp(McuBlockProperty.get("win_val", 1));
		getDevicePair().model().addModelProp(McuBlockProperty.get("early_int", true));
	}
}
