package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

import static milandr_ex.data.McuBlockProperty.*;

import java.util.List;

public class MCUPowerController extends BasicController {

	@FXML
	private GridPane pwr_grid;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.PWR);
		addModelProps(new String[]{"bp_ucc", "bp_bucc"}, uccList, buccList);
	}

	@Override
	protected Pane getPropControl() {
		return pwr_grid;
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		int ucc = getConfPropInt("bp_ucc");
		int bucc = getConfPropInt("bp_bucc");
		oldCode = Lists.newArrayList();

		g().addCodeStr(oldCode, "void Power_Init( void ){");

		g().addCodeStr(oldCode, "//разрешение тактирования Power");
		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK |= 1 << 11;");

		g().addCodeStr(oldCode, " //сравнение с Ucc ( " + uccList.get(ucc) + " )");
		g().addCodeStr(oldCode, "    MDR_POWER->PVDCS = ((" + ucc + " << 3)");

		g().addCodeStr(oldCode, " //равнение с BUcc ( " + buccList.get(bucc) + " ));");
		g().addCodeStr(oldCode, "                     | (" + bucc + " << 1);");

		g().addCodeStr(oldCode, "} //void Power_Init");
		return super.generateCode(device, oldCode);
	}
}
