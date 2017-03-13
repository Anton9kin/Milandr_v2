package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.*;
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
	public List<String> generateCode(Device device, List<String> oldCode) {
		log.debug(String.format("#generateDACCode(%s)", device));

		//code block generation
		oldCode = Lists.newArrayList();
		generateCode(oldCode, 0);

		return super.generateCode(device, oldCode);
	}
}
