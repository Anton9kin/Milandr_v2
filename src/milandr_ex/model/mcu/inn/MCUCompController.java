package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

import java.util.List;

/**
 * Created by lizard2k1 on 12.03.2017.
 */
public class MCUCompController extends BasicController {

	@FXML
	GridPane comp_gpio;
	@FXML
	GridPane comp_grid;

	@Override
	protected Parent getGPIOControl() {
		return comp_gpio;
	}

	@Override
	protected Pane getPropControl() {
		return comp_grid;
	}

	private boolean addSimplePropCode = true;
	private String[] strIE = { "Запрещено", "Разрешено" },
			strINV = { "Прямой", "Инверсный" },
			strCCH = { "IN2", "IN1", "IN3", "IVREF (1.2 B)" },
			strCREF = { "IN1", "CREF" },
			strCVRR = { "В верхнем диапозоне", "В нижнем диаозоне" };

	int addPinCnt(int tot, int pin) {
		return tot + (pin > 0 ? 1 : 0);
	}
	String addProp(List<String> oldCode, String pref, String level, int pin1, int pin2, int pin3, int refP, int refM)
	{
		if (!addSimplePropCode) {
			return addPropCmplx(pref, level, pin1, pin2, pin3, refP, refM);
		}
		int pinCnt = addPinCnt(0, pin1);
		pinCnt = addPinCnt(pinCnt, pin2);
		pinCnt = addPinCnt(pinCnt, pin3);
		pinCnt = addPinCnt(pinCnt, refP);
		pinCnt = addPinCnt(pinCnt, refM);

		g().addCodeStr(oldCode, pref);
		if (pin1 > 0)
		{
			switch (level)
			{
				case "OE":
				case "ANALOG":
					g().addCodeStr(oldCode,"(1 << 2)");
					break;
				case "PULL":
					g().addCodeStr(oldCode,"((1 << 2) | (1 << (2 << 16))");
					break;
			}

			pinCnt--;
			if (pinCnt > 0) g().addCodeStr(oldCode," | ");
		}
		if (pin2 > 0)
		{
			switch (level)
			{
				case "OE":
				case "ANALOG":
					g().addCodeStr(oldCode,"(1 << 3)");
					break;
				case "PULL":
					g().addCodeStr(oldCode,"((1 << 3) | (1 << (3 << 16))");
					break;
			}

			pinCnt--;
			if (pinCnt > 0) g().addCodeStr(oldCode," | ");
		}
		if (pin3 > 0)
		{
			switch (level)
			{
				case "OE":
				case "ANALOG":
					g().addCodeStr(oldCode,"(1 << 8)");
					break;
				case "PULL":
					g().addCodeStr(oldCode,"((1 << 8) | (1 << (8 << 16))");
					break;
			}

			pinCnt--;
			if (pinCnt > 0) g().addCodeStr(oldCode," | ");
		}
		if (refP > 0)
		{
			switch (level)
			{
				case "OE":
				case "ANALOG":
					g().addCodeStr(oldCode,"(1 << 4)");
					break;
				case "PULL":
					g().addCodeStr(oldCode,"((1 << 4) | (1 << (4 << 16))");
					break;
			}

			pinCnt--;
			if (pinCnt > 0) g().addCodeStr(oldCode," | ");
		}
		if (refM > 0)
		{
			switch (level)
			{
				case "OE":
				case "ANALOG":
					g().addCodeStr(oldCode,"(1 << 5)");
					break;
				case "PULL":
					g().addCodeStr(oldCode,"((1 << 5) | (1 << (5 << 16))");
					break;
			}
		}
		return "";
	}
	private String addPropCmplx(String pref, String level, int... pins) {
		if (pins == null) return pref;
		boolean first = true, isPull = level.equals("PULL");
		String codeLine = pref;
		for(int pin: pins) {
			if (pin > 0) {
				if (!first) codeLine += (isPull ? "\n\t\t" : "") + " | ";
				codeLine += String.format((isPull ? "(" : "") + "(1 << %s)"
						+ (isPull ? " | (1 << (%s << 16))" : ""), pin, pin);
				first = false;
			}
		}
		return codeLine;
	}
	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		int pinCompInP = getPinSelectedInd("COMP-00"); // 2 or 4
		int pinCompInM = getPinSelectedInd("COMP-01"); // 2 or 3 or 8 or 5
		int pinCompOut = getPinSelectedInd("COMP-02"); // 8 or 11
		int pinIn1 = pinCompInP == 2 || pinCompInM == 2 ? 2 : 0;
		int pinIn2 = pinCompInM == 3 ? 3 : 0;
		int pinIn3 = pinCompInM == 8 ? 8 : 0;
		int pinRefP = pinCompInP == 4 ? 4 : 0;
		int pinRefM = pinCompInM == 5 ? 5 : 0;


