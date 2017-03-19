package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import static milandr_ex.data.McuBlockProperty.div128List;
import static milandr_ex.data.McuBlockProperty.uartList;

public class MCUUartController extends MCUExtPairController {
	@FXML
	private GridPane uart_gpio;
	@FXML
	private GridPane uart_grid;

	@Override
	protected Pane getPropControl() {
		return uart_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.UART);
		addModelProps(new String[]{"-"}, true);
		addModelProps(new String[]{"pre_div", "speed"}, div128List, uartList);
		addModelProps(new String[]{"-", "word_len", "odd_chk", "stp_bit", "-", "r_speed", "error"}, "", "-SBB-SS");
//		addModelProps(new String[]{"-", "word_len", "odd_chk", "stp_bit", "-", "r_speed", "error"}, "-SBB-SS",
//				"", "", true, true, "", "", "");
	}

	@Override
	protected Parent getGPIOControl() {
		return uart_gpio;
	}
}
