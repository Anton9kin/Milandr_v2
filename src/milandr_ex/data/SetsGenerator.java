package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static milandr_ex.data.Constants.comboTexts;
import static milandr_ex.data.Constants.keyToText;

/**
 * Created by lizard2k1 on 20.02.2017.
 */
public class SetsGenerator {
	private Set<String> portKeys;
	public SetsGenerator(Set<String> portKeys) {
		this.portKeys = portKeys;
	}

	private static Map<String, List<String>> pairItems = Maps.newHashMap();
	private static Map<String, Integer[]> pairNumbers = Maps.newHashMap();
	public ObservableList<String> genObsList(String setName) {
		return genObsList(setName, false);
	}
	public ObservableList<String> genObsList(String setName, boolean cust) {
		if (cust && setName.startsWith("ADC")) {
			List<String> adcList = Lists.newArrayList();
			for(int i=0; i < 16; i++) adcList.add(i + "");
			return genObsList(adcList);
		}
		return genObsList(setName, "");
	}
	public ObservableList<String> genObsList(String setName, String skipName) {
		return FXCollections.observableArrayList(genLists(setName, skipName));
	}
	public ObservableList<String> genObsList(List<String> items) {
		return FXCollections.observableArrayList(items);
	}
	public String[] genLists(String setName, String skipName) {
		generateSets(setName, skipName);
		return pairItems.get(setName).toArray(new String[]{});
	}
	public Integer[] genNumbers(String setName) {
		generateSets(setName, "");
		return pairNumbers.get(setName);
	}
	private void generateSets(String setName, String skipName) {
		if (!pairItems.containsKey(setName)) {
			List<String> pairs = genOnePairList(setName, skipName);
			pairItems.put(setName, pairs);
		} else if (!skipName.isEmpty()) {
			List<String> items = pairItems.get(setName);
			items.clear();
			items.addAll(genOnePairList(setName, skipName));
		}
	}

	private List<String> genOnePairList(String setName, String skipName) {
		List<String> pairs = Lists.newArrayList();
		List<Integer> numbers = Lists.newArrayList();
		String sName = setName;
		if (!sName.startsWith("ADC")) {
			fillInputs(pairs, numbers, sName, skipName);
		}
		Collections.sort(pairs);
		pairs.add(0, "RESET");
		pairNumbers.put(setName, numbers.toArray(new Integer[]{}));
		return pairs;
	}

	private void fillInputs(List<String> pairs, List<Integer> numbers, String sName, String skipName) {
		if (sName.startsWith("SPI")) sName = "SSP" + (sName.length() > 3 ? sName.substring(3) : "");
		if (sName.startsWith("COMP")) sName = "COMP";
		if (sName.startsWith("I2C")) {
			sName = "SDA" + (sName.length() > 3 ? sName.substring(3) : "");
			findInputs(pairs, numbers, sName, skipName);
			sName = "SCL" + (sName.length() > 3 ? sName.substring(3) : "");
		}
		findInputs(pairs, numbers, sName, skipName);
	}

	private void findInputs(List<String> pairs, List<Integer> numbers, String sName, String skipName) {
		String[] skips = skipName.split(",");
		for(int i=0; i < comboTexts.length; i++) {
			int div = i % 16;
			String cbKey = "cb" + (i / 16) + (div > 9 ? "" : "0") + div;
			if (!portKeys.contains(cbKey)) continue;
			int idx = -1;
			String str = "";
			String[] comboItems = comboTexts[i];
			for(String cItem: comboItems) {
				if (cItem.contains(sName)) {
					idx = i;
					str = cItem;
					break;
				}
			}
			if (idx < 0 || str.isEmpty() || str.equals("-")) continue;
			boolean skipIt = false;
			for(String skp: skips) skipIt |= str.equals(skp);
			if (skipIt) continue;
			if (idx >= 0) {
//                String sId = str;//"" + idx;
				String sId = str + " " + keyToText(cbKey);
				if (!pairs.contains(sId)) pairs.add(sId);
				numbers.add(idx);
			}
		}
	}
}
