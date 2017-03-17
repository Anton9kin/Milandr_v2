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
		addModelProps(new String[]{"sw_chn", "temp_sens"}, "BB");
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
		}
		return oldCode;
	}

	Module MDR_RST_CLK = Module.MDR_RST_CLK.get();
	Module MDR_PORTD = Module.MDR_PORTD.get();
	Module MDR_ADC = Module.MDR_ADC.get();
	String[] comments = {
			"начало преобразования %s канала",
			"источник синхросигнала",
			"выбор запуска",
			"номер канала преобразования",
			"источник опорного",
			"коэффициент деления частоты",
	};
	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		// xtodo implement using each selected adc channel

		switch (codeStep) {
			case 0:
				int adcC1 = getClockProp("ADC-C1.S");
				int adcC2 = getClockProp("ADC-C2.S");
				int adcDiv = getClockProp("ADC-C2-O.S");

				MDR_RST_CLK.get();
				MDR_RST_CLK.set(
						Param.ADC_MCO_CLOCK.set(adcC1, adcC2, adcDiv, 1).shift(0, 4, 8, 13)
					).pre(1, 1, 2, 4).cmt("ADC_C1", "ADC_C2", "ADC_C3", "ADC_CLK").build(oldCode);
				break;
			case 1:
				MDR_RST_CLK.set(Param.PER_CLOCK.seti(1, 17, "|")).cmt("тактирование АЦП").build(oldCode);
				MDR_PORTD.get();
				String chnlLst = getConfPropStr("lst_chn", 0) + getConfPropStr("lst_chn", 1);
				chnlLst = chnlLst.replaceAll("[\\s\\,\\[\\]]","").replace("RESET", "");
				Integer[] vals = new Integer[chnlLst.length()];
				Integer[] ofst = new Integer[chnlLst.length()];
				Arrays.fill(vals, 1);
				for(int i = 0; i < chnlLst.length(); i++) {
					ofst[i] = Integer.parseInt(chnlLst.charAt(i) + "");
				}
				if (chnlLst.length() > 0) {
					MDR_PORTD.set(Param.OE.seti(vals, ofst, "&~")).cmt("вход").build(oldCode);
					MDR_PORTD.set(Param.ANALOG.seti(vals, ofst, "&~")).cmt("аналоговый").build(oldCode);
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
				String chnLst = getConfPropStr("lst_chn", adcIndex);

				MDR_ADC.get().arr(comments);
				int chn = 0;
				for(int chni = 0; chni < 32; chni++) {
					if (!chnLst.contains(chni + "")) continue;
					chn |= 1 << chni;
				}
				if (chn > 0) {
					MDR_ADC.set((codeStep > 2 ? Param.ADC2_CFG : Param.ADC1_CFG)
							.set(1, syncSrc - 1, sample, chn, mref, adcSrcDiv)
							.shift(0, 2, 3, 4, 11, 12)).args(chnLst).cmt(0, 1, 2, 3, 4, 5).build(oldCode);
				}
				break;
		}
		return oldCode;
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s)", device));

		//, Color\.\w+,\sFontStyle\.\w+,\s\w+

		int syncSrc = getClockProp("ADC-C1.S") % 2 == 1 ? 1 : 2;
		if (syncSrc == 2) generateCode(oldCode, 0);

		generateCode(oldCode, 1);

		if (isCboxChecked(0)) generateCode(oldCode, 2);
		if (isCboxChecked(1)) generateCode(oldCode, 3);

		return super.generateCode(device, oldCode);
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
