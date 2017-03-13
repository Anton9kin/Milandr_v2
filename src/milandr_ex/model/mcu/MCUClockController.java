package milandr_ex.model.mcu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.*;
import milandr_ex.model.BasicController;
import milandr_ex.model.mcu.ext.MCUExtPairController;
import milandr_ex.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.*;
import static milandr_ex.data.McuBlockProperty.buccList;
import static milandr_ex.data.McuBlockProperty.uccList;
import static milandr_ex.utils.GuiUtils.*;

/**
 * Created by lizard2k1 on 24.02.2017.
 */
public class MCUClockController extends MCUExtPairController
		implements PinoutsModel.Observer {
	private static final Logger log	= LoggerFactory.getLogger(MCUClockController.class);

	private Map<String, ComboBox> clkMap = Maps.newHashMap();
	private Map<String, Label> cllMap = Maps.newHashMap();
	private Map<String, GridPane> clkBlocks = Maps.newLinkedHashMap();
	private GridPane clckCont;
	@FXML
	private GridPane cpu_grid;

	public MCUClockController() {
	}

	@Override
	protected Pane getPropControl() {
		return cpu_grid;
	}

	public List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		Integer hClk = getClockProp("HCLK");
		int delayEeprom = getDelayEeprom(hClk);
		int lowBKP = getLowBKP(hClk);

		Integer hse = getClockProp("HSE");
		int pllMull = getClockProp("CPU-C2-1.S");
		int cpuC1Sel = getClockProp("CPU-C1.S");

		int cpuC2Sel = getClockProp("CPU-C2.S");
		int cpuC3Sel = getClockProp("HCLK.S");
		int hclkSel = getClockProp("HCLK-1.S");

		switch (codeStep) {
			case 0:
				g().addCodeStr(oldCode,"	//Необходима пауза для работы Flash-памяти программ");
				g().addCodeStr(oldCode,"	MDR_EEPROM->CMD |= (" + delayEeprom + " << 3);");
				g().addCodeStr(oldCode,"");
				break;
			case 1:
				g().addCodeStr(oldCode,"// вкл. HSE осцилятора (частота кварца " + hse + ")");
				g().addCodeStr(oldCode,"	MDR_RST_CLK->HS_CONTROL = 0x01;");

				g().addCodeStr(oldCode,"// ждем пока HSE выйдет в рабочий режим\n\r");
				g().addCodeStr(oldCode,"	while ((MDR_RST_CLK->CLOCK_STATUS & (1 << 2)) == 0x00);");
				g().addCodeStr(oldCode,"");
				break;
			case 2:
				g().addCodeStr(oldCode," //подача частоты на блок PLL");
				g().addCodeStr(oldCode,"	MDR_RST_CLK->CPU_CLOCK = ((" + cpuC1Sel + " << 0));");
				g().addCodeStr(oldCode," //вкл. PLL  | коэф. умножения = " + pllMull + "");
				g().addCodeStr(oldCode,"	MDR_RST_CLK->PLL_CONTROL = ((1 << 2) | (" + (pllMull - 1) + " << 8));");
				g().addCodeStr(oldCode," // ждем когда PLL выйдет в раб. режим");
				g().addCodeStr(oldCode,"	while ((MDR_RST_CLK->CLOCK_STATUS & 0x02) != 0x02);");
				g().addCodeStr(oldCode,"");
				break;
			case 3:
				g().addCodeStr(oldCode," //источник для CPU_C1");
				g().addCodeStr(oldCode,"	MDR_RST_CLK->CPU_CLOCK = ((" + cpuC1Sel + " << 0)");
				g().addCodeStr(oldCode," //источник для CPU_C2");
				g().addCodeStr(oldCode,"							| (" + cpuC2Sel + " << 2)");
				g().addCodeStr(oldCode," //предделитель для CPU_C3");
				g().addCodeStr(oldCode,"							| (" + cpuC3Sel + " << 4)");
				g().addCodeStr(oldCode," //источник для HCLK");
				g().addCodeStr(oldCode,"							| (" + hclkSel + " << 8));");

				g().addCodeStr(oldCode,"");

				g().addCodeStr(oldCode,"//режим встроенного регулятора напряжения DUcc");
				g().addCodeStr(oldCode,"	MDR_BKP->REG_0E |= ((" + lowBKP + " << 0) ");
				g().addCodeStr(oldCode,"//выбор доп.стабилизирующей нагрузки");
				g().addCodeStr(oldCode,"					  | (" + lowBKP + " << 3)); ");
				g().addCodeStr(oldCode,"");
				break;
		}
		return oldCode;
	}

	public List<String> generateComplexCodeStep(List<String> oldCode, int codeStep) {
		Integer hClk = getClockProp("HCLK");
		int delayEeprom = getDelayEeprom(hClk);
		int lowBKP = getLowBKP(hClk);

		Integer hse = getClockProp("HSE");
		int pllMull = getClockProp("CPU-C2-1.S");
		int cpuC1Sel = getClockProp("CPU-C1.S");

		int cpuC2Sel = getClockProp("CPU-C2.S");
		int cpuC3Sel = getClockProp("HCLK.S");
		int hclkSel = getClockProp("HCLK-1.S");

		switch (codeStep) {
			case 0:
				g().setCodeParameter(oldCode, "Необходима пауза для работы Flash-памяти программ",
						"MDR_EEPROM->CMD",  delayEeprom + " << 3", "|");
				break;
			case 1:
				g().setCodeParameter(oldCode,"вкл. HSE осцилятора (частота кварца " + hse + ")",
						"MDR_RST_CLK->HS_CONTROL", "0x01");
				g().execCodeCommand(oldCode,"ждем пока HSE выйдет в рабочий режим",
						"	while ((MDR_RST_CLK->CLOCK_STATUS & (1 << 2)) == 0x00);", "");
				break;
			case 2:
				g().setCodeParameter(oldCode,"подача частоты на блок PLL",
						"MDR_RST_CLK->CPU_CLOCK",+ cpuC1Sel + " << 0");
				g().setCodeParameter(oldCode,"вкл. PLL  | коэф. умножения = " + pllMull,
						"MDR_RST_CLK->PLL_CONTROL","(1 << 2) | (" + (pllMull - 1) + " << 8)");
				g().execCodeCommand(oldCode,"ждем когда PLL выйдет в раб. режим",
						"while ((MDR_RST_CLK->CLOCK_STATUS & 0x02) != 0x02);", "");
				break;
			case 3:
				g().addCodeStr(oldCode,"");
				String[] comments = {"источник для CPU_C1", "источник для CPU_C2", "предделитель для CPU_C3", "источник для HCLK"};
				String[] values = {cpuC1Sel + " << 0", cpuC2Sel + " << 2", cpuC3Sel + " << 4", hclkSel + " << 8"};
				g().setCodeParameters(oldCode, "MDR_RST_CLK->CPU_CLOCK", comments, values);

				comments = new String[]{"режим встроенного регулятора напряжения DUcc", "выбор доп.стабилизирующей нагрузки"};
				values = new String[]{ lowBKP + " << 0",  lowBKP + " << 3 "};
				g().setCodeParameters(oldCode, "MDR_BKP->REG_0E", comments, values);
				break;
		}
		return oldCode;
	}

	public List<String> generateBuilderCodeStep(List<String> oldCode, int codeStep) {
		Integer hClk = getClockProp("HCLK");
		int delayEeprom = getDelayEeprom(hClk);
		int lowBKP = getLowBKP(hClk);

		Integer hse = getClockProp("HSE");
		int pllMull = getClockProp("CPU-C2-1.S");
		int cpuC1Sel = getClockProp("CPU-C1.S");

		int cpuC2Sel = getClockProp("CPU-C2.S");
		int cpuC3Sel = getClockProp("HCLK.S");
		int hclkSel = getClockProp("HCLK-1.S");

		switch (codeStep) {
			case 0:
				b().setComments("Необходима пауза для работы Flash-памяти программ")
						.setParam("MDR_EEPROM->CMD").setValues(delayEeprom + " << 3")
						.setOpp("|").buildParam(oldCode);
				break;
			case 1:
				b().setComments("вкл. HSE осцилятора (частота кварца " + hse + ")")
						.setParam("MDR_RST_CLK->HS_CONTROL").setValues("0x01")
						.buildParam(oldCode);

				b().setComments("ждем пока HSE выйдет в рабочий режим")
						.setParam("	while ((MDR_RST_CLK->CLOCK_STATUS & (1 << 2)) == 0x00);")
						.buildCommand(oldCode);
				break;
			case 2:
				b().setCommentParamValue("подача частоты на блок PLL",
						"MDR_RST_CLK->CPU_CLOCK", cpuC1Sel + " << 0").buildParam(oldCode);
				b().setCommentParamValue("вкл. PLL  | коэф. умножения = " + pllMull,
						"MDR_RST_CLK->PLL_CONTROL", "(1 << 2) | (" + (pllMull - 1) + " << 8)").buildParam(oldCode);
				b().setCommentCommand("ждем когда PLL выйдет в раб. режим",
						"while ((MDR_RST_CLK->CLOCK_STATUS & 0x02) != 0x02);").buildCommand(oldCode);
				break;
			case 3:
				b().setComments("источник для CPU_C1", "источник для CPU_C2", "предделитель для CPU_C3", "источник для HCLK");
				b().setValues(cpuC1Sel + " << 0", cpuC2Sel + " << 2", cpuC3Sel + " << 4", hclkSel + " << 8");
				b().setParam("MDR_RST_CLK->CPU_CLOCK").buildParams(oldCode);

				b().setComments("режим встроенного регулятора напряжения DUcc", "выбор доп.стабилизирующей нагрузки");
				b().setValues( lowBKP + " << 0",  lowBKP + " << 3 ");
				b().setParam("MDR_BKP->REG_0E").buildParams(oldCode);
				break;
		}
		return oldCode;
	}

	private int getDelayEeprom(int hClk) {
		int delayEeprom = hClk % 25_000_000;
		if (delayEeprom > 7) delayEeprom = 7;
		return delayEeprom;
	}
	public List<String> generateCode(List<String> oldCode, int step) {
		switch (getScene().genKind()) {
			case SIMPLE: return generateSimpleCodeStep(oldCode, step);
			case COMPLEX: return generateComplexCodeStep(oldCode, step);
			case BUILDER: return generateBuilderCodeStep(oldCode, step);
		}
		return oldCode;
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		Integer hClk = getClockProp("HCLK");
		int cpuC1Sel = getClockProp("CPU-C1.S");
		int cpuC2Sel = getClockProp("CPU-C2.S");
		boolean pllCheck = cpuC2Sel > 0;

		if (cpuC1Sel < 2) {//if (HSECheck.isSelected() || HSE2Check.isSelected()){
			generateCode(oldCode, 0);
			if (hClk > 80000000){
				g().addCodeStr(oldCode,"//Частота > 80 МГц: работа миксросхемы не гарантируется!!!");
			}
			generateCode(oldCode, 1);
		}

		if (pllCheck){
			generateCode(oldCode, 2);
		}

		generateCode(oldCode, 3);
		return super.generateCode(device, oldCode);
	}

	private int getLowBKP(Integer hClk) {
		int lowBKP = 4; // which case take lowBKP eq 4 value ?!
		if (hClk < 200_000) lowBKP = 1; // -xxxxx < hClk < 200_000
		else if (hClk < 500_000) lowBKP = 2; // 200_000 <= hClk < 500_000
		else if (hClk < 1_000_000) lowBKP = 3; // 500_000 <= hClk < 1_000_000
		else if (hClk < 10_000_000) lowBKP  = 0; // 1_000_000 <= hClk < 10_000_000
		else if (hClk < 40_000_000) lowBKP  = 5; // 10_000_000 <= hClk < 40_000_000
		else if (hClk < 80_000_000) lowBKP  = 6; // 40_000_000 <= hClk < 80_000_000
		else if (hClk >= 80_000_000) lowBKP  = 7; // 80_000_000 <= hClk < +xxxxx
		return lowBKP;
	}

	public void setClckCont(GridPane clckCont) {
		this.clckCont = clckCont;
	}

	private static final String[] cpuProps = {"hsi", "hse", "lsi", "lse", "-", "adc_clk", "cpu_clk", "usb_clk", "hclk"};
	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.CPU);
		addModelProps(cpuProps, true);
