package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.data.code.Command;
import milandr_ex.data.code.Module;
import milandr_ex.data.code.Param;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUIwdgController extends BasicController {

	@FXML
	private GridPane iwdg_grid;

	@Override
	protected Pane getPropControl() {
		return iwdg_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.IWDG);
		getDevicePair().model().addModelProp(McuBlockProperty.getF(getDevicePair(), "freq_div", div512List, unitList),
				"f_div.freq_div", "f_time.time_freq", "f_units.tf_units");
	}

	@Override
	protected int getBasicClockSrc(List<String> istList, String signSrc) {
		return getConfPropInt("freq_div.f_div") * getClockProp("LSI");
	}

	private int getWatchDogReloadReg() {
		return getBasicReloadReg("freq_div.f_time", "freq_div.f_units");
	}

	@Override
	protected List<String> generateDefines(Device device, List<String> oldCode) {
		oldCode.add("#define IWDG_RST() MDR_IWDG->KR = 0xAAAA; //сброс IWDG");
		return super.generateDefines(device, oldCode);
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		String freq = getConfPropStr("freq_div", "f_time");
		Integer div = getConfPropInt("freq_div", "f_div");
		Integer irlr = getWatchDogReloadReg();

		g().addCodeStr(oldCode,"//разрешение тактирование IWDG");
		g().addCodeStr(oldCode,"MDR_RST_CLK->PER_CLOCK |= ( 1 << 13 );");

		g().addCodeStr(oldCode,"//ждем обновления частоты сторожевого таймера");
		g().addCodeStr(oldCode,"while ((MDR_IWDG->SR &= (1 << 0)) != 0);\n");
		g().addCodeStr(oldCode,"MDR_IWDG->KR =  0x5555; //разрешение записи в регистры PR и RLR");
		g().addCodeStr(oldCode,"MDR_IWDG->PR =  " + div + "; //частота IWDG = LSI(40kHz)" + div + " = " + freq+ "");
		g().addCodeStr(oldCode,"MDR_IWDG->RLR = 0x" + Integer.toHexString(irlr) + "; //значение перегрузки IWDG");
		g().addCodeStr(oldCode,"MDR_IWDG->KR =  0xCCCC; //запускаем IWDG");
		return oldCode;
	}

	private String[] comments = {
			"разрешение тактирование IWDG",
			"ждем обновления частоты сторожевого таймера",
			"разрешение записи в регистры PR и RLR",
			"частота IWDG = LSI(40kHz) %s = %s",
			"значение перегрузки IWDG",
			"запускаем IWDG",
	};
	private Module MDR_RST_CLK = Module.MDR_RST_CLK.get();
	private Module MDR_IWDG = Module.MDR_IWDG.get();

	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		String freq = getConfPropStr("freq_div", "f_time");
		Integer div = getConfPropInt("freq_div", "f_div");
		String irlr = "0x" + Integer.toHexString(getWatchDogReloadReg());

		MDR_RST_CLK.get().arr(comments);
		MDR_RST_CLK.set(Param.PER_CLOCK.seti(1, 13, "|")).cmt(0).build(oldCode);

		MDR_IWDG.get().arr(comments);
		MDR_IWDG.set(Command.WHILE.set(Param.SR, "1 << 0", " != 0")).cmt(1).build(oldCode);
		MDR_IWDG.set(Param.KR.set("0x5555")).cmt(2).build(oldCode);
		MDR_IWDG.set(Param.PR.set(div)).cmta(3, div, freq).build(oldCode);
		MDR_IWDG.set(Param.RLR.set(irlr)).cmt(4).build(oldCode);
		MDR_IWDG.set(Param.KR.set("0xCCCC")).cmt(5).build(oldCode);
		return oldCode;
	}
}
