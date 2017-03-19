package milandr_ex.model.mcu.ext;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUUsbController extends MCUExtPairController {
	@FXML
	private GridPane usb_gpio;
	@FXML
	private GridPane usb_grid;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.USB);
		//noinspection unchecked
		addModelProps(new String[]{"usb_mode", "usb_len", "usb_sph", "usb_spo"},
				usbModeList, usbSpeedList, usbPolarList, usbPushPullList);
		addModelProps(new String[]{"usb_txd"}, "", "B", true);
	}

	@Override
	protected Parent getGPIOControl() { return usb_gpio; }

	@Override
	protected Pane getPropControl() { return usb_grid; }

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		return super.generateCode(device, oldCode);
	}
}
