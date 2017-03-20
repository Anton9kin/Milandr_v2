package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

import static milandr_ex.data.McuBlockProperty.opUList;
import static milandr_ex.data.McuBlockProperty.typeStartList;

public class MCUBkpController extends BasicController {

	@FXML
	private GridPane bkp_grid;

	@Override
	protected Pane getPropControl() {
		return bkp_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.BKP);
		//noinspection unchecked
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
		addModelProps(new String[]{"sw_chn", "temp_sens", "lst_chn"}, "","BBS");
	}

	@Override
	protected void checkSelectedPin(String comboKey, String value) {
		super.checkSelectedPin(comboKey, value);
		if (comboKey.startsWith(getDevicePair().name())) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			setModelProp("lst_chn", Integer.parseInt(ind) - 1, value);
		}
	}
}
