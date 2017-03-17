package milandr_ex.model.mcu.ext;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUCanController extends MCUExtPairController {

	@FXML
	private GridPane can_gpio;

	@FXML
	private GridPane can_grid;

	@Override
	protected Pane getPropControl() {
		return can_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.CAN);
		//noinspection unchecked
		addModelProps(new String[]{"pre_div", "speed"}, div128List, speedList);
		addModelProps(new String[]{"-", "quants", "seg1", "seg2", "pseg"}, "-SIII");
		addModelProps(new String[]{"-", "r_speed", "error"}, true);
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int CAN_TX = 0, CAN_RX = 1, CAN_PORT = 2, CAN_PER = 4;
		int F_TX = 0, F_RX = 1, brpN = 1, SJW = 2;

                        /* 2 строка */
		g().addCodeStr(oldCode, "//Тактирование порта " + CAN_PORT);
		g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (1UL << %s);", CAN_PER);
                        /* 3 строка */
		g().addCodeStr(oldCode, "//режим работы порта ");
		g().addCodeStr(oldCode, "    MDR_PORT" + CAN_PORT + "->FUNC   |= ((" + F_TX + " << " + CAN_TX + "*2) | (" + F_RX + " << " + CAN_RX + "*2));");
                        /* 4 строка */
		g().addCodeStr(oldCode, "//цифровой ");
		g().addCodeStr(oldCode, "    MDR_PORT" + CAN_PORT + "->ANALOG |= ((1 << " + CAN_TX + "  ) | (1 << " + CAN_RX + "));");
                        /* 5 строка */
		g().addCodeStr(oldCode, "//управляемый драйвер");
		g().addCodeStr(oldCode, "    MDR_PORT" + CAN_PORT + "->PD     &=~((1 << " + CAN_TX + "  ) | (1 << " + CAN_RX + "));");
                        /* 6 строка */
		g().addCodeStr(oldCode, "//максимально быcтрый");
		g().addCodeStr(oldCode, "    MDR_PORT" + CAN_PORT + "->PWR    |= ((3 << " + CAN_TX + "*2) | (3 << " + CAN_RX + "*2));");
		g().addCodeStr(oldCode, "");

		if (isCboxChecked(0)) generateCanMdrCode(oldCode, "CAN1", 0, brpN, SJW);
		if (isCboxChecked(1)) generateCanMdrCode(oldCode, "CAN2", 1, brpN, SJW);
		return oldCode;
	}

	private void generateCanMdrCode(List<String> oldCode, String canBlock,
									int canInd, int brpN, int SJW) {
		int div = getConfPropInt("pre_div", canInd);
		int seg1 = getConfPropInt("seg1", canInd);
		int seg2 = getConfPropInt("seg2", canInd);
		int pseg = getConfPropInt("pseg", canInd);

                            /* 7 строка */
		g().addCodeStr(oldCode, "//тактирование %s", canBlock);
		g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (1UL << %d);", canInd);
                            /* 8 строка */
		g().addCodeStr(oldCode, " /*предделитель %s */", canBlock);
		g().addCodeStr(oldCode, "MDR_RST_CLK->CAN_CLOCK = (%s" + (canInd > 0 ? " << 8" : ""), div);
                            /* 9 строка */
		g().addCodeStrR(oldCode, " /*вкл. %s */", canBlock);
		g().addCodeStr(oldCode, "|(1 << %d));", (24 + canInd));
		g().addCodeStr(oldCode, "");
                            /* 10 строка */
		g().addCodeStrL(oldCode, "//  MDR_%s->BITTMNG = (BRP|(PSEG << 16)|(SEG1 << 19)|(SEG2 << 22)|(SJW << 25)|(SB << 27));", canBlock);
		g().addCodeStr(oldCode, "    MDR_%s->BITTMNG = (" + brpN + " |(  " + pseg + "  << 16)|(  " + seg1 + "  << 19)|(  "
				+ seg2 + "   << 22)|( " + SJW + " << 25)|( 0 << 27));", canBlock);
                            /* 11 строка */
		g().addCodeStr(oldCode, " //разрешение работы %s", canBlock);
		g().addCodeStr(oldCode, "    MDR_%s->CONTROL = (1 << 0);", canBlock);
		g().addCodeStr(oldCode, "");
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateCANCode(%s)", device));
		generateCode(oldCode, 0);
		return super.generateCode(device, oldCode);
	}

	@Override
	protected Parent getGPIOControl() {
		return can_gpio;
	}
}
