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
	public enum GenKind {
		SIMPLE, COMPLEX, BUILDER
	}
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
		indent++;
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
		addCodeStr(-1, codeList, format, args);
	}
	public void addCodeStr(int lineInd, List<String> codeList, String format, Object... args) {
		String braces = format.trim();
		if (braces.contains("//")) {
			braces = braces.substring(0, format.lastIndexOf("//")).trim();
			format = format.replaceFirst("//", "\t//");
		}
		if (braces.endsWith("}")) indent--;
		format = format.trim();
		for(int i = indent; i > 0; i--) format = "\t" + format;
		if (lineInd >= 0) codeList.add(lineInd, String.format(format, args));
		else codeList.add(String.format(format, args));
		if (braces.endsWith("{")) indent++;
	}

	public void execCodeCommand(List<String> codeList, String comment, String command, String... values) {
		addCodeStr(codeList,"// " + comment);
		addCodeStr(codeList, command, values);
		addCodeStr(codeList, "");
	}

	public void setCodeParameter(List<String> codeList, String comment, String param, String value) {
		setCodeParameter(codeList, comment, param, value, "");
	}
	public void setCodeParameter(List<String> codeList, String comment, String param, String value, String opp) {
		addCodeStr(codeList,"// " + comment);
		addCodeStr(codeList, String.format("%s %s= (%s)", param, opp, value));
		addCodeStr(codeList, "");
	}

	public void setCodeParameters(List<String> codeList, String param, String[] comments, String[] values) {
		if (values.length < 1) return;
		addCodeStr(codeList,"// " + comments[0]);
		String lastLine = String.format("%s = ((%s)", param, values[0]);
		for(int i = 1; i < values.length; i++) {
			addCodeStr(codeList, lastLine);
			if (i == 1) indent++;
			if (comments.length > i) addCodeStr(codeList,"// " + comments[i]);
			lastLine = String.format("| (%s)", values[i]);
		}
		addCodeStr(codeList, lastLine + ");");
		addCodeStrL(codeList, "");
	}

	private CodeExpressionBuilder builder;

	public CodeExpressionBuilder builder() {
		if (builder == null) builder = new CodeExpressionBuilder();
		return builder;
	}

	public class CodeExpressionBuilder {
		String param, value, opp;
		List<String> comments, values;

		public CodeExpressionBuilder clear() {
			param = null;
			value = null;
			comments = null;
			values = null;
			opp = null;
			return this;
		}

		public CodeExpressionBuilder setParam(String param) {
			this.param = param;
			return this;
		}

		public CodeExpressionBuilder setValue(String value) {
			this.value = value;
			return this;
		}

		public CodeExpressionBuilder setOpp(String opp) {
			this.opp = opp;
			return this;
		}

		public CodeExpressionBuilder setComments(String... comments) {
			List<String> list = Lists.newArrayList(comments);
			if (this.comments == null) {
				this.comments = list;
			} else this.comments.addAll(list);
			return this;
		}

		public CodeExpressionBuilder setValues(String... values) {
			List<String> list = Lists.newArrayList(values);
			if (this.values == null) {
				this.values = list;
			} else this.values.addAll(list);
			return this;
		}

		public CodeExpressionBuilder setCommentParamValue(String comment, String param, String value) {
			return setComments(comment).setParam(param).setValues(value);
		}

		public CodeExpressionBuilder setCommentCommand(String comment, String command) {
			return setComments(comment).setParam(command);
		}

		private CodeExpressionBuilder validate() {
			if (comments == null) comments = Lists.newArrayList();
			if (values == null) values = Lists.newArrayList();
			if (comments.isEmpty()) comments.add("");
			if (values.isEmpty()) values.add("");
			if (opp == null) opp = "";
			return this;
		}

		public void buildParam(List<String> codeList) {
			validate();
			CodeGenerator.this.setCodeParameter(codeList, comments.get(0), param,
					values.get(0), opp);
			clear();
		}

		public void buildParams(List<String> codeList) {
			validate();
			CodeGenerator.this.setCodeParameters(codeList, param,
					comments.toArray(new String[]{}), values.toArray(new String[]{}));
			clear();
		}

		public void buildCommand(List<String> codeList) {
			validate();
			CodeGenerator.this.execCodeCommand(codeList, comments.get(0), param,
					values.toArray(new String[]{}));
			clear();
		}
	}
}
