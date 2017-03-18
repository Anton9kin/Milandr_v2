package milandr_ex.model.mcu;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

/**
 * Main controller for additional features
 * Created by lizard on 18.03.17 at 22:08.
 */
public class MCUOtherSController extends BasicController {

	@FXML
	private GridPane othr_grid;

	@Override
	protected Pane getPropControl() {
		return othr_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.NON);
	}
}
