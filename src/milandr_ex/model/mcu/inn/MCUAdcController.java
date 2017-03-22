package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.data.code.Module;
import milandr_ex.data.code.Param;
import milandr_ex.model.BasicController;

import java.util.Arrays;
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
		addModelProps(new String[]{"base_power", "start_kind", "adc_src", "adc_div"},
				opUList, typeStartList, adcSrcList, div2048List);
		addModelProps(new String[]{"sw_chn", "temp_sens"}, "","BB");
		addModelProps(new String[]{"lst_chn"}, true);
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int NUMCH = 0;

		switch (codeStep) {
			case 0:
				int adcC1 = getClockProp("ADC-C1.S");
				int adcC2 = getClockProp("ADC-C2.S");
				int adcDiv = getClockProp("ADC-C2-4.S");
				if (adcDiv > 0) adcDiv += 7;

				g().addCodeStr(oldCode, " MDR_RST_CLK->ADC_MCO_CLOCK = ((" + adcC1 + " << 0)");
				g().addCodeStr(oldCode, "/*источник для ADC_C1*/");
				g().addCodeStr(oldCode, " | (" + adcC2 + " << 4)");
				g().addCodeStr(oldCode, "/*источник для ADC_C2*/");
				g().addCodeStr(oldCode, " | (" + adcDiv + " << 8)");
				g().addCodeStr(oldCode, "/*делитель для ADC_C3*/");
				g().addCodeStr(oldCode, " | (1 << 13));");
				g().addCodeStr(oldCode, "/*разрешение тактовой частоты ADC_CLK*/");
				break;
			case 1:
				g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (1 << 17); //тактирование АЦП");

                /* 3 строка */
				g().addCodeStr(oldCode, "MDR_PORTD->OE &=~ (1 << " + NUMCH + "); //вход");

                /* 4 строка */
				g().addCodeStr(oldCode, "MDR_PORTD->ANALOG &=~ (1 << " + NUMCH + "); //аналоговый");
				break;
			case 2:
			case 3:
				int adcIndex = codeStep - 2;
				int adcSrcDiv = getConfPropInt("adc_src", adcIndex) == 0 ? 0 :
						getConfPropInt("adc_div", adcIndex);
				int syncSrc = getClockProp("ADC-C1.S") % 2 == 1 ? 1 : 2;
				int mref = getConfPropInt("base_power", adcIndex);
				int sample = getConfPropInt("start_kind", adcIndex);

				g().addCodeStr(oldCode, "/*начало преобразования*/");
				g().addCodeStr(oldCode, "MDR_ADC->ADC" + (adcIndex)+"_CFG = ( 1 ");
				g().addCodeStr(oldCode, "/*источник синхросигнала*/");
				g().addCodeStr(oldCode, "|(" + (syncSrc - 1) + " << 2) ");

				g().addCodeStr(oldCode, "/*выбор запуска*/");
				g().addCodeStr(oldCode, "|(" + sample + " << 3) ");

				g().addCodeStr(oldCode, "/*номер канала преобразования*/");
				g().addCodeStr(oldCode, "|(" + NUMCH + " << 4) ");

				g().addCodeStr(oldCode, "/*источник опорного*/");
				g().addCodeStr(oldCode, "|(" + mref + " << 11) ");

				g().addCodeStr(oldCode, "/*коэффициент деления частоты*/");
				g().addCodeStr(oldCode, "|(" + adcSrcDiv + " << 12)); ");
				break;
			case 4:
				if (getConfPropInt("temp_sens") <= 0) break;
                    /* 1 строка */
				g().addCodeStr(oldCode, "void TS_init( void ){");



				g().addCodeStr(oldCode, "/* выбор для оцифровки датчика температуры */");
				g().addCodeStr(oldCode, "    MDR_ADC->ADC1_CFG ");
				if (isCboxChecked(0))
					g().addCodeStr(oldCode, "|= ");
				else
					g().addCodeStr(oldCode, "= ");

				g().addCodeStr(oldCode, "/*включение вых. усилителя*/");
				g().addCodeStr(oldCode, " ((1 << 19) ");
				g().addCodeStr(oldCode, "                          |(1 << 18) ");
				g().addCodeStr(oldCode, "/*включение вых. усилителя*/");
				g().addCodeStr(oldCode, "                          |(1 << 17) ");

				if (getConfPropInt("sw_chn") > 0)
				{
					g().addCodeStr(oldCode, "/*включено переключение каналов*/");
					g().addCodeStr(oldCode, "                          |(1 << 9)); ");
					g().addCodeStr(oldCode, "/*выбор каналов для переключения*/");
					g().addCodeStr(oldCode, "    MDR_ADC->ADC1_CHSEL = (( 1 << 31) | ( 1 << " + NUMCH + "));");
				}
				else
				{
					g().addCodeStr(oldCode, "/*номер канала преобразования*/");
					g().addCodeStr(oldCode, "                          |(31 << 4)); ");
				}

                    /* 11 строка */
				g().addCodeStr(oldCode, "//void TS_init");
				break;
		}
		return oldCode;
	}

	private Module MDR_RST_CLK = Module.MDR_RST_CLK.get();
	private Module MDR_PORTD = Module.MDR_PORTD.get();
	private Module MDR_ADC = Module.MDR_ADC.get();
	private static String[] comments = {
			"начало преобразования %s канала",
			"источник синхросигнала",
			"выбор запуска",
			"номер канала преобразования",
			"переключение каналов",
			"источник опорного",
			"коэффициент деления частоты",
	};

	@Override
	protected List<String> generateDefines(Device device, List<String> oldCode) {
		if (getConfPropInt("temp_sens") <= 0) return oldCode;
		g().addCodeStr(oldCode, "// ADC value = 1700 @ 25C = 1.36996V - from milandr demo project");
		g().addCodeStr(oldCode, "#define FACTORY_ADC_TEMP25     1700         ");

		g().addCodeStr(oldCode, "// 1.38393 @ 26C. 1.34-1.52, 1.43 V typical @ factory delta_calib");
		g().addCodeStr(oldCode, "#define FACTORY_VTEMP25        1.36996      ");

		g().addCodeStr(oldCode, "// ADC delta value @ 1C, from milandr demo project");
		g().addCodeStr(oldCode, "#define FACTORY_ADC_AVG_SLOPE  6            ");

		g().addCodeStr(oldCode, "// 4.0-4.6, 4.3 mV/C typical @ factory delta_calib");
		g().addCodeStr(oldCode, "#define FACTORY_AVG_SLOPE      0.004835     ");

		g().addCodeStr(oldCode, "// расчёт в int");
		g().addCodeStr(oldCode, "temperature_C = (adc_value - FACTORY_ADC_TEMP25)/FACTORY_ADC_AVG_SLOPE + FACTORY_TEMP25;");

		g().addCodeStr(oldCode, "// расчёт в float");
		g().addCodeStr(oldCode, "temperature_C = ((Vtemp - Vtemp25) / Avg_Slope) + FACTORY_TEMP25;");
		return super.generateDefines(device, oldCode);
	}

	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		// xtodo implement using each selected adc channel
		MDR_RST_CLK.get().set(oldCode);
		MDR_PORTD.get().set(oldCode);

		switch (codeStep) {
			case 0:
				int adcC1 = getClockProp("ADC-C1.S");
				int adcC2 = getClockProp("ADC-C2.S");
				int adcDiv = getClockProp("ADC-C2-O.S");

				int adcCLKSrc = isCboxChecked(0) ? getConfPropInt("adc_src", 0) : 1;
				adcCLKSrc += isCboxChecked(1) ? getConfPropInt("adc_src", 1) : 1;
				
				if (adcCLKSrc != 2){
					MDR_RST_CLK.set(
							Param.ADC_MCO_CLOCK.set(adcC1, adcC2, adcDiv, 1).shift(0, 4, 8, 13)
							).pre(1, 1, 2, 4).cmt("ADC_C1", "ADC_C2", "ADC_C3", "ADC_CLK").build();
				}
				break;
			case 1:
				MDR_RST_CLK.set(Param.PER_CLOCK.seti(1, 17, "|")).cmt("тактирование АЦП").build();

				String chnlLst = getConfPropStr("lst_chn", 0) + getConfPropStr("lst_chn", 1);
				chnlLst = cleanChannelsList(chnlLst);
				Integer[] vals = new Integer[chnlLst.length()];
				Integer[] ofst = new Integer[chnlLst.length()];
				Arrays.fill(vals, 1);
				for(int i = 0; i < chnlLst.length(); i++) {
					ofst[i] = Integer.parseInt(chnlLst.charAt(i) + "");
				}
				if (chnlLst.length() > 0) {
					MDR_PORTD.set(Param.OE.seti(vals, ofst, "&~")).cmt("вход").build();
					MDR_PORTD.set(Param.ANALOG.seti(vals, ofst, "&~")).cmt("аналоговый").build();
				}
				break;
			case 2:
			case 3:
				int adcIndex = codeStep - 2;
				int adcSrcDiv = getConfPropInt("adc_src", adcIndex) == 0 ? 0 :
						getConfPropInt("adc_div", adcIndex);
				int syncSrc = getClockProp("ADC-C1.S") % 2 == 1 ? 1 : 2;
				int mref = getConfPropInt("base_power", adcIndex);
				int sample = getConfPropInt("start_kind", adcIndex);
				int adcSwCh = getConfPropInt("sw_chn");
				
				String chnLst = getConfPropStr("lst_chn", adcIndex);
				String chnsLst = cleanChannelsList(chnLst);
				String chnCmt = "";

				MDR_ADC.get().arr(comments).set(oldCode);
				int chn;
				int chns = 0;

				if (chnsLst.length() > 1) {
					chn = 1;
					adcSwCh = 1;
				} else chn = chnsLst.isEmpty() ? 0 : Integer.parseInt(chnsLst);
				for(int chni = 0; chni < 16; chni++) {
					if (!chnsLst.contains(chni + "")) continue;
					chns |= 1 << chni;
					chnCmt += (chnCmt.length() > 0 ? "|" : "") + String.format("1 << %d", chni);
				}
				if (chn >= 0) {
					MDR_ADC.set((codeStep > 2 ? Param.ADC2_CFG : Param.ADC1_CFG)
							.set(1, syncSrc - 1, sample, chn, adcSwCh,mref, adcSrcDiv)
							.shift(0, 2, 3, 4, 9, 11, 12)).args(chnLst).cmt(0, 1, 2, 3, 4, 5, 6).build();
				}
				if (adcSwCh > 0) {
					MDR_ADC.set((codeStep > 2 ? Param.ADC2_CHSEL : Param.ADC1_CHSEL)
							.set(chns)).cmt(chnCmt).build();
				}
				break;
			case 4:
				int NUMCH = 0; // channels indexes for switching

				g().addCodeStrR(oldCode, "/* выбор для оцифровки датчика температуры */");
				g().addCodeStr(oldCode, "    MDR_ADC->ADC1_CFG " +
						(isCboxChecked(0) ? "|= " : "= "));

				g().addCodeStrR(oldCode, "/*включение вых. усилителя*/");
				g().addCodeStr(oldCode, " ((1 << 19) |(1 << 18) |(1 << 17) ");

				if (getConfPropInt("sw_chn") > 1) {
					g().addCodeStr(oldCode, "/*включено переключение каналов*/");
					g().addCodeStr(oldCode, "                          |(1 << 9)); ");
					g().addCodeStrL(oldCode, "/*выбор каналов для переключения*/");
					g().addCodeStr(oldCode, "    MDR_ADC->ADC1_CHSEL = (( 1 << 31) | ( 1 << " + NUMCH + "));");
				} else {
					g().addCodeStr(oldCode, "/*номер канала преобразования*/");
					g().addCodeStr(oldCode, "|(31 << 4)); ");
				}
				g().addCodeStrL(oldCode, "");
				break;
		}
		return oldCode;
	}

	private String cleanChannelsList(String chnlLst) {
		return chnlLst.replaceAll("[\\s,\\[\\]]","").replace("RESET", "");
	}

	@Override
	protected String[] methodNames() {
		return new String[]{"ADC", "TS"};
	}

		//, Color\.\w+,\sFontStyle\.\w+,\s\w+
	@Override
	protected boolean methodNeeded(String methodName) {
		switch (methodName) {
			case "ADC":
				return true;
			case "TS":
				return getConfPropInt("temp_sens") > 0;
			default:
				return false;
		}
	}

	@Override
	protected List<String> generateCode(Device device, List<String> oldCode, String methodName) {
		switch (methodName) {
			case "ADC":
				int syncSrc = getClockProp("ADC-C1.S") % 2 == 1 ? 1 : 2;
				if (syncSrc == 2) generateCode(oldCode, 0);
				generateCode(oldCode, 1);
				if (isCboxChecked(0)) generateCode(oldCode, 2);
				if (isCboxChecked(1)) generateCode(oldCode, 3);
				return super.generateCode(device, oldCode, methodName);
			case "TS":
				generateCode(oldCode, 4);
				return super.generateCode(device, oldCode, methodName);
		}
		return oldCode;
	}

	@Override
	protected void checkSelectedPin(String comboKey, String value) {
		super.checkSelectedPin(comboKey, value);
		if (comboKey.matches(getDevicePair().name() + "-\\d")) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			setModelProp("lst_chn", Integer.parseInt(ind) - 1, value);
		}
	}
}
