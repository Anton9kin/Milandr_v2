package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.div128List;
import static milandr_ex.data.McuBlockProperty.uartList;

public class MCUUartController extends MCUExtPairController {
	@FXML
	private GridPane uart_gpio;
	@FXML
	private GridPane uart_grid;

	@Override
	protected Pane getPropControl() {
		return uart_grid;
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.UART);
		addModelProps(new String[]{"-"}, true);
		addModelProps(new String[]{"pre_div", "speed"}, div128List, uartList);
		addModelProps(new String[]{"-", "word_len", "odd_chk", "stp_bit", "-", "r_speed", "error"}, "", "-SBB-SS");
//		addModelProps(new String[]{"-", "word_len", "odd_chk", "stp_bit", "-", "r_speed", "error"}, "-SBB-SS",
//				"", "", true, true, "", "", "");
	}

	@Override
	protected Parent getGPIOControl() {
		return uart_gpio;
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int UART_PORT = 0;
		int UART_PER = 1;
		int UART_TX = 2;
		int UART_RX = 3;
		int UF_TX = 4;
		int UF_RX = 5;
		int UART_CLK_EN = 6;
		int Udiv = 7;
		int PEN = 8, EPS = 9, STP = 10, WLEN = 11, SPS = 12;
		String uartParity = "", uartStop = "", uartWord = "";
		String uartDiv = "", uartlabel = "", uartSpeed = "";
		int IBRD = 13, FBRD = 14;
		boolean uart1En = true;

                /* 2 строка */
		g().addCodeStr(oldCode,"//тактирование порта " + UART_PORT);
		g().addCodeStr(oldCode,"   MDR_RST_CLK->PER_CLOCK |= (1UL << " + UART_PER + "); ");

                /* 3 строка */
		g().addCodeStr(oldCode,"//режим работы порта ");
		g().addCodeStr(oldCode,"   MDR_PORT" + UART_PORT + "->FUNC   |= ((" + UF_TX + " << " + UART_TX + "*2) | (" + UF_RX + " << " + UART_RX + "*2)); ");

                /* 4 строка */
		g().addCodeStr(oldCode,"//цифровой");
		g().addCodeStr(oldCode,"   MDR_PORT" + UART_PORT + "->ANALOG |= ((1 << " + UART_TX + ")   | (1 << " + UART_RX + ")); ");

                /* 5 строка */
		g().addCodeStr(oldCode,"//максимально быcтрый");
		g().addCodeStr(oldCode,"   MDR_PORT" + UART_PORT + "->PWR    |= ((3 << " + UART_TX + "*2) | (3 << " + UART_RX + "*2)); ");

                /* 6 строка */
		g().addCodeStr(oldCode,"//тактирование UART" + UART_CLK_EN);
		g().addCodeStr(oldCode,"   MDR_RST_CLK->PER_CLOCK |= (1UL << " + (UART_CLK_EN + 5) + "); ");

                /* 7 строка */
		g().addCodeStr(oldCode," /* установка делителя для UART_CLK = HCLK" + uartDiv + " */");
		if (uart1En)
			g().addCodeStr(oldCode,"   MDR_RST_CLK->UART_CLOCK = ((" + Udiv + ")");
		else
			g().addCodeStr(oldCode,"   MDR_RST_CLK->UART_CLOCK = ((" + Udiv + " << 8)");

                /* 8 строка */
		g().addCodeStr(oldCode," /* разрешение тактовой частоты UART" + UART_CLK_EN + " */");
		g().addCodeStr(oldCode,"                            |(1 << " + (UART_CLK_EN + 23) + ")); ");

                /* 9 строка */
		g().addCodeStr(oldCode,"   //Параметры делителя при частоте = " + uartlabel + " Гц и скорости = " + uartSpeed);

                /* 10 строка */
		g().addCodeStr(oldCode," //целая часть делителя скорости");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->IBRD = 0x" + IBRD + ";");

                /* 11 строка */
		g().addCodeStr(oldCode," //дробная часть делителя скорости");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->FBRD = 0x" + FBRD + ";");

                /* 12 строка */
		g().addCodeStr(oldCode," /* разрешение проверки четности */");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->LCR_H = ((" + PEN + " << 1)");

                /* 13 строка */
		g().addCodeStr(oldCode," /* четность/нечетность (" + uartParity + ") */");
		g().addCodeStr(oldCode,"                      |(" + EPS + " << 2)");

                /* 14 строка */
		g().addCodeStr(oldCode," /* стоп-бит = " + uartStop + " */");
		g().addCodeStr(oldCode,"                      |(" + STP + " << 3)");

                /* 15 строка */
		g().addCodeStr(oldCode," /* длина слова = " + uartWord + " */");
		g().addCodeStr(oldCode,"                      |(" + WLEN + " << 5)");

                /* 16 строка */
		g().addCodeStr(oldCode," /* передача бита четности */");
		g().addCodeStr(oldCode,"                      |(" + SPS + " << 7));");

                /* 17 строка */
		g().addCodeStr(oldCode," //передачик и приемник разрешен, разрешение приемопередатчика");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->CR = ((1 << 8)|(1 << 9)|1);");
		return super.generateSimpleCodeStep(oldCode, codeStep);
	}
}
