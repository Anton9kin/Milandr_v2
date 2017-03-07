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
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "intrp", true));
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "wrk_kind", modeList));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "time_freq", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "tf_units", unitList));
	}

	@Override
	public List<String> generateCode(Device device, Device.EPairNames pairBlock,
									 PinoutsModel model, List<String> oldCode) {
		log.debug(String.format("#generateDACCode(%s, %s, %s)", device, pairBlock, model));
		McuBlockModel blockModel = pairBlock.model();
		//handling incoming parameters
		Integer reloadReg = blockModel.getProp("sign_src").getIntValue();
		int interrupt = blockModel.getProp("intrp").getIntValue();
		int source = blockModel.getProp("sign_src").getIntValue();
		int mode = blockModel.getProp("wrk_kind").getIntValue();

		//code block generation
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateSystCode(%s, %s, %s) %d, %d, %d, %d", device, pairBlock, model,
				reloadReg, interrupt, source, mode));
		g().addCodeStr(oldCode, "void SysTick_Init(void){");

		g().addCodeStr(oldCode, "SysTick->LOAD = 0x" + Integer.toString(reloadReg, 16) + ";");
		g().addCodeStr(oldCode, "//стартовое значение загружаемое в регистр VAL");
		g().addCodeStr(oldCode, "SysTick->VAL = 0x00;");
		g().addCodeStr(oldCode, "SysTick->CTRL = ((1 << 0)");

		g().addCodeStr(oldCode, "//включение таймера");
		g().addCodeStr(oldCode, "| ((" + interrupt + " << 1)");
		g().addCodeStr(oldCode, "//" + g().EN_INT[interrupt] + " прерывания");
		g().addCodeStr(oldCode, "| ((" + source + " << 2));");

		g().addCodeStr(oldCode, "//источник синхросигнала = " + g().EN_IST[source] + "");

		g().addCodeStr(oldCode, "}//void SysTick_Init");
		return super.generateCode(device, pairBlock, model, oldCode);
	}
}
