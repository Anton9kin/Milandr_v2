package milandr_ex.model.mcu.ext;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

public class MCUUartController extends MCUExtPairController {
	@FXML
	private GridPane uart_gpio;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.UART);
	}

	@Override
	protected Parent getGPIOControl() {
		return uart_gpio;
	}
}
