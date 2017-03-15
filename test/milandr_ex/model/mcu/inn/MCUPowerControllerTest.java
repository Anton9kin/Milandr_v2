package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import milandr_ex.data.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Test class for verify result of code generation for Power block
 * Created by lizard2k1 on 15.03.2017.
 */
public class MCUPowerControllerTest {
	private static String defaultCodeResult = "void PWR_init( void ){MDR_RST_CLK->PER_CLOCK |= ((1 << 11));MDR_POWER->PVDCS = ((0 << 3)| (0 << 1));}";

	@Mock
	private AppScene appScene;
	@Mock
	private PinoutsModel pinoutsModel;
	@Mock
	private McuBlockModel blockModel;
	@Mock
	private ClockModel clockModel;
	private Device device;

	private CodeGenerator codeGenerator;
	private MCUPowerController powerController;

	@Before
	public void initController() {
		MockitoAnnotations.initMocks(this);
		codeGenerator = CodeGenerator.instance();
		powerController = new MCUPowerController();
		powerController.setScene(appScene);
		powerController.postInit(appScene);
		device = DeviceFactory.getDevice("LQFP64");
		when(appScene.getPinoutsModel()).thenReturn(pinoutsModel);
		when(appScene.genKind()).thenReturn(CodeGenerator.GenKind.MODEL);
		when(appScene.getCodeGenerator()).thenReturn(codeGenerator);
		when(pinoutsModel.getBlockModel(Mockito.anyString())).thenReturn(blockModel);
		when(pinoutsModel.getClockModel()).thenReturn(clockModel);
	}
	@Test
	public void testDefaultPowerParams() {
		List<String> oldCodeList = Lists.newArrayList();
		assertEquals("[]", String.valueOf(oldCodeList));
		List<String> newCodeList = powerController.generateCode(device, oldCodeList);
		assertNotEquals(oldCodeList, newCodeList);
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
			codeResult.append(codeLine.trim());
		}
		assertEquals(6, commentsCount);
		assertEquals(5, linesCount);
		assertEquals(12, newCodeList.size());
		assertEquals(defaultCodeResult, codeResult.toString());
	}
}
