package milandr_ex.utils;

import impl.org.controlsfx.skin.CheckComboBoxSkin;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.Constants;
import org.controlsfx.control.CheckComboBox;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static milandr_ex.data.Constants.cssStyleFromColor;
import static milandr_ex.data.Constants.keyToText;
import static milandr_ex.data.Constants.textToKey;

/**
 * Created by lizard on 20.02.17 at 16:55.
 */
public class GuiUtils {
	public static Color bcDef = newClr("0x859035ff");
	public static Color bcOk = Color.GREEN;
	public static Color bcIO = Color.GREENYELLOW;
	public static Color bcExt = Color.YELLOW;
	public static Color bcErr = Color.RED;
	public static Background backgroundDefault = null;
	public static Background backgroundIO = new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background backgroundPeriph = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background backgroundError = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
	public static String textStyleDef = "-fx-text-fill: black; -fx-font-size: 12; -fx-background-color: white;";
	public static String textStyleError = "-fx-text-fill: white; -fx-font-size: 12; -fx-background-color: red;";

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

	public static void setupOnHoverStyle(Color stdColor, Node... nodes) {
		for(Node node: nodes) setupOnHoverStyle(node, stdColor);
	}
	public static void setupOnHoverStyle(Node node, Color stdColor) {
		if (stdColor == null) return;
		setupOnHoverStyle(node, cssStyleFromColor(stdColor),
				cssStyleFromColor(stdColor, true));
	}
	public static void setupOnHoverStyle(Node node, String stdStyle, String hoverStyle) {
		if (node == null) return;
		node.styleProperty().unbind();
		node.styleProperty().bind(Bindings.when(node.hoverProperty())
				.then(new SimpleStringProperty(hoverStyle))
				.otherwise(new SimpleStringProperty(stdStyle))
		);
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
		if (skin == null) return;
		ComboBox combo = (ComboBox)skin.getChildren().get(0);
		makeListener(key, combo.valueProperty(), callback);
	}
	public static void makeListener(final String key, ReadOnlyProperty property, ChangeCallback callback) {
		property.addListener((ov, t, t1) -> {
			Map<String, ? extends Node> map = callback.nodeMap();
			String kkey = callback.listenChange(key, map);
			if (kkey == null) return;
			callback.callListener(kkey, String.valueOf(t), String.valueOf(t1));
			Platform.runLater(() -> {
				if (map.containsKey(kkey) && !map.get(kkey).isVisible()) return;
				callback.callGuiListener(kkey, String.valueOf(t), String.valueOf(t1));
			});
		});
		callback.callListener(key, "null", "RESET");
		Platform.runLater(() -> callback.callGuiListener(key, "null", "RESET"));
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
//		newCombo.setBackground(backgroundDefault);
		newCombo.setPromptText(keyToText(pText));
		if (newStyle) setupOnHoverStyle(bcDef, newCombo);
		initItem(newCombo, 1);
		return newCombo;
	}

	public static TextField findTextFromGrid(GridPane grid, int row, int col) {
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof TextField ? (TextField)node : null;
	}
	public static HBox findHBTextFromGrid(GridPane grid, int row, int col) {
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof HBox? (HBox) node : null;
	}
	public static Label findLabelFromGrid(GridPane grid, int row, int col) {
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof Label ? (Label)node : null;
	}
	public static GridPane findGridFromGrid(GridPane grid, int row, int col) {
		Node node = findNodeFromGrid(grid, row, col);
		return node instanceof GridPane ? (GridPane)node : null;
	}
	public static Node findNodeFromGrid(GridPane grid, int row, int col) {
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
			((ComboBox) cb).getSelectionModel().selectLast();
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
	public static void selectObjects(String pref, Map<String, ? extends Node> nodeMap, boolean inValue) {
		iterateObjects(pref, nodeMap, new NodeIterateProcessor() {
			@Override
			public boolean check(String key, Node node) {
				String text = keyToText(key);
				if (text.startsWith("PC")) return false;
				if (text.startsWith("PD")) return false;
				return true;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void process(String key, Node node) {
				List<String> items =  ((ComboBox) node).getItems();
				for(String item: items) {
					if (isaDataOrAddr(item)) {
						SingleSelectionModel model = ((ComboBox) node).getSelectionModel();
						String selItem = (String) model.getSelectedItem();
						if (inValue) {
							if (selItem == null || selItem.equals("RESET")) {
								model.select(item);
							}
						} else if (selItem != null && isaDataOrAddr(selItem)) {
							model.select("RESET");
						}
					}
				}
			}

			private boolean isaDataOrAddr(String item) {
				return item.startsWith("DATA") || item.startsWith("ADDR");
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
