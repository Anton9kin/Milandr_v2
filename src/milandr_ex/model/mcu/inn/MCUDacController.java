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
		getDevicePair().model().setBundle(getMessages());
		getDevicePair().model().addModelProp(McuBlockProperty.getC("base_power", opUList.sorted()));
		getDevicePair().model().addModelProp(McuBlockProperty.getC("start_kind", typeStartList.sorted()));
		getDevicePair().model().addModelProp(McuBlockProperty.get("sw_chn", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get("temp_sens", true));
		getDevicePair().model().addModelProp(McuBlockProperty.get("lst_chn", ""));
//		getDevicePair().model().addModelProp(McuBlockProperty.getF("freq"));
	}
}
