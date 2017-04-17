package milandr_ex.utils.guava;

import java.util.Arrays;

/**
 *
 * Created by lizard on 17.04.17 at 14:24.
 */
public class Objects {
	public static boolean equal(Object o1, Object o2) {
		return o1 != null && o1.equals(o2);
	}
	public static int hashCode(Object... o) {
		return Arrays.hashCode(o);
	}
}
