package milandr_ex.model.mcu;

import com.google.common.collect.Lists;
import milandr_ex.model.BasicControllerTest;
import milandr_ex.model.mcu.inn.MCUAdcController;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by lizard2k1 on 15.03.2017.
 */
public class MCUClockControllerTest extends BasicControllerTest {
	private MCUClockController cpuController;
	private static String[] cpuParamNames = {"base_power", "start_kind", "adc_src", "adc_div", "sw_chn", "temp_sens", "lst_chn"};
	private static String[] cpuClockNames = {
			"CPU-C1.S", "CPU-C2.S", "HCLK.S",
			"USB-C1.S", "USB-C2.S", "ADC-C1.S", "ADC-C2.S",
			"CPU-C2-1.S", "HCLK-1.S",
	};

	@Before
	public void initController() {
		cpuController = new MCUClockController();
		super.initController(cpuController);
	}

	@Test
	public void testOneTestCase() {
		//						//CPU			//USB	//ADC
		String[] clockValues = {"2", "1", "1", "0", "0", "0", "0",
				//additional combos
				"1", "1", "0", "0", "0"};
		String expectedCodeResult = "voidCPU_init(void){MDR_EEPROM->CMD|=(0<<3);" +
				"MDR_RST_CLK->HS_CONTROL=(1);while((MDR_RST_CLK->CLOCK_STATUS&(1<<2))==0);" +
				"MDR_RST_CLK->CPU_CLOCK=((2<<0));MDR_RST_CLK->PLL_CONTROL=((1<<2)|(0<<8));" +
				"while((MDR_RST_CLK->CLOCK_STATUS&2)!=2);MDR_RST_CLK->CPU_CLOCK=((2<<0)|(1<<2)|(0<<4)|(1<<8));" +
				"MDR_BKP->REG_0E|=(0<<0);MDR_BKP->REG_0E|=(0<<3);}";
		testCase.setClockValues(clockValues).build(this);
		List<String> newCodeList = cpuController.generateCode(device, Lists.newArrayList());
		testCase.test(this, newCodeList, expectedCodeResult);
	}
}
