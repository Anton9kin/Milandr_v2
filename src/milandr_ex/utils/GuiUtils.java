package milandr_ex.utils;

import com.google.common.collect.Lists;
import impl.org.controlsfx.skin.CheckComboBoxSkin;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.Constants;
import org.controlsfx.control.CheckComboBox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.*;

/**
 * Created by lizard on 20.02.17 at 16:55.
 */
public class GuiUtils {
	public static Color bcDef = newClr("0x393939ff");
//	public static Color bcDef = newClr("0xf78218ff");
//	public static Color bcDef = newClr("0x859035ff");
	public static Color bcOk = Color.GREEN;
	public static Color bcIO = Color.GREENYELLOW;
	public static Color bcExt = Color.YELLOW;
	public static Color bcData = Color.CYAN;
	public static Color bcAddr = Color.ORANGE;
	public static Color bcAdc = Color.ORCHID;
	public static Color bcErr = Color.RED;
	public static Color bcOld = newClr("0xdbe0b6");
	public static Color bcTxt = newClr("0xf78218ff");
	public static Background backgroundDefault = null;
	public static Background backgroundIO = new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background backgroundPeriph = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background backgroundError = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
	public static String textStyleDef = "-fx-text-fill: black; -fx-font-size: 12; -fx-background-color: white;";
	public static String textStyleError = "-fx-text-fill: white; -fx-font-size: 12; -fx-background-color: red;";
	public static Map<String, Color> pinColors = new HashMap<String, Color>(){{
		put("ADC", bcAdc);
		put("ADDR", bcAddr);
		put("DATA", bcData);
		put("IO", bcIO);
	}};

	public static Color newClr(String hexColor) {
		if (hexColor.startsWith("#")) hexColor = hexColor.substring(1);
		if (hexColor.startsWith("0x")) hexColor = hexColor.substring(2);
		float r = Integer.parseInt(hexColor.substring(0, 2), 16) / 255f;
		float g = Integer.parseInt(hexColor.substring(2, 4), 16) / 255f;
		float b = Integer.parseInt(hexColor.substring(4, 6), 16) / 255f;
		float a = (float) (hexColor.length() < 8 ? 1.0 :
				(Integer.parseInt(hexColor.substring(6, 8), 16) / 255f));
		return new Color(r, g, b, a);
	}

	public static void setupOnHoverStyle(Color stdColor, Color txtColor, Node... nodes) {
		for(Node node: nodes) setupOnHoverStyle(node, stdColor, txtColor);
	}
	public static void setupOnHoverStyle(Node node, Color stdColor, Color txtColor) {
		if (stdColor == null || node == null) return;
		String stdStyle = cssStyleFromColor(stdColor, txtColor, false);
		if (!USE_HOVERED_STYLE) {
			node.setStyle(stdStyle);
			return;
		}
		String hoverStyle = cssStyleFromColor(stdColor, txtColor, true);
		String selectStyle1 = cssStyleFromColor(stdColor, txtColor, false, true);
		String selectStyle2 = cssStyleFromColor(stdColor, txtColor, true, true);
		String[] styles = new String[]{stdStyle, selectStyle1, hoverStyle, selectStyle2};
		BooleanProperty sp =  node instanceof ToggleButton ?
				((ToggleButton)node).selectedProperty() : node.visibleProperty();
		ReadOnlyBooleanProperty hp = node.hoverProperty();
		node.styleProperty().bind(Bindings.when(hp)
				.then(Bindings.when(sp).then(styles[0]).otherwise(styles[2]))
				.otherwise(Bindings.when(sp).then(styles[1]).otherwise(styles[3])));
	}

	public static void makeListener(final String key, Button newBtn, ChangeCallback callback) {
//		makeListener("b-" + key, newBtn.onMouseEnteredProperty(), callback);
		newBtn.setOnAction((event) -> {
			String kkey = "b-" + key;
			callback.callListener(kkey, "none", "click");
			Platform.runLater(() -> callback.callGuiListener(kkey, "none", "click"));
		});
	}
	public static void makeListener(final String key, TitledPane newPane, ChangeCallback callback) {
		makeListener("t-" + key, newPane.expandedProperty(), callback);
	}
	public static void makeListener(final String key, CheckBox newBox, ChangeCallback callback) {
		makeListener("c-" + key, newBox.selectedProperty(), callback);
	}
	public static void makeListener(final String key, ComboBox newCombo, ChangeCallback callback) {
		makeListener(key, newCombo.valueProperty(), callback);
	}
	public static void makeListener(final String key, CheckComboBox newCombo, ChangeCallback callback) {
		CheckComboBoxSkin skin = (CheckComboBoxSkin)newCombo.getSkin();
		if (skin != null) {
			ComboBox combo = (ComboBox)skin.getChildren().get(0);
			makeListener(key, combo.valueProperty(), callback);
		}
		makeListener(key, newCombo.getCheckModel().getCheckedItems(), callback);
	}

