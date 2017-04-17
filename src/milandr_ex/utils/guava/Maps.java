package milandr_ex.utils.guava;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Created by lizard on 17.04.17 at 14:22.
 */
public class Maps {
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	public static <K, V> HashMap<K, V> newHashMap(Map<K, V> items) {
		return new HashMap<K, V>(items);
	}
	public static <K, V> HashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}
}
