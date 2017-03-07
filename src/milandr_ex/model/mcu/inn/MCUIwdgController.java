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
	public List<String> generateCode(Device device, Device.EPairNames pairBlock,
									 PinoutsModel model, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		McuBlockModel blockModel = pairBlock.model();
		log.debug(String.format("#generateWDGCode(%s, %s, %s)", device, pairBlock, model));
		String freq = blockModel.getProp("freq_div").getProp("f_time").getStrValue();
		Integer div = blockModel.getProp("freq_div").getProp("f_div").getIntValue();
		Integer irlr = blockModel.getProp("freq_div").getProp("irlr").getIntValue();
		g().addCodeStr(oldCode,"#define ");
		g().addCodeStr(oldCode,"IWDG_RST() MDR_IWDG->KR = 0xAAAA; ");
		g().addCodeStr(oldCode,"//сброс IWDG\r\n\r\n");

		g().addCodeStr(oldCode,"void ");
		g().addCodeStr(oldCode,"IWDG_Init( ");
		g().addCodeStr(oldCode,"void ");
		g().addCodeStr(oldCode," ){\r\n");

		g().addCodeStr(oldCode,"    MDR_RST_CLK->PER_CLOCK |= ( 1 << 13 );");
		g().addCodeStr(oldCode,"//разрешение тактирование IWDG\r\n\r\n");

		g().addCodeStr(oldCode,"    while ");
		g().addCodeStr(oldCode,"((MDR_IWDG->SR &= (1 << 0)) != 0);");
		g().addCodeStr(oldCode,"//ждем обновления частоты сторожевого таймера\r\n\r\n");

		g().addCodeStr(oldCode,"    MDR_IWDG->KR =  0x5555; ");
		g().addCodeStr(oldCode,"//разрешение записи в регистры PR и RLR\r\n");

		g().addCodeStr(oldCode,"    MDR_IWDG->PR =  " + div + "; ");
		g().addCodeStr(oldCode,"//частота IWDG = LSI(40kHz)" + div + " = " + freq+ "\r\n");

		g().addCodeStr(oldCode,"    MDR_IWDG->RLR = 0x" + Integer.toHexString(irlr) + "; ");
		g().addCodeStr(oldCode,"//значение перегрузки IWDG\r\n");

		g().addCodeStr(oldCode,"    MDR_IWDG->KR =  0xCCCC; ");
		g().addCodeStr(oldCode,"//запускаем IWDG\r\n");

		g().addCodeStr(oldCode,"}");
		g().addCodeStr(oldCode,"//void IWDG_Init\r\n\r\n");
		return super.generateCode(device, pairBlock, model, oldCode);
	}
}
