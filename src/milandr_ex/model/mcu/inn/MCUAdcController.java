package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUAdcController extends BasicController {

	@FXML
	private GridPane adc_grid;

	@Override
	protected Pane getPropControl() {
		return adc_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.ADC);
		//noinspection unchecked
		addModelProps(new String[]{"base_power", "start_kind"}, opUList, typeStartList);
		addModelProps(new String[]{"sw_chn", "temp_sens", "lst_chn"}, "BBS");
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s)", device));
		g().addCodeStr(oldCode, "void  ADC_init( void ){");
		//, Color\.\w+,\sFontStyle\.\w+,\s\w+

		int adcC1 = getClockProp("ADC-C1.S");
		int adcC2 = getClockProp("ADC-C2.S");
		int adcDiv = getClockProp("ADC-C2-4.S");
		int NUMCH = 0; int IST = 1; int SAMPLE = 2;
		int MREF = 3; int DIV_CLK = 4;
		if (IST == 2)
		{
			g().addCodeStr(oldCode, "    MDR_RST_CLK->ADC_MCO_CLOCK = ((" + adcC1 + " << 0)");
			g().addCodeStr(oldCode, "/*источник для ADC_C1*/");
			g().addCodeStr(oldCode, "                                | (" + adcC2 + " << 4)");
			g().addCodeStr(oldCode, "/*источник для ADC_C2*/");
			g().addCodeStr(oldCode, "                                | (" + adcDiv + " << 8)");
			g().addCodeStr(oldCode, "/*делитель для ADC_C3*/");
			g().addCodeStr(oldCode, "                                | (1 << 13));");
			g().addCodeStr(oldCode, "/*разрешение тактовой частоты ADC_CLK*/");
		}

		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK |= (1 << 17); //тактирование АЦП");

                /* 3 строка */
		g().addCodeStr(oldCode, "    MDR_PORTD->OE &=~ (1 << " + NUMCH + "); //вход");

                /* 4 строка */
		g().addCodeStr(oldCode, "    MDR_PORTD->ANALOG &=~ (1 << " + NUMCH + "); //аналоговый");

		g().addCodeStr(oldCode, "/*начало преобразования*/");
		if (adcChecked[0]) {
			g().addCodeStr(oldCode, "    MDR_ADC->ADC1_CFG = ( 1 ");
		}
		if (adcChecked[1]) {
			g().addCodeStr(oldCode, "    MDR_ADC->ADC2_CFG = ( 1 ");
		}

		g().addCodeStr(oldCode, "/*источник синхросигнала*/");
		g().addCodeStr(oldCode, "                        |(" + (IST - 1) + " << 2) ");

		g().addCodeStr(oldCode, "/*выбор запуска*/");
		g().addCodeStr(oldCode, "                        |(" + SAMPLE + " << 3) ");

		g().addCodeStr(oldCode, "/*номер канала преобразования*/");
		g().addCodeStr(oldCode, "                        |(" + NUMCH + " << 4) ");

		g().addCodeStr(oldCode, "/*источник опорного*/");
		g().addCodeStr(oldCode, "                        |(" + MREF + " << 11) ");

		g().addCodeStr(oldCode, "/*коэффициент деления частоты*/");
		g().addCodeStr(oldCode, "                        |(" + DIV_CLK + " << 12)); ");
		g().addCodeStr(oldCode,"} //void ADC_init");
		return super.generateCode(device, oldCode);
	}

	private boolean[] adcChecked = {false, false};
	@Override
	protected void checkSelectedPin(String comboKey, String value) {
		super.checkSelectedPin(comboKey, value);
		if (comboKey.matches(getDevicePair().name() + "-\\d")) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			setModelProp("lst_chn", Integer.parseInt(ind) - 1, value);
		}
		if (comboKey.matches("c-" + getDevicePair().name() + "-\\d")) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			adcChecked[Integer.parseInt(ind) - 1] = Boolean.parseBoolean(value);
		}
	}
}
