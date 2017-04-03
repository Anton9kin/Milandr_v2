package milandr_ex.model.mcu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.*;
import milandr_ex.data.code.Command;
import milandr_ex.data.code.Module;
import milandr_ex.data.code.Param;
import milandr_ex.model.mcu.ext.MCUExtPairController;
import milandr_ex.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.*;
import static milandr_ex.utils.GuiUtils.*;

/**
 * Clock controller - hold logic for setup clock and generation CPU code block
 * Created by lizard2k1 on 24.02.2017.
 */
public class MCUClockController extends MCUExtPairController
		implements PinoutsModel.Observer {
	private static final Logger log	= LoggerFactory.getLogger(MCUClockController.class);

	private Map<String, ComboBox> clkMap = Maps.newHashMap();
	private Map<String, Spinner> spnMap = Maps.newHashMap();
//	private Map<String, Label> cllMap = Maps.newHashMap();
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

	@Override
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
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

	@Override
	protected List<String> generateComplexCodeStep(List<String> oldCode, int codeStep) {
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

	private String[] comments = {
			"Необходима пауза для работы Flash-памяти программ",
			"вкл. HSE осцилятора (частота кварца %s)",
			"ждем пока HSE выйдет в рабочий режим",
			"ждем пока PLL выйдет в рабочий режим",
			"подача частоты на блок PLL",
			"вкл. PLL  | коэф. умножения = %s",
			"режим встроенного регулятора напряжения DUcc",
			"выбор доп.стабилизирующей нагрузки",
	};
	@Override
	protected List<String> generateBuilderCodeStep(List<String> oldCode, int codeStep) {
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
				b().setCommentsArr(comments);
				b().setModule("MDR_EEPROM").setParam("CMD").setValues(delayEeprom).setShifts(3)
					.setComments(0).setOpp("|").buildParam(oldCode);
				break;
			case 1:
				b().addComment(1, hse).setParam("HS_CONTROL").setValues("0x01");
				b().setModule("MDR_RST_CLK").buildParam(oldCode);

				b().setWhileCommand("CLOCK_STATUS", "1 << 2", "== 0x00");
				b().setComments(2).buildCommand(oldCode);
				break;
			case 2:
				b().setCommentParamValue(3,"CPU_CLOCK", cpuC1Sel , 0).buildParam(oldCode);
				b().setParam("PLL_CONTROL").addValue("(1 << 2) | (%s << 8)", (pllMull - 1))
						.addComment(5, pllMull).buildParam(oldCode);
				b().setWhileCommand("CLOCK_STATUS", "0x02", "!= 0x02")
						.setComments(4).buildCommand(oldCode);
				break;
			case 3:
				b().setCommentPref("источник для ").setComments("CPU_C1", "CPU_C2", "CPU_C3", "HCLK");
				b().setValues(cpuC1Sel, cpuC2Sel, cpuC3Sel, hclkSel).setShifts(0, 2, 4, 8);
				b().setParam("CPU_CLOCK").buildParams(oldCode);

				b().setComments(6, 7).setValues(lowBKP, lowBKP).setShifts(0, 3);
				b().setModule("MDR_BKP").setParam("REG_0E").buildParams(oldCode);
				break;
		}
		return oldCode;
	}

	private Module MDR_EEPROM = Module.MDR_EEPROM.get();
	private Module MDR_RST_CLK = Module.MDR_RST_CLK.get();
	private Module MDR_BKP = Module.MDR_BKP.get();

	private String[][] cmnts = {
	{ "Необходима пауза для работы Flash-памяти программ"},{
		"вкл. HSE осцилятора (частота кварца %s)",
		"ждем пока HSE выйдет в рабочий режим",
		"подача частоты на блок PLL",
		"вкл. PLL  | коэф. умножения = %s",
		"ждем пока PLL выйдет в рабочий режим"
	}, {
		"режим встроенного регулятора напряжения DUcc",
		"выбор доп.стабилизирующей нагрузки",
	}};
	@Override
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		Integer hClk = getClockProp("HCLK");
		int delayEeprom = getDelayEeprom(hClk);
		int lowBKP = getLowBKP(hClk);

		Integer hse = getClockProp("HSE");
		int pllMull = getClockProp("CPU-C2-1.S") + 1;
		int cpuC1Sel = getClockProp("CPU-C1.S");

		int cpuC2Sel = getClockProp("CPU-C2.S");
		int cpuC3Sel = getClockProp("HCLK.S");
		int hclkSel = getClockProp("HCLK-1.S");


		switch (codeStep) {
			case 0:
				MDR_EEPROM.get().arr(cmnts[0]).set(oldCode);
				MDR_EEPROM.set(Param.CMD.set(delayEeprom).shift(3).opp("|")).end();
				break;
			case 1:
				MDR_RST_CLK.get().arr(cmnts[1]).set(oldCode);
				MDR_RST_CLK.set(Param.HS_CONTROL.set(1)).end(makeHzText(hse));
				MDR_RST_CLK.sete(Command.WHILE.set(Param.CLOCK_STATUS, "1 << 2", "== 0"), 2);
				break;
			case 2:
				MDR_RST_CLK.set(Param.CPU_CLOCK.seti(cpuC1Sel , 0)).end(hse);
				MDR_RST_CLK.sete(Param.PLL_CONTROL.add("(1 << 2) | (%s << 8)", (pllMull - 1)), pllMull);
				MDR_RST_CLK.sete(Command.WHILE.set(Param.CLOCK_STATUS, "0x02", "!= 0x02"));
				break;
			case 3:
				MDR_RST_CLK.set(Param.CPU_CLOCK.set(cpuC1Sel, cpuC2Sel, hclkSel, cpuC3Sel).shift(0, 2, 4, 8))
						.pre(1, 1, 2, 1).cmt("CPU_C1", "CPU_C2", "CPU_C3", "HCLK").build(oldCode);

				MDR_BKP.get().arr(cmnts[2]).set(oldCode);
				MDR_BKP.sete(Param.REG_0E.set(lowBKP, lowBKP).shift(0, 3));
				break;
		}
		return oldCode;
	}

	private int getDelayEeprom(int hClk) {
		int delayEeprom = hClk / 25_000_000;
		if (delayEeprom > 7) delayEeprom = 7;
		return delayEeprom;
	}

	@Override
	protected String[] methodNames() {
		return new String[]{"CPU", ""};
	}

	@Override
	public List<String> generateCode(Device device, List<String> oldCode, String methodName) {
		oldCode = Lists.newArrayList();
		Integer hClk = getClockProp("HCLK");
		int cpuC1Sel = getClockProp("CPU-C1.S");
		int cpuC2Sel = getClockProp("CPU-C2.S");
		boolean pllCheck = cpuC2Sel > 0;

		if (cpuC1Sel > 1) {//if (HSECheck.isSelected() || HSE2Check.isSelected()){
			generateCode(oldCode, 0);
			if (hClk > 80000000){
				g().addCodeStr(oldCode,"// warning Частота > 80 МГц: работа миксросхемы не гарантируется!!!");
			}
			generateCode(oldCode, 1);
		}

		if (pllCheck){
			generateCode(oldCode, 2);
		}

		generateCode(oldCode, 3);
		return super.generateCode(device, oldCode, methodName);
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

	private boolean clockInitialized = false;
	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.CPU);
		addModelProps(cpuProps, true);
