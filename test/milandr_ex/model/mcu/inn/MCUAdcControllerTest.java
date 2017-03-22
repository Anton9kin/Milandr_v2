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
	private static String emptyCodeResult = "voidADC_init(void){MDR_ADC->PER_CLOCK|=((0x1<<17));}";
	private static String defaultCodeResult = "voidADC_init(void){" +
			"MDR_RST_CLK->ADC_MCO_CLOCK=((0x0<<0)|(0x0<<4)|(0x0<<8)|(0x1<<13));" +
			"MDR_RST_CLK->PER_CLOCK|=((0x1<<17));}";

	private MCUAdcController adcController;
	private static String[] adcParamNames = {"base_power", "start_kind", "adc_src", "adc_div", "sw_chn", "temp_sens", "lst_chn"};
	private static String[] adcClockNames = {"ADC-C1.S", "ADC-C2.S", "ADC-C3-O.S"};

	@Before
	public void initController() {
		adcController = new MCUAdcController();
		super.initController(adcController);
	}

	@Override
	protected String[] getControllerParamNames() {
		return adcParamNames;
	}

	@Override
	protected String[] getControllerClockNames() {
		return adcClockNames;
	}

	@Test
	public void testDefaultAdcParams() {
		List<String> oldCodeList = Lists.newArrayList();
		assertEquals("[]", String.valueOf(oldCodeList));
		String[][] values = {{"0","0","0"}, {"0","0","0"}, {"1","0","0"}, {"0","0","0"}, {"0","0","0"}, {"0","0","0"}, {"7","0","0"}};
		testCase.setValues(values).build(this);
		List<String> newCodeList = adcController.generateCode(device, oldCodeList);
		assertNotEquals(oldCodeList, newCodeList);
		testCodeResult(emptyCodeResult, 4, 3, 7, newCodeList);
//		testCodeResult(defaultCodeResult, 8, 7, 16, newCodeList);
	}

	@Test
	public void testOneTestCase() {
		adcController.checkSelectedPin("c-ADC-1", "true");
		String[][] values = {{"0","0","0"}, {"0","0","0"}, {"1","0","0"}, {"0","0","0"}, {"0","0","0"}, {"0","0","0"}, {"7","0","0"}};
		String expectedCodeResult = "voidADC_init(void){MDR_RST_CLK->PER_CLOCK|=(1<<17);MDR_PORTD->OE&=~(1<<7);" +
						"MDR_PORTD->ANALOG&=~(1<<7);MDR_ADC->ADC1_CFG=(1)|(0<<2)|(0<<3)|(7<<4)|(0<<11)|(0<<12);}";
		testCase.setValues(values).build(this);
		List<String> newCodeList = adcController.generateCode(device, Lists.newArrayList());
		testCase.test(this, newCodeList, expectedCodeResult);
	}
	@Test
	public void testTwoTestCase() {
		adcController.checkSelectedPin("c-ADC-1", "true");
		String[][] values = {{"1","0","0"}, {"1","0","0"}, {"0","0","0"}, {"0","0","0"}, {"0","0","0"}, {"0","0","0"}, {"7","0","0"}};
		String expectedCodeResult = "voidADC_init(void){MDR_RST_CLK->ADC_MCO_CLOCK=((0<<0)|(2<<4)|(9<<8)|(1<<13));" +
				"MDR_RST_CLK->PER_CLOCK|=(1<<17);MDR_PORTD->OE&=~(1<<7);MDR_PORTD->ANALOG&=~(1<<7);" +
				"MDR_ADC->ADC1_CFG=(1)|(1<<2)|(1<<3)|(7<<4)|(1<<11)|(0<<12));}";
		testCase.setValues(values).build(this);
		List<String> newCodeList = adcController.generateCode(device, Lists.newArrayList());
		testCase.test(this, newCodeList, expectedCodeResult);
	}
}
