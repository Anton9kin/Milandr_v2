package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

public class MCUGpioController extends MCUExtPairController {
	@FXML
	private GridPane gpio_gpio;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.GPIO);
	}

	@Override
	protected Parent getGPIOControl() {
		return gpio_gpio;
	}

	@Override
	public boolean filterGpio(String key, String item) {
		return key != null && key.startsWith("cb")
				&& item != null && !item.equals("RESET");
	}
}
