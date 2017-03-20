package milandr_ex.model.mcu.inn;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.data.McuBlockProperty;
import milandr_ex.model.BasicController;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.div128List;
import static milandr_ex.data.McuBlockProperty.div512List;
import static milandr_ex.data.McuBlockProperty.unitList;

public class MCUTimerController extends BasicController {

	@FXML
	private GridPane tmr_grid;

	@Override
	protected Pane getPropControl() {
		return tmr_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.TMR);
		getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), "pre_div", div128List));
		getDevicePair().model().addModelProp(McuBlockProperty.get(getDevicePair(), "tim_clk", ""));
		getDevicePair().model().addModelProp(McuBlockProperty.getF(getDevicePair(), "freq_div", div512List, unitList),
				"f_div.main_div", "f_time.time_freq", "f_units.tf_units");
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int TIM_CLK_EN = 2;
		int TIM_BRG = 3;
		int TIM_PSG = 4;
		int TIM_ARR = 5;
		int timTim = 6;
		int timUnit = 7;
		switch (codeStep) {
			case 0:
                /* 2 строка */
				g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK |= 1 << " + (13 + TIM_CLK_EN) + ";");
				g().addCodeStr(oldCode, "//разрешение тактирования Таймера " + TIM_CLK_EN);

                /* 3 строка */
				switch (TIM_CLK_EN)
				{
					case 1:
						g().addCodeStr(oldCode, " /* делитель тактовой частоты Таймера 1 */");
						g().addCodeStr(oldCode, "    MDR_RST_CLK->TIM_CLOCK = (" + TIM_BRG);
						g().addCodeStr(oldCode, " /* разешение тактирования Таймера 1 */");
						g().addCodeStr(oldCode, "                            |(1 << 24)); ");
						break;
					case 2:
						g().addCodeStr(oldCode, " /* делитель тактовой частоты Таймера 2 */");
						g().addCodeStr(oldCode, "    MDR_RST_CLK->TIM_CLOCK = ((" + TIM_BRG + " << 8)");
						g().addCodeStr(oldCode, " /* разешение тактирования Таймера 2 */");
						g().addCodeStr(oldCode, "                            | (1 << 25)); ");
						break;
					case 3:
						g().addCodeStr(oldCode, " /* делитель тактовой частоты Таймера 3 */");
						g().addCodeStr(oldCode, "    MDR_RST_CLK->TIM_CLOCK = ((" + TIM_BRG + " << 16)");
						g().addCodeStr(oldCode, " /* разешение тактирования Таймера 3 */");
						g().addCodeStr(oldCode, "                            | (1 << 26)); ");
						break;
				}

                /* 4 строка */
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->PSG = 0x" + (TIM_PSG - 1) + ";");

                /* 5 строка */
				if (TIM_ARR > 65536) {
					g().addCodeStr(oldCode, "// ЗНАЧЕНИЕ НЕ ДОЛЖНО ПРЕВЫШАТЬ 65536!!! ИЗМЕНИТЕ ВВЕДЕННЫЕ ЗНАЧЕНИЯ!!!");
				}
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->ARR = 0x" + TIM_ARR + ";");
                /* 6 строка */
				g().addCodeStr(oldCode, "//разрешение прерывания по совпадению");
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->IE = (1 << 1);");

                /* 7 строка */
				g().addCodeStr(oldCode, "//счет вверх по TIM_CLK, таймер вкл.");
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->CNTRL = 0x01;");

                /* 8 строка */
				g().addCodeStr(oldCode, "    NVIC_EnableIRQ(Timer" + TIM_CLK_EN + "_IRQn);");

				break;
			case 1:
                /* 10 строка */
				g().addCodeStr(oldCode, "//Прерывание ( " + timTim + " " + timUnit + " )");
                /* 11 строка */
				g().addCodeStrR(oldCode, "    //Исполняемый код");
                /* 12 строка */
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->CNT = 0x0000;");
				g().addCodeStr(oldCode, "    MDR_TIMER" + TIM_CLK_EN + "->STATUS &= ~(1 << 1);");
				g().addCodeStr(oldCode, "    NVIC_ClearPendingIRQ(Timer" + TIM_CLK_EN + "_IRQn);");
				g().addCodeStrL(oldCode, "");
				break;
		}
		return oldCode;
	}

	@Override
	protected String[] methodNames() {
		return new String[]{"TMR", "IRQ"};
	}

	@Override
	protected List<String> generateCode(Device device, List<String> oldCode, String methodName) {
		generateCode(oldCode, methodName.equals("TMR") ? 2 : 1);
		return super.generateCode(device, oldCode, methodName);
	}
}
