package milandr_ex.utils;

import impl.org.controlsfx.skin.CheckComboBoxSkin;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import milandr_ex.data.Constants;
import org.controlsfx.control.CheckComboBox;

import java.util.Iterator;
import java.util.Map;

import static milandr_ex.data.Constants.keyToText;

/**
 * Created by lizard on 20.02.17 at 16:55.
 */
public class GuiUtils {
	public static Background backgroundDefault = null;
	public static Background backgroundIO = new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background backgroundPeriph = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));

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
			Platform.runLater(() -> {
				if (map.containsKey(kkey) && !map.get(kkey).isVisible()) return;
				callback.callListener(kkey, String.valueOf(t), String.valueOf(t1));
			});
		});
		callback.callListener(key, "null", "RESET");
	}

	public static void iterateComboMap(String pref, Map<String, String> pins, Map<String, ? extends Node> map) {
		for(String key: map.keySet()) {
			String pin = pins.get(key);
			if (pin == null) pin = pins.get(pref + key);
			if (pin == null) continue;
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
		Label label = new Label("   " + keyToText(key));
		label.setMinWidth(80.0);
		return label;
	}

	public static ComboBox<String> makeCombo(String pText) {
		ComboBox<String> newCombo = new ComboBox<>();
		newCombo.setBackground(backgroundDefault);
		newCombo.setPromptText(keyToText(pText));
		initItem(newCombo, 1);
		return newCombo;
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
		Region cb;
		if (items.matches("\\d+\\s[MK]Hz")) {
			int max = Integer.parseInt(items.substring(0, items.indexOf(' ')));
			cb = new Spinner<Integer>(2, max, 2);
		} else cb = !items.matches("[\\/\\*] \\d+") ? new TextField(items) :
				new ComboBox<String>(Constants.getDvMlItems(items));
		if (cb instanceof ComboBox) {
			((ComboBox) cb).getSelectionModel().selectLast();
		} else if (cb instanceof TextField) {
			((TextField) cb).setEditable(false);
			cb.setDisable(true);
		}
		cb.setPrefWidth(120.0);
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

	public static void switchObjects(String pref, Map<String, ? extends Node> nodeMap, boolean visible, boolean single){
		for(String comboKey: nodeMap.keySet()) {
			if (!comboKey.startsWith(pref)) continue;
			Node node = nodeMap.get(comboKey);
			node.setVisible(visible);
			((VBox)node).setMinHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setPrefHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
			((VBox)node).setMaxHeight(visible ? ((VBox) node).getChildren().size() * 25 : 0);
		}
	}

}
