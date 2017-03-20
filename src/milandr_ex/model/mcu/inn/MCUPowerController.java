package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.code.Module;
import milandr_ex.data.code.Param;
import milandr_ex.model.BasicController;

import static milandr_ex.data.McuBlockProperty.*;

import java.util.List;

public class MCUPowerController extends BasicController {

	@FXML
	private GridPane pwr_grid;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.PWR);
		//noinspection unchecked
		addModelProps(new String[]{"bp_ucc", "bp_bucc"}, uccList, buccList);
	}

	@Override
	protected Pane getPropControl() {
		return pwr_grid;
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int ucc = getConfPropInt("bp_ucc");
		int bucc = getConfPropInt("bp_bucc");

		g().addCodeStr(oldCode, "//разрешение тактирования Power");
		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK |= ((1 << 11));");

		g().addCodeStr(oldCode, " //сравнение с Ucc ( " + uccList.get(ucc) + " )");
		g().addCodeStr(oldCode, "    MDR_POWER->PVDCS = ((" + ucc + " << 3)");

		g().addCodeStr(oldCode, " //равнение с BUcc ( " + buccList.get(bucc) + " ));");
		g().addCodeStr(oldCode, "                     | (" + bucc + " << 1));");

		return oldCode;
	}

	private Module MDR_RST_CLK = Module.MDR_RST_CLK.get();
	private Module MDR_POWER = Module.MDR_POWER.get();
	private static String[] comments = {
			"сравнение с Ucc ( %s )",
			"сравнение с BUcc ( %s )",
	};
	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		int ucc = getConfPropInt("bp_ucc");
		int bucc = getConfPropInt("bp_bucc");

		MDR_RST_CLK.get();
		MDR_RST_CLK.set(Param.PER_CLOCK.seti(1, 11, "|")).cmt("тактирование Power").build(oldCode);
		MDR_POWER.get().arr(comments);
		MDR_POWER.set(Param.PVDCS.set(ucc, bucc).shift(3, 1)
			).cmta(0, uccList.get(ucc)).cmta(1, buccList.get(bucc)).build(oldCode);
		return oldCode;
	}
}
