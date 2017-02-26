package milandr_ex.model.mcu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import milandr_ex.data.AppScene;
import milandr_ex.data.ClockModel;
import milandr_ex.data.Constants;
import milandr_ex.data.PinoutsModel;
import milandr_ex.model.BasicController;
import milandr_ex.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.*;
import static milandr_ex.utils.GuiUtils.*;
import static milandr_ex.utils.GuiUtils.textStyleError;

/**
 * Created by lizard2k1 on 24.02.2017.
 */
public class MCUClockController extends BasicController
		implements PinoutsModel.Observer {
	private static final Logger log	= LoggerFactory.getLogger(MCUClockController.class);

	private Map<String, ComboBox> clkMap = Maps.newHashMap();
	private Map<String, GridPane> clkBlocks = Maps.newLinkedHashMap();
	private GridPane clckCont;

	public MCUClockController() {
	}

	public void setClckCont(GridPane clckCont) {
		this.clckCont = clckCont;
	}

	@Override
	protected void postInit(AppScene scene) {
		scene.addObserver("pinouts", this);
		fillClockGrid();
	}

	@Override
	public void observe(PinoutsModel pinoutsModel) {
		if (pinoutsModel == null) return;
		Map<String, String> pins = pinoutsModel.getSelectedPins();
		iterateComboMap("k-", pins, clkMap, true);
	}

	private void fillClockGrid() {
		clckCont.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
		makePaddings(clckCont);
		String[][] combostrs = Constants.combostrs;
		List<ClockModel.Block> blocks = Lists.newArrayList();
		for(int i = 0; i < combostrs.length - 1; i++) {
			for(int j = 0; j < combostrs[i].length; j++) {
				String namestr = combostrs[i][j];
				if (namestr.isEmpty()) continue;
				String combostr = combostrs[combostrs.length - 1][i * combostrs[i].length + j];
				makeClockGridItem(namestr, combostr, j, i);
				blocks.add(ClockModel.Block.get(namestr, combostr));
			}
		}
		PinoutsModel model = getScene().getPinoutsModel();
		ClockModel clock = ClockModel.get(model.getSelectedBody(), blocks);
		model.setClockModel(clock);
		clock.setInputs(new String[]{"HSI", "HSE", "LSI", "LSE"}, new int[]{8000000, 8000000, 40000, 32000});
		clock.addRestriction("USB-C2", "= 48000000");
		clock.addRestriction("ADC-C2", "< 14000000");
		clock.addRestriction("CPU-C3", "< 80000000");
		// make usb 48 MHz
		selectCBox("k-USB-C2-0", "IN-2");
		selectCBox("k-USB-C2-9", "/ 1");
		selectCBox("k-USB-C2-30", "* 6");
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
			ComboBox<String> box = new ComboBox<>(splitLen > 5 ? (splitLen > 7 ? clItem3 : clItem) : clItem2);
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
		log_debug(log, String.format("#callListener[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		String subKey = comboKey.substring(2, comboKey.lastIndexOf("-"));
		Integer subInd = Integer.parseInt(comboKey.substring(comboKey.lastIndexOf("-") + 1));
		ClockModel clock = getScene().getPinoutsModel().getClockModel();
		if (clock == null) return;
		if (subInd == 0) clock.setSelected(subKey, Integer.parseInt(value.split("-")[1]) - 1);
		else if (subInd == 9) clock.setFactor(subKey, "out", value);
		else clock.setFactor(subKey, factors.indexOf(subInd), value);
		log_debug(log, clock.calc().toStr(subKey));
	}

	@Override
	public void callGuiListener(String comboKey, String prev, String value) {
		if (comboKey == null) return;
		if (value != null && !value.equals("null") && !value.equals("RESET")) {
			log_debug(log, String.format("#switchComboIndex[%d](%s, %s -> %s)", 0, comboKey, prev, value));
		}
		ComboBox comboBox = clkMap.get(comboKey);
		String subKey = comboKey.substring(2, comboKey.lastIndexOf("-"));
		Integer subInd = Integer.parseInt(comboKey.substring(comboKey.lastIndexOf("-") + 1));
		// 0, 10, 11, 12, 30, 31, 32
		if (comboBox == null || comboBox.getSelectionModel() == null) return;
		comboBox.setStyle(cssStyleFromColor(newClr("dbe0b6")));
		int selIndex = comboBox.getSelectionModel().getSelectedIndex();
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
						boolean isFailed = checkClockRestrictions(restr, pinOut);
						textF.setStyle(cssStyleFromColor(isFailed ? newClr("dbe0b6") : Color.RED));
//						textF.setStyle(isFailed ? textStyleDef : textStyleError);
//						textF.setBackground(isFailed ? backgroundDefault : backgroundError);
//						Color backColor = isFailed ? Color.ALICEBLUE : Color.INDIANRED;
//						textF.setStyle("-fx-background-color: " + Constants.toRGBCode(backColor) + ";");
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
