package milandr_ex.utils;

import javafx.scene.Node;

import java.util.Map;

/**
 * Created by lizard on 21.02.17 at 12:26.
 */
public abstract class ChangeCallBackImpl implements ChangeCallback {
	protected ChangeCallbackOwner checker;
	private Map<String, ? extends Node> nodeMap;

	public ChangeCallBackImpl(ChangeCallbackOwner owner) {
		this.checker = owner;
		this.nodeMap = owner.nodeMap();
	}

	@Override
	public Map<String, ? extends Node> nodeMap() {
		return nodeMap;
	}

	@Override
	public String listenChange(String key, Map<String, ? extends Node> nodeMap) {
		if (checker.check()) return null;
		return key;
	}
}