	@SuppressWarnings("unchecked")
	public static void makeListener(final String key, Observable property, ChangeCallback callback) {
		if (property instanceof ObservableValue) {
			((ObservableValue)property).addListener((ov, t, t1) -> {
				listenerBody(key, callback, t, t1);
			});
		} else if (property instanceof ObservableList) {
			((ObservableList)property).addListener((ListChangeListener) c -> {
					while(c.next()) listenerBody(key, callback, "check", c.getList());});
		}
		callback.callListener(key, "null", "RESET");
		Platform.runLater(() -> callback.callGuiListener(key, "null", "RESET"));
	}

	private static void listenerBody(String key, ChangeCallback callback, Object t, Object t1) {
		Map<String, ? extends Node> map = callback.nodeMap();
		String kkey = callback.listenChange(key, map);
		if (kkey == null) return;
		callback.callListener(kkey, String.valueOf(t), String.valueOf(t1));
		Platform.runLater(() -> {
			if (map != null && map.containsKey(kkey) && !map.get(kkey).isVisible()) return;
			callback.callGuiListener(kkey, String.valueOf(t), String.valueOf(t1));
		});
	}

	public static void iterateComboMap(String pref, Map<String, String> pins, Map<String, ? extends Node> map) {
		iterateComboMap(pref, pins, map, false);
	}
	public static void iterateComboMap(String pref, Map<String, String> pins, Map<String, ? extends Node> map, boolean skipReset) {
		for(String key: map.keySet()) {
			String pin = pins.get(key);
			if (pin == null) pin = pins.get(pref + key);
			if (pin == null) continue;
			if (skipReset && pin.equals("RESET")) continue;
			Node node = map.get(key);
			if (node instanceof ComboBox) //noinspection unchecked
				((ComboBox)node).getSelectionModel().select(pin);
			if (node instanceof CheckBox) //noinspection unchecked
				((CheckBox)node).setSelected(pin.equals("true"));
			if (node instanceof TitledPane) //noinspection unchecked
				((TitledPane)node).setExpanded(pin.equals("true"));
		}
	}

	public static void setMinPrefHW(Region node, double minPrefH, double minPrefW) {
		setMinPrefHeight(node, minPrefH);
		setMinPrefWidth(node, minPrefW);
	}
	public static void setMinPrefInfHW(Region node, double minPrefH, double minPrefW) {
		setMinPrefInfHeight(node, minPrefH);
		setMinPrefInfWidth(node, minPrefW);
	}
	public static void setMinPrefInfHeight(Region node, double minPref) {
		setMinPrefMaxHeight(node, minPref, minPref, Double.MAX_VALUE);
	}
	public static void setMinPrefMaxHeight(Region node, double minPrefMax) {
		setMinPrefMaxHeight(node, minPrefMax, minPrefMax);
	}
	public static void setMinPrefHeight(Region node, double minPref) {
		setMinPrefMaxHeight(node, minPref, 0.0);
	}
	public static void setMinPrefMaxHeight(Region node, double minPref, double max) {
		setMinPrefMaxHeight(node, minPref, minPref, max);
	}
	public static void setMinPrefMaxHeight(Region node, double min, double pref, double max) {
		node.setMinHeight(min);
		node.setPrefHeight(pref);
		if (max > 0.0) node.setMaxHeight(max);
	}
	public static void setMinPrefInfWidth(Region node, double minPref) {
		setMinPrefMaxWidth(node, minPref, minPref, Double.MAX_VALUE);
	}
	public static void setMinPrefMaxWidth(Region node, double minPrefMax) {
		setMinPrefMaxWidth(node, minPrefMax, minPrefMax);
	}
	public static void setMinPrefWidth(Region node, double minPref) {
		setMinPrefMaxWidth(node, minPref, 0.0);
	}
	public static void setMinPrefMaxWidth(Region node, double minPref, double max) {
		setMinPrefMaxWidth(node, minPref, minPref, max);
	}
	public static void setMinPrefMaxWidth(Region node, double min, double pref, double max) {
		node.setMinWidth(min);
		node.setPrefWidth(pref);
		if (max > 0.0) node.setMaxWidth(max);
	}
	public static void setAnchors(double topBottomLeftRight, Node... nodes) {
		for(Node node: nodes) {
			if (node == null) return;
			setAnchors(node, topBottomLeftRight);
			setAnchors(node.getParent(), topBottomLeftRight);
		}
	}
	public static void setAnchors(Node node, double topBottomLeftRight) {
		setAnchors(node, topBottomLeftRight, topBottomLeftRight);
	}
	public static void setAnchors(Node node, double topBottom, double leftRight) {
		setAnchors(node, topBottom, topBottom, leftRight, leftRight);
	}
	public static void setAnchors(Node node, double top, double bottom, double left, double right) {
		if (node == null) return;
		node.setLayoutX(left);
		node.setLayoutY(top);
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
		AnchorPane.setRightAnchor(node, right);
	}


