package milandr_ex.data;

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
		resetIndent();
		List<String> codeList = model.getBlockCode(pairBlock.name());
		if (codeList == null || pairBlock.model() == null) return;
		if (pairBlock.model().getController() == null) return;
		codeList = pairBlock.model().getController()
				.generateCode(device, codeList);
		model.setBlockCode(pairBlock.name(), codeList);
	}

	public final String[] strWWDGINT = { "запрещено", "разрешено" };
	public final String[] EN_INT = { "запрещение", "разрешение" }, EN_IST = {"LSI", "HCLK"};

	private int indent = 0;

	public void resetIndent() {
		indent = 0;
	}
	public void addCodeStrL2(List<String> codeList, String format, Object... args) {
		indent--;
		addCodeStrL(codeList, format, args);
	}
	public void addCodeStrR2(List<String> codeList, String format, Object... args) {
		indent++;
		addCodeStrR(codeList, format, args);
	}
	public void addCodeStrL(List<String> codeList, String format, Object... args) {
		indent--;
		addCodeStr(codeList, format, args);
	}
	public void addCodeStrR(List<String> codeList, String format, Object... args) {
		indent++;
		addCodeStr(codeList, format, args);
	}
	public void addCodeStr(List<String> codeList, String format, Object... args) {
		String braces = format.trim();
		if (braces.contains("//")) {
			braces = braces.substring(0, format.lastIndexOf("//")).trim();
			format = format.replaceFirst("//", "\t//");
		}
		if (braces.endsWith("}")) indent--;
		for(int i = indent; i >0; i--) format = "\t" + format.trim();
		codeList.add(String.format(format, args));
		if (braces.endsWith("{")) indent++;
	}
}
