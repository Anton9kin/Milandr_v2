package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

import java.util.List;

/**
 * Created by lizard2k1 on 12.03.2017.
 */
public class MCUCompController extends BasicController {

	@FXML
	GridPane comp_grid;

	@Override
	protected Pane getPropControl() {
		return comp_grid;
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		return super.generateCode(device, oldCode);
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.COMP);
		addModelProps(new String[]{"inv_out", "intrp"}, "BB");
		addModelProps(new String[]{"bounds", "res_deps"}, (List)null, null);
	}
}