//		addModelProps(new String[]{"bp_ucc", "bp_bucc"}, uccList, buccList);
		scene.addObserver("pinouts", this);
		fillClockGrid();
		clockInitialized = true;
		setupInitalClockValues(scene.getPinoutsModel().getClockModel());
		fillCpuConfProperties(scene);
		log.debug("#postInit - initialized");
	}

	private void updateCpuConfProperties() {
		for(String cpuProp: cpuProps) {
			McuBlockProperty mcuProp = getDevicePair().model().getProp(cpuProp);
			Integer clockProp = getClockProp(mcuProp.getMsgTxt());
			mcuProp.setStrValue(makeHzText(clockProp));
//			mcuProp.setIntValue(clockProp);
		}
	}
	private void fillCpuConfProperties(AppScene scene) {
		updateCpuConfProperties();
		if (scene == null || scene.isSetupInProcess()) return;
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
	}

	private void setupInitalClockValues(ClockModel clock) {
		clock.setInputs(new String[]{"HSI", "HSE", "LSI", "LSE"}, new int[]{8000000, 8000000, 40000, 32000});
		getScene().getPinoutsModel().loadClockParams();
		updateInputEditors("sp-k-INPUTS-30", "HSE");
		clock.addRestriction("USB-CLK", "= 48000000");
		clock.addRestriction("ADC-CLK", "< 14000000");
		clock.addRestriction("HCLK", "< 80000000");
		clock.addRestriction("CPU-CLK", "< 80000000");
		// disable HSE/2, HSI/2 for usb and cpu
		selectClockCBoxes("CPU-C1", 0, 30, 31, "IN-2", "/ 2","/ 2");
		selectClockCBoxes("USB-C1", 30, 31, "/ 2","/ 2");
		disableCBox("k-CPU-C1-30", true);
		disableCBox("k-CPU-C1-31", true);
		disableCBox("k-USB-C1-30", true);
		disableCBox("k-USB-C1-31", true);
		// make cpu default
		selectClockCBoxes("CPU-C2", 0, 30, "IN-2","* 2");
		selectClockCBoxes("HCLK", 0, 30, "IN-2","/ 2");
		// make usb 48 MHz
		selectClockCBoxes("USB-C2", 0, 9, 30, "IN-2", "/ 1", "* 6");
		//update pIN combos with grid's contented labels and combo items
		updatePInComboByGrid("k-USB-C1-0", "USB-C1");
		updatePInComboByGrid("k-ADC-C1-0", "ADC-C1");
		updatePInComboByGrid("k-ADC-C2-0", "ADC-C2");
	}

	@SuppressWarnings({"SameParameterValue", "unchecked"})
	private void updateInputEditors(String editorKey, String propName) {
		SpinnerValueFactory valueFactory = spnMap.get(editorKey).getValueFactory();
		valueFactory.setValue(getClockProp(propName) * 1.0);
		valueFactory.decrement(0);
	}

	private void selectClockCBoxes(String block, int i1, int i2, int i3, String v1, String v2, String v3) {
		selectClockCBoxes(block, i1, i2, v1, v2);
		selectCBox("k-" + block + "-" + i3, v3);
	}

	private void selectClockCBoxes(String block, int i1, int i2, String v1, String v2) {
		selectCBox("k-" + block + "-" + i1, v1);
		selectCBox("k-" + block + "-" + i2, v2);
	}

	private VBox makeImageBox() {
		ImageView iv = new ImageView("resourse/clock.png");
		iv.setFitWidth(360.0);
		iv.setFitHeight(180.0);
		return new VBox(iv);
	}

	@SuppressWarnings("unchecked")
	private void selectCBox(String comboKey, String value) {
		if (clkMap.isEmpty()) return;
		clkMap.get(comboKey).getSelectionModel().select(value);
	}
	private void disableCBox(String comboKey, boolean value) {
		if (clkMap.isEmpty()) return;
		clkMap.get(comboKey).setDisable(value);
	}

	private void makeClockGridItem(String caption, String combostr, int col, int row) {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		makePaddings(gridPane);
		addRowToGrid(gridPane, makeLabel(caption), 0, 0, 1);
		String exitStr = combostr.split("\\\\")[1];
		if (!exitStr.equals("Not")) {
			int splitLen = combostr.split("\\|").length;
			ObservableList cbItems = getScene().getSetsGenerator().genObsList(
					splitLen > 5 ? (splitLen > 9 ? clItem3 : clItem) : clItem2);
			ComboBox<String> box = new ComboBox<String>(cbItems);
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
		String prev = "";
		while (combos.length > i) {
			addComboToGrid(gridPane1, prefix, prev, combos[i], i % 4, i / 4);
			prev = combos[i++];
		}

		addRowToGrid(gridPane, gridPane1, 0, 1, span);
		return gridPane1;
	}

	private void setGridRowCol(GridPane gp, Node node, int row, int col) {
		GridPane.setColumnIndex(node, col);
		GridPane.setRowIndex(node, row);
		gp.getChildren().add(node);
	}
	private Region addComboToGrid(GridPane gridPane1, String prefix, String prev, String items, int col, int row) {
		if (items.startsWith("\\")) return null;
		String key = "k-" + prefix + "-" + col + row;
		Region cb = col % 2 == 0 ? makeLabel(items) : makeGridsCombo(items, key, changeCallback);
		if (cb instanceof ComboBox) {
//			GuiUtils.makeListener(key, (ComboBox) cb, changeCallback);//xtodo	makeListener
			clkMap.put(key, (ComboBox) cb);
//			cb = new HBox(cb, new Label(items));
		} else if (cb instanceof Spinner) {
			spnMap.put("sp-" + key, (Spinner) cb);
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

		if (subInd != 0 && subInd != 9 && !value.equals("null")) {
			String preKey = comboKey.substring(0, comboKey.lastIndexOf("-"));
			updatePInComboByGrid(preKey + "-0", preKey.substring(2));
//				zeroCBox.getItems().set(factors.indexOf(subInd), value);
		}
		ClockModel clock = getScene().getPinoutsModel().getClockModel();
		ComboBox comboBox = clkMap.get(comboKey);
		if (comboBox == null || comboBox.getSelectionModel() == null) {
			if (comboKey.startsWith("sp-k-")) {
				clock.setInput("HSE", (int)Math.round(Double.parseDouble(value)));
				clock.calc();
			}
			return;
		}
		int selIndex = comboBox.getSelectionModel().getSelectedIndex();
		if (clock == null) return;
		switch (subInd) {
			case 0 :
				clock.setSelected(subKey, selIndex);//Integer.parseInt(value.split("-")[1]) - 1);
				break;
			case 9 :
				clock.setFactor(subKey, "out", value, selIndex);
				break;
			default:
				clock.setFactor(subKey, factors.indexOf(subInd), value, selIndex);
				break;
		}
		clock.calc();
//		updateCpuConfProperties();
//		log_debug(log, clock.calc().toStr(subKey));
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		if (!clockInitialized) return;
		if (comboKey == null || value == null) return;
		// do not valid values for clock combo-boxes
		if (value.equals("null") || value.equals("RESET")) return;
		super.callGuiListener(comboKey, prev, value);
		log_debug(log, String.format("#callGuiListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		ComboBox comboBox = clkMap.get(comboKey);
		String preKey = comboKey.substring(0, comboKey.lastIndexOf("-"));
		String subKey = preKey.substring(2);
		String subSubKey = comboKey.substring(comboKey.lastIndexOf("-") + 1);
		// skip keys from mcuBlockProperty listener
		if (!subSubKey.matches("\\d+")) return;
		Integer subInd = Integer.parseInt(subSubKey);
		// 0, 10, 11, 12, 30, 31, 32
//		if (comboBox == null || comboBox.getSelectionModel() == null) return;
//		int selIndex = comboBox.getSelectionModel().getSelectedIndex();
//		if (subInd != 0 && subInd != 9) {
////			String preKey = comboKey.substring(0, comboKey.lastIndexOf("-"));
//			updatePInComboByGrid(preKey + "-0", preKey.substring(2));
////				zeroCBox.getItems().set(factors.indexOf(subInd), value);
//		}
		if (comboBox != null && comboBox.getSelectionModel() != null) {
			setupOnHoverStyle(bcDef, bcTxt, comboBox);
//		setupOnHoverStyle(newClr("dbe0b6"), comboBox);
			saveSelectedPin(comboKey, value);
		}
		ClockModel clock = getScene().getPinoutsModel().getClockModel();
		log_debug(log, String.format("#callGuiListener[%d] process clock with key [%s, %s] -> %s)", 0, subKey, subInd, value));
//		int row = subInd % 10; int col = subInd / 10;
//		int blockInd = 0;
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
		fillCpuConfProperties(getScene());
		if (value.trim().equals("LSE")) {
			makeFadeSplash(getScene());
		}
	}

	private void updatePInComboByGrid(String comboKey, String key) {
		log_debug(log, String.format("#updatePInComboByGrid(%s, %s)", comboKey, key));
		ComboBox comboBox = clkMap.get(comboKey);
		GridPane subGr = findGridFromGrid(clkBlocks.get(key), 1, 0);
		if (comboBox != null && subGr != null) {
			Map<Label, Node> labels = Maps.newLinkedHashMap();
			for(int i = 0; i < 4; i++) {
				Label lbl = findLabelFromGrid(subGr, i, 0);
				if (lbl != null) {
					labels.put(lbl, findNodeFromGrid(subGr, i, 1));
				}
				lbl = findLabelFromGrid(subGr, i, 2);
				if (lbl != null) labels.put(lbl, findNodeFromGrid(subGr, i, 3));
			}
			int selInd = comboBox.getSelectionModel().getSelectedIndex();
			int cind = 0;
			comboBox.getItems().clear();
			for(Label lbl: labels.keySet()) {
				Node suffHolder = labels.get(lbl);
				String suff = " ";
				if (suffHolder != null && suffHolder instanceof ComboBox) {
					ComboBox cb = (ComboBox) suffHolder;
					if (cb.getSelectionModel() != null) {
						suff += cb.getSelectionModel().getSelectedItem();
					}
				}

				log_debug(log, String.format("#updatePInComboByGrid(%s, %s) [%d] = %s", comboKey, key, cind++, lbl.getText() + suff));
				comboBox.getItems().add(lbl.getText() + suff);
			}
			comboBox.getSelectionModel().select(selInd > 0 ? selInd : 0);
		}
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
