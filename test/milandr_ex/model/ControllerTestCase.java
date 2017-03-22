package milandr_ex.model;

import com.google.common.collect.Maps;
import milandr_ex.data.Device;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Entity contains params and fields for testing cases
 * Created by lizard on 22.03.17 at 10:59.
 */
public class ControllerTestCase {
	private Device.EPairNames pair;
	private Map<String, String> params = Maps.newLinkedHashMap();
	private Map<String, String> clocks = Maps.newLinkedHashMap();
	private String expectedCodeResult;
	private int expectedComments;
	private int expectedLines;
	private int expectedSize;
	private String[] paramNames;
	private String[] paramValues;
	private String[] clockNames;
	private String[] clockValues;

	public ControllerTestCase(Device.EPairNames pair) {
		this.pair = pair;
	}

	public ControllerTestCase setNames(String[] paramNames) {
		this.paramNames = paramNames;
		String[] paramValues = new String[paramNames.length];
		Arrays.fill(paramValues, "0");
		setValues(paramValues);
		return this;
	}

	public ControllerTestCase setValues(String[] paramValues) {
		this.paramValues = paramValues;
		return this;
	}

	public ControllerTestCase setClockNames(String[] clockNames) {
		this.clockNames= clockNames;
		String[] clockValues = new String[clockNames.length];
		Arrays.fill(clockValues, "0");
		setClockValues(clockValues);
		return this;
	}

	public ControllerTestCase setClockValues(String[] clockValues) {
		this.clockValues = clockValues;
		return this;
	}

	public ControllerTestCase build(BasicControllerTest controller) {
		for(int i = 0; i < paramNames.length; i++) {
			this.addParam(paramNames[i], paramValues[i]);
		}
		for(int i = 0; i < clockNames.length; i++) {
			this.addClock(clockNames[i], clockValues[i]);
		}
		controller.buildResults(params, clocks);
		return this;
	}

	public ControllerTestCase addParam(String name, String value) {
		params.put(name, value);
		return this;
	}

	public ControllerTestCase addClock(String name, String value) {
		clocks.put(name, value);
		return this;
	}

	public Device.EPairNames getPair() {
		return pair;
	}

	public String getExpectedCodeResult() {
		return expectedCodeResult;
	}

	public ControllerTestCase setExpectedCodeResult(String expectedCodeResult) {
		this.expectedCodeResult = expectedCodeResult;
		return this;
	}

	public int getExpectedComments() {
		return expectedComments;
	}

	public ControllerTestCase setExpectedComments(int expectedComments) {
		this.expectedComments = expectedComments;
		return this;
	}

	public int getExpectedLines() {
		return expectedLines;
	}

	public ControllerTestCase setExpectedLines(int expectedLines) {
		this.expectedLines = expectedLines;
		return this;
	}

	public int getExpectedSize() {
		return expectedSize;
	}

	public ControllerTestCase setExpectedSize(int expectedSize) {
		this.expectedSize = expectedSize;
		return this;
	}

	public void test(BasicControllerTest controller, List<String> codeList) {
		test(controller, codeList, expectedCodeResult);
	}
	public void test(BasicControllerTest controller, List<String> codeList, String expectedCodeResult) {
		controller.testCodeResult(expectedCodeResult, expectedComments, expectedLines, expectedSize, codeList);
	}
}
