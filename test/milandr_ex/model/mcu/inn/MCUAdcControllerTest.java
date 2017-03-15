package milandr_ex.model.mcu.inn;

import com.google.common.collect.Lists;
import milandr_ex.model.BasicControllerTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for verify result of code generation for ADC block
 * Created by lizard2k1 on 15.03.2017.
 */
public class MCUAdcControllerTest extends BasicControllerTest {
	private static String defaultCodeResult = "voidADC_init(void){" +
			"MDR_RST_CLK->ADC_MCO_CLOCK=((0<<0)|(0<<4)|(0<<8)|(1<<13));" +
			"MDR_RST_CLK->PER_CLOCK|=((1<<17));MDR_PORTD->OE&=~((1<<0));" +
			"MDR_PORTD->ANALOG&=~((1<<0));}";

	private MCUAdcController adcController;

	@Before
	public void initController() {
		adcController = new MCUAdcController();
		super.initController(adcController);
	}

	@Test
	public void testDefaultPowerParams() {
		List<String> oldCodeList = Lists.newArrayList();
		assertEquals("[]", String.valueOf(oldCodeList));
		List<String> newCodeList = adcController.generateCode(device, oldCodeList);
		assertNotEquals(oldCodeList, newCodeList);
		testCodeResult(defaultCodeResult, 10, 9, 22, newCodeList);
	}
}
