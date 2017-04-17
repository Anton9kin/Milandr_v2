package milandr_ex.data;

import milandr_ex.utils.guava.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Some test for Pair's Sets generator
 * Created by lizard on 17.03.17 at 17:54.
 */
public class SetsGeneratorTest {
	private Set<String> portKeys = Sets.newHashSet();
	private SetsGenerator setsGenerator = new SetsGenerator(portKeys);
	@Before
	public void beforeTests() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 16; j++) {
				final String key = "cb" + (i) + (j < 10 ? "0" : "") + j;
				portKeys.add(key);
			}
		}
	}
	public void testGenLists(String setName, String skipName, String expectedArr) {
		List<String> arr = setsGenerator.genArrList(setName, skipName, true);
//		assertEquals(false, arr.isEmpty());
		assertEquals(expectedArr, arr.toString());
	}
	@Test
	public void testGenLists() {
		testGenLists("COMP-00", "", "[RESET, COMP1_IN1 PE2, COMP1_REF+ PE4]");
		testGenLists("COMP-01", "", "[RESET, COMP2_IN1 PE2, COMP2_IN2 PE3, COMP2_IN3 PE8, COMP2_REF- PE5]");
		testGenLists("COMP-02", "", "[RESET, COMP3_OUT PB8, COMP3_OUT PB11]");

//		testGenLists("COMP-00", "IN1", "[RESET, COMP1_IN1 PE2, COMP1_REF+ PE4]");
		testGenLists("COMP-01", "IN1", "[RESET, COMP2_IN2 PE3, COMP2_IN3 PE8, COMP2_REF- PE5]");

		testGenLists("COMP-00", "PE2", "[RESET, COMP1_IN1 PE2, COMP1_REF+ PE4]");
//		testGenLists("COMP-01", "PE2", "[RESET, COMP2_IN2 PE3, COMP2_IN3 PE8, COMP2_REF- PE5]");

		testGenLists("COMP-00", "COMP2_IN1", "[RESET, COMP1_REF+ PE4]");
//		testGenLists("COMP-01", "COMP2_IN1", "[RESET, COMP1_IN1 PE2]");
	}
}
