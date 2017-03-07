package milandr_ex.data;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Main class for code generation
 * Created by lizard on 06.03.17 at 10:04.
 */
public class CodeGenerator {
	private static final Logger log	= LoggerFactory.getLogger(CodeGenerator.class);
	private CodeGenerator() {
	}
	private static CodeGenerator instance;
	public static CodeGenerator instance(){
		if (instance == null) instance = new CodeGenerator();
		return instance;
	}


	public void listenPinsChanges(Device device, Device.EPairNames pairBlock, PinoutsModel model) {
		if (device == null || pairBlock == null || model == null) return;
		log.debug(String.format("#listenPinsChanges(%s, %s, %s)", device, pairBlock, model));
		indent = 0;
		List<String> codeList = model.getBlockCode(pairBlock.name());
		McuBlockModel blockModel = pairBlock.model();
		switch (pairBlock) {
			case ADC:
				codeList = generateADCCode(device, pairBlock, model, blockModel, codeList);
				break;
			case DAC:
				codeList = generateDACCode(device, pairBlock, model, blockModel, codeList);
				break;
			case SYST:
				codeList = generateSystCode(device, pairBlock, model, blockModel, codeList);
				break;
			default:
				// do nothing
				break;
		}
		model.setBlockCode(pairBlock.name(), codeList);
	}

	private List<String> generateADCCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, McuBlockModel blockModel, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s, %s, %s)", device, pairBlock, model));
		oldCode.add(0, String.format("// code block for %s module", pairBlock.name()));
		oldCode.add(String.format("// end of code block for %s module", pairBlock.name()));
		return oldCode;
	}

	private List<String> generateDACCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, McuBlockModel blockModel, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateDACCode(%s, %s, %s)", device, pairBlock, model));
		oldCode.add(0, String.format("// code block for %s module", pairBlock.name()));
		oldCode.add(String.format("// end of code block for %s module", pairBlock.name()));
		return oldCode;
	}

	private static final String[] EN_INT = { "запрещение", "разрешение" }, EN_IST = {"LSI", "HCLK"};
	private List<String> generateSystCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, McuBlockModel blockModel, List<String> oldCode) {
		//handling incoming parameters
		Integer reloadReg = blockModel.getProp("sign_src").getIntValue();
		int interrupt = blockModel.getProp("intrp").getIntValue();
		int source = blockModel.getProp("sign_src").getIntValue();
		int mode = blockModel.getProp("wrk_kind").getIntValue();

		//code block generation
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateSystCode(%s, %s, %s) %d, %d, %d, %d", device, pairBlock, model,
				reloadReg, interrupt, source, mode));

		addCodeStr(oldCode, "// code block for %s module", pairBlock.name());
		addCodeStr(oldCode, "void SysTick_Init(void){");

		addCodeStrR(oldCode, "SysTick->LOAD = 0x" + Integer.toString(reloadReg, 16) + ";");
		addCodeStr(oldCode, "//стартовое значение загружаемое в регистр VAL");
		addCodeStr(oldCode, "SysTick->VAL = 0x00;");
		addCodeStr(oldCode, "SysTick->CTRL = ((1 << 0)");

		addCodeStrR2(oldCode, "//включение таймера");
		addCodeStr(oldCode, "| ((" + interrupt + " << 1)");
		addCodeStr(oldCode, "//" + EN_INT[interrupt] + " прерывания");
		addCodeStr(oldCode, "| ((" + source + " << 2));");

		addCodeStrL2(oldCode, "//источник синхросигнала = " + EN_IST[source] + "");

		addCodeStrL(oldCode, "}\t//void SysTick_Init");

		addCodeStr(oldCode, "// end of code block for %s module", pairBlock.name());
		return oldCode;
	}
	private int indent = 0;

	private void addCodeStrL2(List<String> codeList, String format, Object... args) {
		indent--;
		addCodeStrL(codeList, format, args);
	}
	private void addCodeStrR2(List<String> codeList, String format, Object... args) {
		indent++;
		addCodeStrR(codeList, format, args);
	}
	private void addCodeStrL(List<String> codeList, String format, Object... args) {
		indent--;
		addCodeStr(codeList, format, args);
	}
	private void addCodeStrR(List<String> codeList, String format, Object... args) {
		indent++;
		addCodeStr(codeList, format, args);
	}
	private void addCodeStr(List<String> codeList, String format, Object... args) {
		for(int i = indent; i >0; i--) format = "\t" + format;
		codeList.add(String.format(format, args));
	}
}
