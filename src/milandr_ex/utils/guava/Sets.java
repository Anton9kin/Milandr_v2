package milandr_ex.utils.guava;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * Created by lizard on 17.04.17 at 14:31.
 */
public class Sets {
	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}
	public static <T> Set<T> newHashSet(Collection<T> items) {
		return new HashSet<T>(items);
	}
	public static <T> Set<T> newLinkedHashSet() {
		return new LinkedHashSet<T>();
	}
}
