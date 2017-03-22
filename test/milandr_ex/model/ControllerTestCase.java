package milandr_ex.model;

import com.google.common.collect.Maps;
import milandr_ex.data.Device;

import java.util.List;
import java.util.Map;

/**
 * Entity contains params and fields for testing cases
 * Created by lizard on 22.03.17 at 10:59.
 */
public class ControllerTestCase {
	private Device.EPairNames pair;
	private Map<String, String> params = Maps.newLinkedHashMap();
	private String expectedCodeResult;
	private int expectedComments;
	private int expectedLines;
	private int expectedSize;
	private String[] paramNames;
	private String[] paramValues;

	public ControllerTestCase(Device.EPairNames pair) {
		this.pair = pair;
	}

	public ControllerTestCase setNames(String[] paramNames) {
		this.paramNames = paramNames;
		return this;
	}

	public ControllerTestCase setValues(String[] paramValues) {
		this.paramValues = paramValues;
		return this;
	}

	public ControllerTestCase build(BasicControllerTest controller) {
		for(int i = 0; i < paramNames.length; i++) {
			this.addParam(paramNames[i], paramValues[i]);
		}
		controller.buildResults(params);
		return this;
	}

	public ControllerTestCase addParam(String name, String value) {
		params.put(name, value);
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
