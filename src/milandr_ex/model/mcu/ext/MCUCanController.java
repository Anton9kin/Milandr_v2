package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUCanController extends MCUExtPairController {

	@FXML
	private GridPane can_gpio;

	@FXML
	private GridPane can_grid;

	@Override
	protected Pane getPropControl() {
		return can_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.CAN);
		//noinspection unchecked
		addModelProps(new String[]{"pre_div", "speed"}, div128List, speedList);
		addModelProps("-", "quants", "seg1", "seg2", "pseg", "-", "r_speed", "error");
	}

	@Override
	protected Parent getGPIOControl() {
		return can_gpio;
	}
}
