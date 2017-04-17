package milandr_ex.utils.guava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by lizard on 17.04.17 at 14:24.
 */
public class Lists {
	public static <T> ArrayList<T> newArrayList(List<T> items) {
		return new ArrayList<T>(items);
	}
	public static <T> ArrayList<T> newArrayList(T... items) {
		ArrayList<T> ts = new ArrayList<T>();
		Collections.addAll(ts, items);
		return ts;
	}
}
