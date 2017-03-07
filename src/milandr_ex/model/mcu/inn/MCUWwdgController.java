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
	public List<String> generateCode(Device device, Device.EPairNames pairBlock,
									 PinoutsModel model, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		McuBlockModel blockModel = pairBlock.model();
		String freq = blockModel.getProp("f_div").getStrValue();
		Integer div = blockModel.getProp("freq_div").getProp("f_div").getIntValue();
		Integer hclk = blockModel.getProp("hclk").getIntValue();
		String cnt = blockModel.getProp("cnt_val").getStrValue();
		String win = blockModel.getProp("win_val").getStrValue();
		Integer wint = blockModel.getProp("early_int").getIntValue();
		log.debug(String.format("#generateWWDGCode(%s, %s, %s)", device, pairBlock, model));
		g().addCodeStr(oldCode,"void ");
		g().addCodeStr(oldCode,"WWDG_Init( ");
		g().addCodeStr(oldCode,"void ");
		g().addCodeStr(oldCode," ){\r\n");

		g().addCodeStr(oldCode,"    MDR_RST_CLK->PER_CLOCK |= ( 1 << 12 );");
		g().addCodeStr(oldCode,"//разрешение тактирование WWDG\r\n\r\n");

		g().addCodeStr(oldCode,"    MDR_WWDG->CR  = (( 1 << 7 ) ");
		g().addCodeStr(oldCode,"//сторожевой таймер включен\r\n");

		g().addCodeStr(oldCode,"                    | 0x" + Integer.toHexString(Integer.parseInt(cnt)) + "); ");
		g().addCodeStr(oldCode,"//значение счетчика\r\n");

		g().addCodeStr(oldCode,"    MDR_WWDG->CFR = (( " + wint + " << 9 ) ");
		g().addCodeStr(oldCode,"//ранее предупреждающее прерывание " + g().strWWDGINT[wint] + "\r\n");

		g().addCodeStr(oldCode,"                   | ( " + div + " << 7) ");
		g().addCodeStr(oldCode,"//частота = HCLK(" + (double)hclk/1000 + " kHz)/4096" + div + " = " + freq + "\r\n");

		g().addCodeStr(oldCode,"                   |   0x" + Integer.toHexString(Integer.parseInt(win)) + "); ");
		g().addCodeStr(oldCode,"//значение окна\r\n");

		g().addCodeStr(oldCode,"}");
		g().addCodeStr(oldCode,"//void WWDG_Init");
		return super.generateCode(device, pairBlock, model, oldCode);
	}
}
