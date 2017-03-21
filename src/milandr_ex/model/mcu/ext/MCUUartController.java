package milandr_ex.model.mcu.ext;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

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
		addModelProps(new String[]{"-", "word_len"}, "", "-SBB", 0, 8);
		addModelProps(new String[]{"odd_chk", "stp_bit"}, uartParityList, uartStopList);
		addModelProps(new String[]{"-", "r_speed", "error"}, true);
//		addModelProps(new String[]{"-", "word_len", "odd_chk", "stp_bit", "-", "r_speed", "error"}, "-SBB-SS",
//				"", "", true, true, "", "", "");
	}

	private static class UartData {
		String port;
		int uartTx, uartRx, ufTx, ufRx, uartPer;

		UartData(String port, int uartTx, int uartRx, int ufTx, int ufRx, int uartPer) {
			this.port = port;
			this.uartTx = uartTx;
			this.uartRx = uartRx;
			this.ufTx = ufTx;
			this.ufRx = ufRx;
			this.uartPer = uartPer;
		}
	}
//            switch (uartParity.SelectedIndex)
//	{
//		case 0: PEN = 0; EPS = 0; SPS = 0; break;
//		case 1: PEN = 1; EPS = 0; SPS = 0; break;
//		case 2: PEN = 1; EPS = 1; SPS = 0; break;
//		case 3: PEN = 1; EPS = 0; SPS = 1; break;
//		case 4: PEN = 1; EPS = 1; SPS = 1; break;
//	}
	private static class UartParity {
		int pen, eps, sps;
		UartParity(int idx) {
			this.pen = idx > 0 ?  1 : 0;
			this.eps = idx == 2 || idx == 4 ?  1 : 0;
			this.sps = idx > 2 ?  1 : 0;
		}
	}
	@Override
	protected Parent getGPIOControl() {
		return uart_gpio;
	}

	private UartData getUartData(int indx, int port) {
		UartData data = null;
		switch (indx) {
			case 0:
				switch (port) {
					case 0: data = new UartData("A", 7, 6, 3, 3, 21); break;
					case 1: data = new UartData("B", 5, 6, 2, 2, 22); break;
					case 2: data = new UartData("D", 8, 7, 3, 3, 24); break;
					case 3: data = new UartData("E", 13, 12, 3, 3, 25); break;
				}
				break;
			case 1:
				switch (port) {
					case 0: data = new UartData("B", 0, 1, 3, 3, 22); break;
					case 1: data = new UartData("D", 1, 0, 2, 2, 24); break;
					case 2: data = new UartData("F", 1, 0, 3, 3, 29); break;
				}
				break;
		}
		return data;
	}

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		generateSimpleCodeStep(oldCode, codeStep, 0);
		generateSimpleCodeStep(oldCode, codeStep, 1);
		return oldCode;
	}
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep, int uartInd) {
		boolean uartEn = isCboxChecked(uartInd);
		if (!uartEn) return oldCode;
		String uartDiv = getConfPropStr("pre_div", uartInd);
		int Udiv = getConfPropInt("pre_div", uartInd);

		String uartSpeed = getConfPropStr("speed", uartInd);
		String uartWord = getConfPropStr("word_len", uartInd);

		int EPS = getConfPropInt("odd_chk", uartInd);
		int STP = getConfPropInt("stp_bit", uartInd);
		String uartParity = getConfPropStr("odd_chk", uartInd);
		String uartStop = getConfPropStr("stp_bit", uartInd);

		int UART_CLK_EN = uartInd == 0 ? 1 : 0;
		UartData udata = getUartData(UART_CLK_EN, 1);
		UartParity upar = new UartParity(EPS);

		String uartlabel = "";
		int IBRD = 13, FBRD = 14;


                /* 2 строка */
		g().addCodeStr(oldCode,"//тактирование порта " + udata.port);
		g().addCodeStr(oldCode,"   MDR_RST_CLK->PER_CLOCK |= (1UL << " + udata.uartPer + "); ");

                /* 3 строка */
		g().addCodeStr(oldCode,"//режим работы порта ");
		g().addCodeStr(oldCode,"   MDR_PORT" + udata.port + "->FUNC   |= ((" +
				udata.ufTx + " << " + udata.uartTx + " * 2) | (" +
				udata.ufRx + " << " + udata.uartRx + " * 2)); ");

                /* 4 строка */
		g().addCodeStr(oldCode,"//цифровой");
		g().addCodeStr(oldCode,"   MDR_PORT" + udata.port + "->ANALOG |= ((1 << " +
				udata.uartTx + ")   | (1 << " + udata.uartRx + ")); ");

                /* 5 строка */
		g().addCodeStr(oldCode,"//максимально быcтрый");
		g().addCodeStr(oldCode,"   MDR_PORT" + udata.port + "->PWR    |= ((3 << " +
				udata.uartTx + " * 2) | (3 << " + udata.uartRx + " * 2)); ");

                /* 6 строка */
		g().addCodeStr(oldCode,"//тактирование UART" + UART_CLK_EN);
		g().addCodeStr(oldCode,"   MDR_RST_CLK->PER_CLOCK |= (1UL << " + (UART_CLK_EN + 5) + "); ");

                /* 7 строка */
		g().addCodeStr(oldCode," /* установка делителя для UART_CLK = HCLK" + uartDiv + " */");
		g().addCodeStr(oldCode,"   MDR_RST_CLK->UART_CLOCK |= ((" + Udiv + (uartInd == 0 ? ")" : " << 8)"));

                /* 8 строка */
		g().addCodeStrR(oldCode," /* разрешение тактовой частоты UART" + UART_CLK_EN + " */");
		g().addCodeStr(oldCode,"|(1 << " + (UART_CLK_EN + 23) + ")); ");

                /* 9 строка */
		g().addCodeStrL(oldCode,"   //Параметры делителя при частоте = " + uartlabel + " Гц и скорости = " + uartSpeed);

                /* 10 строка */
		g().addCodeStr(oldCode," //целая часть делителя скорости");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->IBRD = 0x" + IBRD + ";");

                /* 11 строка */
		g().addCodeStr(oldCode," //дробная часть делителя скорости");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->FBRD = 0x" + FBRD + ";");

                /* 12 строка */
		g().addCodeStr(oldCode," /* разрешение проверки четности */");
		g().addCodeStr(oldCode,"MDR_UART" + UART_CLK_EN + "->LCR_H = ((" + upar.pen + " << 1)");

                /* 13 строка */
		g().addCodeStrR(oldCode," /* четность/нечетность (" + uartParity + ") */");
		g().addCodeStr(oldCode,"|(" + upar.eps + " << 2)");

                /* 14 строка */
		g().addCodeStr(oldCode," /* стоп-бит = " + uartStop + " */");
		g().addCodeStr(oldCode,"|(" + STP + " << 3)");

                /* 15 строка */
		g().addCodeStr(oldCode," /* длина слова = " + uartWord + " */");
		g().addCodeStr(oldCode,"|(" + uartWord + " << 5)");

                /* 16 строка */
		g().addCodeStr(oldCode," /* передача бита четности */");
		g().addCodeStr(oldCode,"|(" + upar.sps + " << 7));");

                /* 17 строка */
		g().addCodeStrL(oldCode," //передачик и приемник разрешен, разрешение приемопередатчика");
		g().addCodeStr(oldCode,"   MDR_UART" + UART_CLK_EN + "->CR = ((1 << 8)|(1 << 9)|1);");
		g().addCodeStr(oldCode,"");
		return super.generateSimpleCodeStep(oldCode, codeStep);
	}
}
