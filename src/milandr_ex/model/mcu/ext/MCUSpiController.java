package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

public class MCUSpiController extends MCUExtPairController {
	@FXML
	private GridPane spi_gpio;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.SPI);
	}

	@Override
	protected Parent getGPIOControl() {
		return spi_gpio;
	}
}
