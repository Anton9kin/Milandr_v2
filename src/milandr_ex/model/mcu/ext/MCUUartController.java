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
	
	@FXML
	private ComboBox<String> div;
	ObservableList<String> divList = FXCollections.
			observableArrayList("/1", "/2", "/4", "/8", "/16", "/32", "/64", "/128");
	
	@FXML
	private ComboBox<String> speed;
	ObservableList<String> speedList = FXCollections.
			observableArrayList("2 400 бит/с", "4 800 бит/с", "9 600 бит/с",
					"14 400 бит/с","19 200 бит/с","28 800 бит/с","38 400 бит/с",
					"57 600 бит/с","76 800 бит/с","115 200 бит/с","230 400 бит/с",
					"460 800 бит/с","921 600 бит/с");

	@FXML
	private TextField uart_clk;
	
	@FXML
	private TextField realSpeed;
	@FXML
	private TextField errSpeed;
	
	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.UART);
		div.setItems(divList);
		speed.setItems(speedList);
	}

	@Override
	protected Parent getGPIOControl() {
		return uart_gpio;
	}
}
