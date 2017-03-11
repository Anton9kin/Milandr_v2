package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUWwdgController extends BasicController {

	@FXML
	private GridPane wwdg_grid;

	@Override
	protected Pane getPropControl() {
		return wwdg_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.WWDG);
		getDevicePair().model().addModelProp(McuBlockProperty.getF(getDevicePair(), "freq_div", div8List, null), "f_div.freq_div");
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "cnt_val", 1));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "win_val", 1));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "early_int", true));
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateWWDGCode(%s) preparing", device));
		Integer hclk = getClockProp("hclk");

		String freq = getConfPropStr("freq_div.f_div");
		Integer div = getConfPropInt("freq_div.f_div");
		String cnt = getConfPropStr("cnt_val");
		String win = getConfPropStr("win_val");
		Integer wint = getConfPropInt("early_int");

		log.debug(String.format("#generateWWDGCode(%s) %s, %d, %d, %s, %s, %d", device,
				freq, div, hclk, cnt, win, wint));

		g().addCodeStr(oldCode,"void  WWDG_Init( void ){");
		g().addCodeStr(oldCode,"//разрешение тактирование WWDG");
		g().addCodeStr(oldCode,"MDR_RST_CLK->PER_CLOCK |= ( 1 << 12 );");
		g().addCodeStr(oldCode,"MDR_WWDG->CR  = (( 1 << 7 ) //сторожевой таймер включен");
		g().addCodeStr(oldCode,"| 0x" + Integer.toHexString(Integer.parseInt(cnt)) + "); //значение счетчика");
		g().addCodeStr(oldCode,"MDR_WWDG->CFR = (( " + wint + " << 9 ) //ранее предупреждающее прерывание " + g().strWWDGINT[wint] + "");
		g().addCodeStr(oldCode,"| ( " + div + " << 7) //частота = HCLK(" + (double)hclk/1000 + " kHz)/4096" + div + " = " + freq + "");
		g().addCodeStr(oldCode,"|   0x" + Integer.toHexString(Integer.parseInt(win)) + "); //значение окна");
		g().addCodeStr(oldCode,"}//void WWDG_Init");
		return super.generateCode(device, oldCode);
	}
}
