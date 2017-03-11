package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
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
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateWDGCode(%s)", device));
		String freq = getConfPropStr("freq_div", "f_time");
		Integer div = getConfPropInt("freq_div", "f_div");
		Integer irlr = getWatchDogReloadReg();
		g().addCodeStr(oldCode,"#define IWDG_RST() MDR_IWDG->KR = 0xAAAA; //сброс IWDG");

		g().addCodeStr(oldCode,"void IWDG_Init( void ) {");

		g().addCodeStr(oldCode,"//разрешение тактирование IWDG");
		g().addCodeStr(oldCode,"MDR_RST_CLK->PER_CLOCK |= ( 1 << 13 );");

		g().addCodeStr(oldCode,"//ждем обновления частоты сторожевого таймера");
		g().addCodeStr(oldCode,"while ((MDR_IWDG->SR &= (1 << 0)) != 0);\n");
		g().addCodeStr(oldCode,"MDR_IWDG->KR =  0x5555; //разрешение записи в регистры PR и RLR");
		g().addCodeStr(oldCode,"MDR_IWDG->PR =  " + div + "; //частота IWDG = LSI(40kHz)" + div + " = " + freq+ "");
		g().addCodeStr(oldCode,"MDR_IWDG->RLR = 0x" + Integer.toHexString(irlr) + "; //значение перегрузки IWDG");
		g().addCodeStr(oldCode,"MDR_IWDG->KR =  0xCCCC; //запускаем IWDG");
		g().addCodeStr(oldCode,"}//void IWDG_Init");
		return super.generateCode(device, oldCode);
	}
}
