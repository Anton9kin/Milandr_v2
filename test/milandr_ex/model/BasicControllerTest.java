package milandr_ex.model;

import milandr_ex.data.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by lizard2k1 on 15.03.2017.
 */
public class BasicControllerTest {
	@Mock
	protected AppScene appScene;
	@Mock
	protected PinoutsModel pinoutsModel;
	@Mock
	protected McuBlockModel blockModel;
	@Mock
	protected ClockModel clockModel;
	protected Device device;
	protected ControllerTestCase testCase;

	protected void initController(BasicController controller) {
		MockitoAnnotations.initMocks(this);
		device = DeviceFactory.getDevice("LQFP64");
		CodeGenerator codeGenerator = CodeGenerator.instance();
		when(appScene.getPinoutsModel()).thenReturn(pinoutsModel);
		setGenKind(CodeGenerator.GenKind.MODEL);
		when(appScene.getCodeGenerator()).thenReturn(codeGenerator);
		when(pinoutsModel.getBlockModel(Mockito.anyString())).thenReturn(blockModel);
		when(pinoutsModel.getClockModel()).thenReturn(clockModel);
		controller.setScene(appScene);
		controller.postInit(appScene);
		testCase = new ControllerTestCase(controller.getDevicePair());
		testCase.setNames(getControllerParamNames());
	}

	protected String[] getControllerParamNames() {
		return new String[0];
	}

	protected ControllerTestCase getTestCase() {
		return testCase;
	}

	protected void setGenKind(CodeGenerator.GenKind kind) {
		when(appScene.genKind()).thenReturn(kind);
	}

	protected void testCodeResult(String expectedCodeResult, int expectedComments, int expectedLines,
								  int expectedSize, List<String> newCodeList) {
		StringBuilder codeResult = new StringBuilder();
		int commentsCount = 0;
		int linesCount = 0;
		for(String codeLine: newCodeList) {
			if (codeLine.trim().contains("//")) {
				commentsCount++;
				codeLine = codeLine.substring(0, codeLine.indexOf("//"));
			}
			if (codeLine.trim().isEmpty()) continue;
			linesCount++;
			codeResult.append(codeLine.trim().replaceAll("\\s", ""));
		}
		if (expectedComments > 0) assertEquals("expectedComments", expectedComments, commentsCount);
		if (expectedLines > 0) assertEquals("expectedLines", expectedLines, linesCount);
		if (expectedSize > 0) assertEquals("expectedSize", expectedSize, newCodeList.size());
		assertEquals("expectedCodeResult", expectedCodeResult, codeResult.toString());
	}

	public void buildResults(Map<String, String> params) {
		for(String key: params.keySet()) {
			String value = params.get(key);
			when(blockModel.getProp(eq(key))).thenReturn(buildProperty(key, value));
		}
	}

	private McuBlockProperty buildProperty(String name, final String value) {
		return new McuBlockProperty(name, McuBlockProperty.PropKind.INT){
			@Override
			public int getIntValue(int valueInd) {
				if (value.matches("\\d+")) {
					return Integer.parseInt(value);
				}
				return 0;
			}
			public String getStrValue(int valueInd) { return value; }
		};
	}
	private McuBlockProperty buildMockProperty(String value) {
		McuBlockProperty property = mock(McuBlockProperty.class);
		final int anInt = value.matches("\\d+") ? Integer.parseInt(value) : 0;

		when(property.getIntValue()).thenReturn(anInt);
		when(property.getStrValue()).thenReturn(value);
		when(property.getIntValue(anyInt())).thenReturn(anInt);
		when(property.getStrValue(anyInt())).thenReturn(value);
//		when(property.getProp(anyString())).thenReturn(property);
		return property;
	}
}
