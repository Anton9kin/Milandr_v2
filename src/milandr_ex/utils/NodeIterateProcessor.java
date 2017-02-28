package milandr_ex.utils;

import javafx.scene.Node;

/**
 * Created by lizard2k1 on 25.02.2017.
 */
public interface NodeIterateProcessor extends NodeIterateItemChecker {
	@Override
	default boolean check(String key, Node node) { return true; };

	public void process(String key, Node node);
}
