package milandr_ex.model.mcu.ext;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

import java.util.List;

import static milandr_ex.data.McuBlockProperty.*;

public class MCUUsbController extends MCUExtPairController {

	@FXML
	private GridPane usb_grid;

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.USB);
		//noinspection unchecked
		addModelProps(new String[]{"usb_mode", "usb_len", "usb_sph", "usb_spo"},
				usbModeList, usbSpeedList, usbPolarList, usbPushPullList);
		addModelProps(new String[]{"usb_txd"}, "", "B", true);
	}
	private static String[] ustr = { "отключен", "включен" };

	@Override
	protected Pane getPropControl() { return usb_grid; }

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		// 2 строка
		g().addCodeStr(oldCode, "    unsigned char i = 0;");

		// 2.1 строка
		g().addCodeStr(oldCode, "//тактирование USB");
		g().addCodeStr(oldCode, "MDR_RST_CLK->PER_CLOCK |= (1 << 2); ");

		// 3 строка
		g().addCodeStrR(oldCode, "/* есть тактовая частота */");
		g().addCodeStr(oldCode, "MDR_RST_CLK->USB_CLOCK = ((1 << 8) ");

		// 4 строка
		if (isCboxChecked(0)) {
			g().addCodeStr(oldCode, "/*источник для USB_C1*/");
			g().addCodeStr(oldCode, "| (1 << 0) ");
		}
		if (isCboxChecked(1)) {
			// 5 строка
			g().addCodeStr(oldCode, "/*источник для USB_C2*/");
			g().addCodeStr(oldCode, "| (1 << 2)");
		}


		int usbclksel = getClockProp("USB-C2.S");
		// 6 строка
		g().addCodeStr(oldCode, "/*источник для USB_C3 (USB_CLK)*/");
		g().addCodeStr(oldCode, "| (" + (usbclksel - 1) + " << 4));");

		int pllusbmul = 1;
		if (pllusbmul > 0)
		{
			// 7 строка
			g().addCodeStrL(oldCode, "//вкл. PLL_USB | коэф. умножения = " + pllusbmul);
			g().addCodeStr(oldCode, "MDR_RST_CLK->PLL_CONTROL = ((1 << 0) | (" + (pllusbmul - 1) + " << 4)); ");

			// 8 строка
			g().addCodeStr(oldCode, "//ждем когда PLL_USB выйдет в раб. режим ");
			g().addCodeStr(oldCode, "while ((MDR_RST_CLK->CLOCK_STATUS & 0x01) != 0x01); ");
			g().addCodeStrR(oldCode, "");
		}
		// 9 строка
		g().addCodeStrL(oldCode, "//программный сброс контроллера USB");
		g().addCodeStr(oldCode, "for (i = 0; i < 20; i++) MDR_USB->HSCR = (1 << 1); ");

		// 10 строка
		g().addCodeStr(oldCode, "//рабочий режим контроллера USB");
		g().addCodeStr(oldCode, "MDR_USB->HSCR &=~ (1 << 1); ");

		// 11 строка
		String usbMode = getConfPropStr("usb_mode");
		int  usbHost = getConfPropInt("usb_mode");
		g().addCodeStrR(oldCode, "/* режим работы - " + usbMode + " */");
		g().addCodeStr(oldCode, "MDR_USB->HSCR = ((" + usbHost + " << 0) ");

		// 12 строка
		int entx = getConfPropInt("usb_txd");
		g().addCodeStr(oldCode, "//TX - " + ustr[entx] + " | RX - включен ");
		g().addCodeStr(oldCode, "| (" + entx + " << 2) | (1 << 3) ");

		// 13 строка
		String usbUp = getConfPropStr("usb_spo");
		int  usbPull = getConfPropInt("usb_spo");
		g().addCodeStr(oldCode, "// Управление подтяжкой - " + usbUp);
		g().addCodeStr(oldCode, "| (" + usbPull + "));");

		String usbSpeed = getConfPropStr("usb_len");
		int  usbLen = getConfPropInt("usb_len");
		String usbPol = getConfPropStr("usb_len");
		int  usbPl = getConfPropInt("usb_len");
		if (usbHost == 1)
		{
			// 14 строка
			g().addCodeStrL(oldCode, "//cкорость = " + usbSpeed + " | полярность = " + usbPol);
			g().addCodeStr(oldCode, "MDR_USB->HTXLC |= ((" + usbLen + " << 4) | (" + usbPl + " << 3)); ");
		}
		else
		{
			// 14 строка
			g().addCodeStrL(oldCode, "//cкорость = " + usbSpeed + " | полярность = " + usbPol + " | вкл. окон. точек");
			g().addCodeStr(oldCode, "MDR_USB->SC |= ((" + usbLen + " << 5) | (" + usbPl + " << 4) | 1); ");
		}
		return super.generateSimpleCodeStep(oldCode, codeStep);
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		generateCode(oldCode, 0);
		return super.generateCode(device, oldCode);
	}
}
