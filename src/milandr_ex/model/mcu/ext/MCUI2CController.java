package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

public class MCUI2CController extends MCUExtPairController {
	@FXML
	private GridPane i2c_gpio;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.I2C);
	}

	@Override
	protected Parent getGPIOControl() {
		return i2c_gpio;
	}
}
