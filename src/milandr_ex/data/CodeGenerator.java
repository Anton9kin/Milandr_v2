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
		List<String> codeList = model.getBlockCode(pairBlock.name());
		switch (pairBlock) {
			case ADC:
				codeList = generateADCCode(device, pairBlock, model, codeList);
				break;
			case DAC:
				codeList = generateDACCode(device, pairBlock, model, codeList);
				break;
			default:
				// do nothing
				break;
		}
		model.setBlockCode(pairBlock.name(), codeList);
	}

	private List<String> generateADCCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateADCCode(%s, %s, %s)", device, pairBlock, model));
		oldCode.add(0, String.format("// code block for %s module", pairBlock.name()));
		oldCode.add(String.format("// end of code block for %s module", pairBlock.name()));
		return oldCode;
	}

	private List<String> generateDACCode(Device device, Device.EPairNames pairBlock,
										 PinoutsModel model, List<String> oldCode) {
		oldCode = Lists.newArrayList();
		log.debug(String.format("#generateDACCode(%s, %s, %s)", device, pairBlock, model));
		oldCode.add(0, String.format("// code block for %s module", pairBlock.name()));
		oldCode.add(String.format("// end of code block for %s module", pairBlock.name()));
		return oldCode;
	}
}