	public static void initItem(Region node, int k) {
		node.setVisible(Boolean.TRUE);
		node.setMinWidth(100.0);
		node.setPrefWidth(100.0);
		node.setMaxWidth(100000.0);
		node.setMinHeight(25 * k);
		node.setPrefHeight(25 * k);
	}

	public static Label makeLabel(String key) {
		Label label = new Label(" " + keyToText(key));
		label.setMinWidth(60.0);
		return label;
	}

	public static ComboBox<String> makeCombo(String pText, boolean newStyle) {
		ComboBox<String> newCombo = new ComboBox<>();
		newCombo.setId(pText);
//		newCombo.setBackground(backgroundDefault);
		newCombo.setPromptText(keyToText(pText));
		if (pText.startsWith("cb")) makeListCell(newCombo);
		if (newStyle) setupOnHoverStyle(bcDef, bcTxt, newCombo);
		initItem(newCombo, 1);
		return newCombo;
	}

	private static void makeListCell(ComboBox newCombo) {
		newCombo.setButtonCell(new ListCell(){
			@Override
			protected void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				if(empty || item==null){
					// styled like -fx-prompt-text-fill:
//					setStyle("-fx-text-fill: derive(-fx-control-inner-background,-30%)");
					setStyle("-fx-text-fill: " + toRGBCode(bcTxt));
				} else {
					setStyle("-fx-text-fill: -fx-text-inner-color");
					setText(item.toString());
				}
			}
		});
	}

	public static TextField findTextFromGrid(GridPane grid, int row, int col) {
		if (grid == null) return null;
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof TextField ? (TextField)node : null;
	}
	public static HBox findHBTextFromGrid(GridPane grid, int row, int col) {
		if (grid == null) return null;
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof HBox? (HBox) node : null;
	}
	public static Label findLabelFromGrid(GridPane grid, int row, int col) {
		if (grid == null) return null;
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof Label ? (Label)node : null;
	}
	public static GridPane findGridFromGrid(GridPane grid, int row, int col) {
		if (grid == null) return null;
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof GridPane ? (GridPane)node : null;
	}
	public static Node findNodeFromGrid(GridPane grid, int row, int col) {
		if (grid == null) return null;
		if (row < 0 || col < 0) return null;
		Node input = null;
		for (Node node : grid.getChildren()) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				input = node;
				break;
			}
		}
		return input;
	}

	public static Region makeGridsCombo(String items) {
		return makeGridsCombo(items, "", null);
	}
	public static Region makeGridsCombo(String items, String key, ChangeCallback callBack) {
		Region cb;
		if (items.matches("\\d+\\s[MK]Hz")) {
			int max = Integer.parseInt(items.substring(0, items.indexOf(' ')));
			cb = new Spinner<Integer>(2, max, 2);
		} else cb = !items.matches("[\\/\\*] \\d+") ? new TextField(items) :
				new ComboBox<String>(Constants.getDvMlItems(items));
		if (cb instanceof ComboBox) {
			((ComboBox) cb).getSelectionModel().selectFirst();
			if (!key.isEmpty()) makeListener(key, (ComboBox) cb, callBack);
		} else if (cb instanceof TextField) {
			((TextField) cb).setEditable(false);
			cb.setDisable(true);
		}
		cb.setPrefWidth(80.0);
//		cb.setPrefWidth(cb instanceof ComboBox ? 120.0 : 80.0);
		return cb;
	}

	public static void addRowToGrid(GridPane gridPane, Node label, int row) {
		addRowToGrid(gridPane, label, 0, row, 1);
	}
	public static void addColToGrid(GridPane gridPane, Node label, int col) {
		addRowToGrid(gridPane, label, col, 0, 1);
	}
	public static void addRowToGrid(GridPane gridPane, Node label, int col, int row, int span) {
		GridPane.setColumnIndex(label, col);
		GridPane.setRowIndex(label, row);
		GridPane.setColumnSpan(label, span);
		gridPane.getChildren().add(label);
	}
	public static void makePaddings(GridPane gridPane) {
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		GridPane.setMargin(gridPane, new Insets(10.0, 10.0, 10.0, 10.0));
	}
	public static void switchObjects(Integer[] idx, Map<String, ? extends Node> nodeMap, boolean visible){
		switchObjects(idx, nodeMap, visible, false);
	}
	public static void switchObjects(Integer[] idx, Map<String, ? extends Node> nodeMap, boolean visible, boolean single){
		Iterator<String> nit = nodeMap.keySet().iterator();
		while (nit.hasNext()) {
			String comboKey = nit.next();
			if (!comboKey.matches("cb\\d+")) continue;
			Node node = nodeMap.get(comboKey);
			boolean outOfRange = single;
			for(int i = 0; i < idx.length; i++) {
				String comboNum = comboKey.substring(2);
				if (!comboNum.startsWith("" + (idx[i] / 100))) continue;
				int parse = Integer.parseInt(comboNum.substring(1));
				outOfRange = single ? parse != (idx[i] % 100) : parse < (idx[i] % 100);
				if (single && outOfRange) continue;
				if (single || outOfRange) break;
			}
			if (outOfRange) continue;
			node.setVisible(visible);
		}
	}

	public static void uncheckObjects(String pref, Map<String, ? extends Node> nodeMap) {
		iterateObjects(pref, nodeMap, (key, node) -> ((ComboBox) node).getSelectionModel().select("RESET"));
	}
	public static void selectAdcObjects(String pref, Map<String, ? extends Node> nodeMap, String inValue) {
		if (inValue == null) return;
		List<String> checks = Lists.newArrayList(inValue.replaceAll("[\\s\\[\\]]","").split(","));
		selectObjects(pref, nodeMap, key -> true,
				(key, node) -> key != null && key.startsWith("ADC"),
				(key, node) -> key != null && checks.contains(key.substring(3,4)));
	}
	public static void selectObjects(String pref, Map<String, ? extends Node> nodeMap, boolean inValue) {
		selectObjects(pref, nodeMap, null, null, inValue);
	}
	public static void selectObjects(String pref, Map<String, ? extends Node> nodeMap, NodeIterateKeyChecker keyChecker,
									 NodeIterateItemChecker itemChecker, boolean inValue) {
		selectObjects(pref, nodeMap, keyChecker, itemChecker, (k, n) -> inValue);
	}
	public static void selectObjects(String pref, Map<String, ? extends Node> nodeMap, NodeIterateKeyChecker keyChecker,
									 NodeIterateItemChecker itemChecker, final NodeIterateItemChecker valueChecker) {
		if (keyChecker == null) keyChecker = key -> {
			String text = keyToText(key);
			return !text.startsWith("PC") && !text.startsWith("PD");
		};
		if (itemChecker == null) itemChecker = (key, node) -> key != null &&
				(key.startsWith("DATA") || key.startsWith("ADDR"));
		final NodeIterateKeyChecker cheker = keyChecker;
		final NodeIterateItemChecker checker = itemChecker;
		iterateObjects(pref, nodeMap, new NodeIterateProcessor() {
			@Override
			public boolean check(String key, Node node) {
				return cheker.check(key);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void process(String key, Node node) {
				List<String> items =  ((ComboBox) node).getItems();
				for(String item: items) {
					if (checker.check(item, node)) {
						SingleSelectionModel model = ((ComboBox) node).getSelectionModel();
						String selItem = (String) model.getSelectedItem();
						if (valueChecker.check(item, node)) {
							if (selItem == null || selItem.equals("RESET")) {
								model.select(item);
							}
						} else if (checker.check(selItem, node)) {
							model.select("RESET");
						}
					}
				}
			}
		});
	}
	public static void switchObjects(String pref, Map<String, ? extends Node> nodeMap,
									 boolean visible, boolean single){
		iterateObjects(pref, nodeMap, (key, node) -> {
			node.setVisible(visible);
			((VBox)node).setMinHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setPrefHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setMaxHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
		});
	}
	public static void iterateObjects(String pref, Map<String, ? extends Node> nodeMap,
									  NodeIterateProcessor processor){
		for(String comboKey: nodeMap.keySet()) {
			Node node = nodeMap.get(comboKey);
			if (!comboKey.startsWith(pref)) {
				if (node instanceof ComboBox) {
					SelectionModel model = ((ComboBox) node).getSelectionModel();
					if (model == null) continue;
					String item = (String)model.getSelectedItem();
					if (item == null || !textToKey(item).contains(pref)) continue;
				} else continue;
			}
			if (!processor.check(comboKey, node)) continue;
			processor.process(comboKey, node);
		}
	}

}
