package milandr_ex.model;

import milandr_ex.data.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
}
