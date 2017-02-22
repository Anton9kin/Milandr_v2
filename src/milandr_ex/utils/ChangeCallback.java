package milandr_ex.utils;

import javafx.scene.Node;

import java.util.Map;

/**
 * Created by lizard on 21.02.17 at 10:50.
 */
public interface ChangeCallback {
	public Map<String, ? extends Node> nodeMap();
	public String listenChange(String key, Map<String, ? extends Node> nodeMap);
	public void callListener(String key, String prev, String value);
	public void callGuiListener(String key, String prev, String value);
}