//		addModelProps(new String[]{"bp_ucc", "bp_bucc"}, uccList, buccList);
		scene.addObserver("pinouts", this);
		fillClockGrid();
		fillCpuConfProperties();
	}

	private void fillCpuConfProperties() {
		for(String cpuProp: cpuProps) {
			McuBlockProperty mcuProp = getDevicePair().model().getProp(cpuProp);
			mcuProp.setIntValue(getClockProp(mcuProp.getMsgTxt()));
		}
		AppScene scene = getScene();
		scene.getCodeGenerator().listenPinsChanges(scene.getDevice(), getDevicePair(), scene.getPinoutsModel());
	}

	@Override
	public void observe(PinoutsModel pinoutsModel) {
		if (pinoutsModel == null) return;
		Map<String, String> pins = pinoutsModel.getSelectedPins();
		iterateComboMap("k-", pins, clkMap, true);
	}

	private void fillClockGrid() {
		if (clckCont == null) return;
		clckCont.setStyle("-fx-background-color: " + toRGBCode(bcDef) + "; -fx-grid-lines-visible: true");
		makePaddings(clckCont);
		String[][] combostrs = Constants.combostrs;
		List<ClockModel.Block> blocks = Lists.newArrayList();
		for(int i = 0; i < combostrs.length - 1; i++) {
			for(int j = 0; j < combostrs[i].length; j++) {
				String namestr = combostrs[i][j];
				if (namestr.isEmpty()) {
					VBox imgBox = makeImageBox();
					GridPane.setColumnIndex(imgBox, j);
					GridPane.setRowIndex(imgBox, i);
					clckCont.getChildren().add(imgBox);
					continue;
				}
				String combostr = combostrs[combostrs.length - 1][i * combostrs[i].length + j];
				makeClockGridItem(namestr, combostr, j, i);
				blocks.add(ClockModel.Block.get(namestr, combostr));
			}
		}
		PinoutsModel model = getScene().getPinoutsModel();
		ClockModel clock = ClockModel.get(model.getSelectedBody(), blocks);
		model.setClockModel(clock);
		clock.setInputs(new String[]{"HSI", "HSE", "LSI", "LSE"}, new int[]{8000000, 8000000, 40000, 32000});
		clock.addRestriction("USB-CLK", "= 48000000");
		clock.addRestriction("ADC-CLK", "< 14000000");
		clock.addRestriction("HCLK", "< 80000000");
		clock.addRestriction("CPU-CLK", "< 80000000");
		// make usb 48 MHz
		selectCBox("k-USB-C2-0", "IN-2");
		selectCBox("k-USB-C2-9", "/ 1");
		selectCBox("k-USB-C2-30", "* 6");
	}

	private VBox makeImageBox() {
		ImageView iv = new ImageView("resourse/clock.png");
		iv.setFitWidth(360.0);
		iv.setFitHeight(180.0);
		return new VBox(iv);
	}

	@SuppressWarnings("unchecked")
	private void selectCBox(String comboKey, String value) {
		clkMap.get(comboKey).getSelectionModel().select(value);
	}

	private void makeClockGridItem(String caption, String combostr, int col, int row) {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		makePaddings(gridPane);
		addRowToGrid(gridPane, makeLabel(caption), 0, 0, 1);
		String exitStr = combostr.split("\\\\")[1];
		if (!exitStr.equals("Not")) {
			int splitLen = combostr.split("\\|").length;
			ComboBox<String> box = new ComboBox<>(splitLen > 5 ? (splitLen > 9 ? clItem3 : clItem) : clItem2);
			box.getSelectionModel().selectFirst();
//xtodo		makeListener("k-" + caption + "-0", box);
			GuiUtils.makeListener("k-" + caption + "-0", box, changeCallback);
			clkMap.put("k-" + caption + "-0", box);
			addRowToGrid(gridPane, box, 1, 0, 1);
		}
		makeComboGrid(gridPane, caption, combostr, 3);
//		addRowToGrid(gridPane, new Label("output"), 0, 2, 1);
		Region rgn = makeGridsCombo(exitStr);
		if (rgn instanceof ComboBox) {
			GuiUtils.makeListener("k-" + caption + "-9", (ComboBox) rgn, changeCallback);
			clkMap.put("k-" + caption + "-9", (ComboBox) rgn);
			rgn = new HBox(rgn, new Label(exitStr));
			rgn.setMinWidth(120.0);
		}
		addRowToGrid(gridPane, rgn, 1, 2, 1);
		if (!exitStr.equals("Sim") && !exitStr.equals("Not"))
			addRowToGrid(gridPane, new CheckBox("Allow"), 2, 2, 1);
		addRowToGrid(clckCont, new VBox(gridPane), col, row, 1);
		clkBlocks.put(caption, gridPane);
	}

	private GridPane makeComboGrid(GridPane gridPane, String prefix, String combostr, int span) {
		GridPane gridPane1 = new GridPane();
		makePaddings(gridPane1);
		String[] combos = combostr.split("\\|");
		int i = 0;
		while (combos.length > i) {
			addComboToGrid(gridPane1, prefix, combos[i], i % 4, i / 4);
			i++;
		}

		addRowToGrid(gridPane, gridPane1, 0, 1, span);
		return gridPane1;
	}

	private void setGridRowCol(GridPane gp, Node node, int row, int col) {
		GridPane.setColumnIndex(node, col);
		GridPane.setRowIndex(node, row);
		gp.getChildren().add(node);
	}
	private Region addComboToGrid(GridPane gridPane1, String prefix, String items, int col, int row) {
		if (items.startsWith("\\")) return null;
		Region cb = col % 2 == 0 ? makeLabel(items) : makeGridsCombo(items);
		if (cb instanceof ComboBox) {
			String key = "k-" + prefix + "-" + col + row;
			GuiUtils.makeListener(key, (ComboBox) cb, changeCallback);//xtodo	makeListener
			clkMap.put(key, (ComboBox) cb);
//			cb = new HBox(cb, new Label(items));
		}
		setGridRowCol(gridPane1, cb, row, col);
		return cb;
	}

	@Override
	public Map<String, ? extends Node> nodeMap() {
		return clkMap;
	}

	private static List<Integer> factors = Lists.newArrayList(10, 30, 11, 31, 12, 32);
	@Override
	public void callListener(String comboKey, String prev, String value) {
		if (comboKey == null || value == null) return;
		if (comboKey.startsWith("t-")) return;
		if (value.equals("null") || value.equals("RESET")) return;
		super.callListener(comboKey, prev, value);
		log_debug(log, String.format("#callListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		String subKey = comboKey.substring(2, comboKey.lastIndexOf("-"));
		Integer subInd = Integer.parseInt(comboKey.substring(comboKey.lastIndexOf("-") + 1));
		ClockModel clock = getScene().getPinoutsModel().getClockModel();
		if (clock == null) return;
		switch (subInd) {
			case 0 :
				clock.setSelected(subKey, Integer.parseInt(value.split("-")[1]) - 1);
				break;
			case 9 :
				clock.setFactor(subKey, "out", value);
				break;
			default:
				clock.setFactor(subKey, factors.indexOf(subInd), value);
				break;
		}
		log_debug(log, clock.calc().toStr(subKey));
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		if (comboKey == null) return;
		super.callGuiListener(comboKey, prev, value);
		if (value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(log, String.format("#switchComboIndex[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		}
		ComboBox comboBox = clkMap.get(comboKey);
		String preKey = comboKey.substring(0, comboKey.lastIndexOf("-"));
		String subKey = preKey.substring(2);
		Integer subInd = Integer.parseInt(comboKey.substring(comboKey.lastIndexOf("-") + 1));
		// 0, 10, 11, 12, 30, 31, 32
		if (comboBox == null || comboBox.getSelectionModel() == null) return;
		int selIndex = comboBox.getSelectionModel().getSelectedIndex();
		if (subInd != 0 && subInd != 9) {
			ComboBox zeroCBox = clkMap.get(preKey + "-0");
//			if (zeroCBox != null) {
//				zeroCBox.getItems().set(factors.indexOf(subInd), value);
//			}
		}
		setupOnHoverStyle(bcDef, bcTxt, comboBox);
//		setupOnHoverStyle(newClr("dbe0b6"), comboBox);
		saveSelectedPin(comboKey, value);
		ClockModel clock = getScene().getPinoutsModel().getClockModel();
		log_debug(log, String.format("#switchComboIndex[%d] process clock with key [%s, %s] -> %s)", 0, subKey, subInd, value));
//		int row = subInd % 10; int col = subInd / 10;
		int blockInd = 0;
		for(String key: clkBlocks.keySet()) {
			fillBlocksInputs(key, subInd, clock, key);
			String hzText = makeHzText(clock.getOut(key, -1));
			TextField label = findTextFromGrid(clkBlocks.get(key), 2, 1);
			if (label != null)  {
				label.setText(hzText);
			} else {
				HBox hb = findHBTextFromGrid(clkBlocks.get(key),2, 1);
				if (hb != null) {
					List<Node> hbc = hb.getChildren();
					for(Node nd: hbc) {
						if (nd instanceof Label) ((Label)nd).setText(hzText);
					}
				}
			}
//			blockInd++;
		}
		fillCpuConfProperties();
	}

	private void fillBlocksInputs(String subKey, Integer subInd, ClockModel clock, String key) {
		GridPane subGr = findGridFromGrid(clkBlocks.get(key), 1, 0);
		if (subGr != null) {
			for(int col = 0; col < 4; col++) for(int row = 0; row < 4; row++) {
				int pinOut = clock.getOut(key, (col / 2) + row * 2);
				TextField textF = findTextFromGrid(subGr, row, col);
				if (textF != null) {
					textF.setText(makeHzText(pinOut));
					String restr = clock.getRestr(key, (col / 2) + row * 2);
					if (!restr.isEmpty() && restr.contains(" ")) {
						//linear-gradient(from 0.0% 0.0% to 0.0% 100.0%, 0x7c833fff 0.0%, 0x899147ff 100.0%)
						//linear-gradient(from 0.0px 0.0px to 0.0px 5.0px, 0xc8cca5ff 0.0%, 0xdbe0b6ff 100.0%)
						boolean isDef = checkClockRestrictions(restr, pinOut);
						setupOnHoverStyle( isDef ? newClr("989898") : Color.RED,
								isDef ? bcTxt : Color.BLACK, textF);
					}
				}
//				Label label = findLabelFromGrid(subGr, row, col);
//				if (label != null) {
////					int pinOut = clock.getOut(subKey, factors.indexOf(subInd));
//					label.setText(makeHzText(pinOut));
//				}
			}
		}
	}

	private boolean checkClockRestrictions(String restr, int value) {
		String[] opp = restr.split("\\s");
		if (opp[0].equals("=")) {
			return value == Integer.parseInt(opp[1]);
		} else if (opp[0].equals("<")) {
			return value <= Integer.parseInt(opp[1]);
		} else if (opp[0].equals(">")) {
			return value >= Integer.parseInt(opp[1]);
		}
		return false;
	}
}
