package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import static milandr_ex.data.McuBlockProperty.opUList;
import static milandr_ex.data.McuBlockProperty.typeStartList;

public class MCUI2CController extends MCUExtPairController {
	@FXML
	private GridPane i2c_gpio;
	@FXML
	private GridPane i2c_grid;

	@Override
	protected Pane getPropControl() {
		return i2c_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.I2C);
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
	}

	@Override
	protected Parent getGPIOControl() {
		return i2c_gpio;
	}
}
