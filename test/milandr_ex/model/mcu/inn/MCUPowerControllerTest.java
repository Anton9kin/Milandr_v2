package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import milandr_ex.model.BasicControllerTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for verify result of code generation for Power block
 * Created by lizard2k1 on 15.03.2017.
 */
public class MCUPowerControllerTest extends BasicControllerTest {
	private static String defaultCodeResult = "voidPWR_init(void){MDR_RST_CLK->PER_CLOCK|=((1<<11));MDR_POWER->PVDCS=((0<<3)|(0<<1));}";

	private MCUPowerController powerController;

	@Before
	public void initController() {
		powerController = new MCUPowerController();
		super.initController(powerController);
	}
	@Test
	public void testDefaultPowerParams() {
		List<String> oldCodeList = Lists.newArrayList();
		assertEquals("[]", String.valueOf(oldCodeList));
		List<String> newCodeList = powerController.generateCode(device, oldCodeList);
		assertNotEquals(oldCodeList, newCodeList);
		testCodeResult(defaultCodeResult, 6, 5, 12, newCodeList);
	}
}
