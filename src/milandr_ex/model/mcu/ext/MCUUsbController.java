package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import static milandr_ex.data.McuBlockProperty.opUList;
import static milandr_ex.data.McuBlockProperty.typeStartList;

public class MCUUsbController extends MCUExtPairController {
	@FXML
	private GridPane usb_gpio;
	@FXML
	private GridPane usb_grid;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.USB);
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
	}

	@Override
	protected Parent getGPIOControl() { return usb_gpio; }

	@Override
	protected Pane getPropControl() { return usb_grid; }
}
