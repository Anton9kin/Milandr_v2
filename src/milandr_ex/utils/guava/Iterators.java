package milandr_ex.utils.guava;

import java.util.Iterator;

/**
 *
 * Created by lizard on 17.04.17 at 14:37.
 */
public class Iterators {
	public interface Checker {
		public boolean check(String i);
	}
	public static void removeIf(Iterator<String> iterator, Checker c) {
		while (iterator.hasNext()) {
			String item = iterator.next();
			if (c.check(item)) iterator.remove();
		}
	}
}
