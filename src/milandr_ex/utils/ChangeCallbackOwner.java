package milandr_ex.utils;

import javafx.scene.Node;

import java.util.Map;

/**
 * Created by lizard on 21.02.17 at 16:14.
 */
public interface ChangeCallbackOwner extends ChangeCallbackChecker {
	public Map<String, ? extends Node> nodeMap();
	public void callListener(String key, String prev, String value);
}
