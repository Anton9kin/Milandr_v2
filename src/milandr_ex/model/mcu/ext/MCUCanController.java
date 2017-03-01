package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;

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
		getDevicePair().model().setBundle(getMessages());
		getDevicePair().model().addModelProp(McuBlockProperty.getC("pre_div", div128List));
		getDevicePair().model().addModelProp(McuBlockProperty.getC("speed", speedList));
		getDevicePair().model().addModelProp(McuBlockProperty.get("-", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("quants", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("seg1", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("seg2", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("pseg", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("-", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("r_speed", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.get("error", ""));
	}

	@Override
	protected Parent getGPIOControl() {
		return can_gpio;
	}
}