		//настройка COMP_OUT
		g().addCodeStr(oldCode, "//разрешение тактирования порта B");
		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK   |= (1UL << 22); ");
		g().addCodeStr(oldCode, " ");
		g().addCodeStr(oldCode, "//направление передачи данных = Выход");
		g().addCodeStr(oldCode, "    MDR_PORTB->OE     |= ((1 << %d));   ", pinCompOut);
		g().addCodeStr(oldCode, "//режим работы контроллера = Цифровой");
		g().addCodeStr(oldCode, "    MDR_PORTB->ANALOG |= ((1 << %d));   ", pinCompOut);
		g().addCodeStr(oldCode, "//режим работы вывода порта = %s", (pinCompOut > 8 ? "Переопределенная" : "Альтернативная"));
		g().addCodeStr(oldCode, "    MDR_PORTB->FUNC   |= ((%d << %d * 2)); ", pinCompOut > 8 ? 3 : 2, pinCompOut);
		g().addCodeStr(oldCode, "//скорость фронта вывода = максимальный");
		g().addCodeStr(oldCode, "    MDR_PORTB->PWR    |= ((3 << %d * 2)); ", pinCompOut);
		g().addCodeStr(oldCode, " ");

		//настройка COMP_IN, vref
		g().addCodeStr(oldCode, "//разрешение тактирования порта E");
		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK   |= (1UL << 25); ");

		g().addCodeStr(oldCode, "//направление передачи данных = Вход");
		String codeLine = addProp(oldCode, "    MDR_PORTE->OE     &=~ ( ",
				"OE", pinIn1, pinIn2, pinIn3, pinRefP, pinRefM);
		g().addCodeStr(oldCode, codeLine + " );");

		g().addCodeStr(oldCode, "//режим работы контроллера = Аналоговый");
		codeLine = addProp(oldCode, "    MDR_PORTE->ANALOG &=~ ( ",
				"ANALOG", pinIn1, pinIn2, pinIn3, pinRefP, pinRefM);
		g().addCodeStr(oldCode, codeLine + " );");

		g().addCodeStr(oldCode, "////запрещение подтяжки к GND и к VCC");
		codeLine = addProp(oldCode, "    MDR_PORTE->PULL   &=~ ( ",
				"PULL", pinIn1, pinIn2, pinIn3, pinRefP, pinRefM);
		g().addCodeStr(oldCode, codeLine + " );");

		// 2 строка
		g().addCodeStr(oldCode, "//тактирование Компаратора");
		g().addCodeStr(oldCode, "    MDR_RST_CLK->PER_CLOCK |= (1 << 19); ");

		int cmpcref = 0, cmpcch = 1;
		int cmpInt = getConfPropInt("intrp");
		int cmpInv = getConfPropInt("inv_out");
		// 3 строка
		g().addCodeStr(oldCode, "//прерывание - %s", strIE[cmpInt]);
		g().addCodeStr(oldCode, "    MDR_COMP->CFG = (( " + cmpInt + " << 13) ");

		// 4 строка
		g().addCodeStr(oldCode, "//выход - %s", strINV[cmpInv]);
		g().addCodeStr(oldCode, "                   | ( " + cmpInv + " << 11) ");

		// 6 строка
		g().addCodeStr(oldCode, "//на '+' компаратора подается %s", strCREF[cmpcref]);
		g().addCodeStr(oldCode, "                   | ( " + cmpcref + " <<  8) ");

		// 5 строка
		g().addCodeStr(oldCode, "//на '-' компаратора подается %s", strCCH[cmpcch]);
		g().addCodeStr(oldCode, "                   | ( " + cmpcch + " <<  9) ");

		int cmpcvren = 1, cmpcvr = 2, cmpcvrss = 3, cmpcvrr = 0;
		if (cmpcvren == 1)
		{
			// 7 строка
			g().addCodeStr(oldCode, "//внутряння шкала напряжения = %s", cmpcvr);
			g().addCodeStr(oldCode, "                   | ( " + cmpcvr + " <<  4) ");

			// 8 строка
			g().addCodeStr(oldCode, "//Источник CVREF = Разрешен");
			g().addCodeStr(oldCode, "                   | ( " + cmpcvren + " <<  3) ");

			// 9 строка
			g().addCodeStr(oldCode, "//Источник CVREF работает в границах %s", cmpcvrss);
			g().addCodeStr(oldCode, "                   | ( " + cmpcvrss + " <<  2) ");

			// 10 строка
			g().addCodeStr(oldCode, "//Источник CVREF работает в границах %s", strCVRR[cmpcvrr]);
			g().addCodeStr(oldCode, "                   | ( " + cmpcvrr + " <<  1) ");
		}
		// 11 строка
		g().addCodeStr(oldCode, "//Включенние Компаратора");
		g().addCodeStr(oldCode, "                   | ( 1 << 0)); ");
		return oldCode;
	}

	@Override
	protected List<String> generateComplexCodeStep(List<String> oldCode, int codeStep) {
		addSimplePropCode = false;
		return super.generateComplexCodeStep(oldCode, codeStep);
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		addSimplePropCode = true;
		generateCode(oldCode, 0);
		return super.generateCode(device, oldCode);
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.COMP);
		addModelProps(new String[]{"inv_out", "intrp"}, "","BB");
		addModelProps(new String[]{"bounds", "res_deps"}, (List)null, null);
	}
}
