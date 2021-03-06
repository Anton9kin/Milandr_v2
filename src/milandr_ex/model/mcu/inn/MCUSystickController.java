package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
import milandr_ex.data.code.Module;
import milandr_ex.data.code.Param;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUSystickController extends BasicController {

	@FXML
	private GridPane syst_grid;


	@Override
	protected Pane getPropControl() {
		return syst_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.SYST);
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "sign_src", istList));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "intrp", true)); // прерывание
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "wrk_kind", modeList));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "time_freq", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "tf_units", unitList));
	}

	@SuppressWarnings("unused")
	private int getSysTickClock(){
		switch (getConfPropInt("sign_src")) {
			case 0: return getClockProp("LSI");
			case 1: return getClockProp("HCLK");
		}
		return 0;
	}

	private int getSysTickClockSrc(){
		return getBasicClockSrc(istList, "sign_src");
	}

	private int getSysTickReloadReg(){
		int reloadReg = getBasicReloadReg("time_freq", "tf_units", getSysTickClockSrc());
		return reloadReg - getConfPropInt("wrk_kind");
	}

	private String[] comments = {
			"",
			"стартовое значение загружаемое в регистр VAL",
			"включение таймера", "%s прерывания",
			"источник синхросигнала = %s",
	};

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		//handling incoming parameters
		Integer reloadReg = getSysTickReloadReg();
		int interrupt = getConfPropInt("intrp");
		int source = getConfPropInt("sign_src");
		int mode = getConfPropInt("wrk_kind");
		log.debug(String.format("#generateSystCode(%d) %d, %d, %d, %d", codeStep, reloadReg, interrupt, source, mode));

		g().addCodeStr(oldCode, "SysTick->LOAD = 0x" + Integer.toString(reloadReg, 16) + ";");
		g().addCodeStr(oldCode, "//стартовое значение загружаемое в регистр VAL");
		g().addCodeStr(oldCode, "SysTick->VAL = 0x00;");
		g().addCodeStr(oldCode, "//включение таймера");
		g().addCodeStr(oldCode, "SysTick->CTRL = ((1 << 0)");
		g().addCodeStr(oldCode, "//" + g().EN_INT[interrupt] + " прерывания");
		g().addCodeStr(oldCode, "| ((" + interrupt + " << 1)");
		g().addCodeStr(oldCode, "//источник синхросигнала = " + g().EN_IST[source] + "");
		g().addCodeStr(oldCode, "| ((" + source + " << 2));");
		return oldCode;
	}

	@Override
	protected List<String> generateComplexCodeStep(List<String> oldCode, int codeStep) {
		int interrupt = getConfPropInt("intrp");
		Integer reloadReg = getSysTickReloadReg();
		int source = getConfPropInt("sign_src");
		int mode = getConfPropInt("wrk_kind");
		log.debug(String.format("#generateSystCode(%d) %d, %d, %d, %d", codeStep, reloadReg, interrupt, source, mode));
		String hexRR = "0x" + Integer.toString(reloadReg, 16);
		g().setCodeParameter(oldCode, "", "SysTick->LOAD", hexRR);
		g().setCodeParameter(oldCode, "стартовое значение загружаемое в регистр VAL",
				"SysTick->VAL", "0x00");
		g().setCodeParameters(oldCode, "SysTick->CTRL",
				new String[]{"включение таймера", g().EN_INT[interrupt] + " прерывания", "источник синхросигнала = " + g().EN_IST[source]},
				new String[]{"0x00", interrupt + " << 1", source + " << 2"});
		return oldCode;
	}

	@Override
	protected List<String> generateBuilderCodeStep(List<String> oldCode, int codeStep) {
		int interrupt = getConfPropInt("intrp");
		Integer reloadReg = getSysTickReloadReg();
		int source = getConfPropInt("sign_src");
		int mode = getConfPropInt("wrk_kind");
		log.debug(String.format("#generateSystCode(%d) %d, %d, %d, %d", codeStep, reloadReg, interrupt, source, mode));
		String hexRR = "0x" + Integer.toString(reloadReg, 16);
		b().setCommentsArr(comments).setModule("SysTick");

		b().setParam("LOAD").setValues(hexRR).buildParam(oldCode);
		b().setCommentParamValue(1, "VAL", "0x00").buildParam(oldCode);
		g().addCodeStr(oldCode,"");

		b().addComment(2).addComment(3, g().EN_INT[interrupt]).addComment(4, g().EN_IST[source]);
		b().setParam("CTRL").setValues(0, interrupt, source).setShifts(0, 1, 2).buildParams(oldCode);
		g().addCodeStr(oldCode,"");
		return oldCode;
	}

	private Module SysTick = Module.SysTick.get();
	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		int interrupt = getConfPropInt("intrp");
		Integer reloadReg = getSysTickReloadReg();
		int source = getConfPropInt("sign_src");
		int mode = getConfPropInt("wrk_kind");
		log.debug(String.format("#generateSystCode(%d) %d, %d, %d, %d", codeStep, reloadReg, interrupt, source, mode));
		String hexRR = "0x" + Integer.toString(reloadReg, 16);
		String a1 = g().EN_INT[interrupt], a2= g().EN_IST[source];
		SysTick.get().arr(comments).set(oldCode);
		SysTick.sete(Param.LOAD.set(hexRR));
		SysTick.sete(Param.VAL.set("0x00"));
		SysTick.args(0, a1, a2).sete(Param.CTRL.set(0, interrupt, source).shift(0, 1, 2));
		return oldCode;
	}
}
