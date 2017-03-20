package milandr_ex.data;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import milandr_ex.data.code.CommentKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for code generation
 * Created by lizard on 06.03.17 at 10:04.
 */
public class CodeGenerator {
	private static final Logger log	= LoggerFactory.getLogger(CodeGenerator.class);
	public enum GenKind {
		SIMPLE, COMPLEX, BUILDER, MODEL
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
		log.debug(String.format("#listenPinsChanges(%s, %s, %s)", device, pairBlock, model.toStr()));
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
	public final Map<CommentKind, String> definedComments = new HashMap<CommentKind, String>(){{
		put(CommentKind.SOURCE, "Источник для ");
		put(CommentKind.DIVIDOR, "Делитель для ");
		put(CommentKind.FACTOR, "Множитель для ");
		put(CommentKind.TICK_TACK, "Тактирование ");
	}};

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
		if (!comment.trim().isEmpty()) addCodeStr(codeList,"// " + comment);
		addCodeStr(codeList, command, values);
		addCodeStr(codeList, "");
	}

	public void setCodeParameter(List<String> codeList, String comment, String param, String value) {
		setCodeParameter(codeList, comment, param, value, "");
	}
	public void setCodeParameter(List<String> codeList, String comment, String param, String value, String opp) {
		if (checkValueForZero(value)) return; // skip zero values
		if (!comment.trim().isEmpty()) addCodeStr(codeList,"// " + comment);
		String opp1 = opp == null || opp.isEmpty() ? "" : opp.charAt(0) + "";
		String opp2 = opp == null || opp.length() < 2 ? "" : opp.charAt(1) + "";
		addCodeStr(codeList, String.format("%s %s=%s (%s);", param, opp1, opp2, value));
		addCodeStr(codeList, "");
	}

	public void setCodeParameters(List<String> codeList, String param, String[] comments, String[] values) {
		setCodeParameters(codeList, param, comments, values, null);
	}
	public void setCodeParameters(List<String> codeList, String param, String[] comments, String[] values, Integer[] shifts) {
		setCodeParameters(codeList, param, "", comments, values, shifts);
	}
	public void setCodeParameters(List<String> codeList, String param, String pref, String[] comments,
								  String[] values, Integer[] shifts) {
		setCodeParameters(codeList, param, pref, comments, values, shifts, "");
	}
	private boolean checkValueForZero(String value) {
		if (value == null) return true;
		value = value.trim();
		return value.isEmpty() || value.equals("0")
				|| value.equals("0x0") || value.equals("0x00");
	}
	public void setCodeParameters(List<String> codeList, String param, String pref, String[] comments,
								  String[] values, Integer[] shifts, String opp) {
		if (values.length < 1) return;
		int firstI = 0;
		while (firstI < values.length && checkValueForZero(values[firstI])) firstI++;
		if (firstI >= values.length) return;
		if (comments.length > firstI && comments[firstI] != null && !comments[firstI].isEmpty()) {
			addCodeStr(codeList,"// " + pref + comments[firstI]);
		}
		String value = values[firstI];
		if (shifts != null && shifts.length > 0) value += " << " + shifts[firstI];
		String opp1 = opp == null || opp.isEmpty() ? "" : opp.charAt(0) + "";
		String opp2 = opp == null || opp.length() < 2 ? "" : opp.charAt(1) + "";
		String lastLine = String.format("%s %s=%s ((%s)", param, opp1, opp2, value);
		for(int i = firstI + 1; i < values.length; i++) {
			if (checkValueForZero(values[i])) continue; // skip zero values
			addCodeStr(codeList, lastLine);
			if (i == (firstI + 1)) indent++;
			if (comments.length > i) addCodeStr(codeList,"// " + pref + comments[i]);
			value = values[i];
			if (shifts != null && shifts.length > i) value += " << " + shifts[i];
			lastLine = String.format("| (%s)", value);
		}
		addCodeStr(codeList, lastLine + ");");
		if (values.length > 1) addCodeStrL(codeList, "");
		else addCodeStr(codeList, "");
	}

	private CodeExpressionBuilder builder;

	public CodeExpressionBuilder builder() {
		if (builder == null) builder = new CodeExpressionBuilder();
		return builder;
	}

	public class CodeExpressionBuilder {
		String module, param, value, opp, command;
		String commentPref;
		String[] commentsArr;
		List<String> comments, values;
		List<Integer> shifts;
		private CommentKind[] commentPrefs;

		public CodeExpressionBuilder clear() {
			param = null;
			value = null;
			comments = null;
			values = null;
			shifts = null;
			commentPref = null;
			commentPrefs = null;
			opp = null;
			return this;
		}

		public CodeExpressionBuilder reset() {
			clear();
			module = null;
			commentsArr = null;
			return this;
		}
		public CodeExpressionBuilder setModule(String module) {
			this.module = module;
			return this;
		}

		public CodeExpressionBuilder setParam(String param) {
			this.param = param;
			return this;
		}

		public CodeExpressionBuilder setParam(String param, Object... args) {
			this.param = String.format(param, args);
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

		public CodeExpressionBuilder setCommand(String command) {
			this.command = command;
			return this;
		}

		public CodeExpressionBuilder setCommentPrefs(CommentKind ... commentPrefs) {
			this.commentPrefs = commentPrefs;
			return this;
		}
		public CodeExpressionBuilder setCommentPref(String commentPref) {
			this.commentPref = commentPref;
			return this;
		}

		public CodeExpressionBuilder addComment(String comment, Object... args) {
			if (this.comments == null) comments = Lists.newArrayList();
			this.comments.add(String.format(comment, args));
			return this;
		}

		public CodeExpressionBuilder setCommentsArr(String[] commentsArr) {
			this.commentsArr = commentsArr;
			return this;
		}

		public CodeExpressionBuilder addComment(Integer ind, Object... args) {
			if (commentsArr == null || ind >= commentsArr.length) return this;
			return addComment(commentsArr[ind], args);
		}

		public CodeExpressionBuilder addValue(String value, Object... args) {
			if (this.values == null) values = Lists.newArrayList();
			this.values.add(String.format(value, args));
			return this;
		}

		public CodeExpressionBuilder setComments(Integer... idxs) {
			return setComments(idxs, new Object[]{});
		}
		public CodeExpressionBuilder setComments(Integer[] idxs, Object... args) {
			if (commentsArr == null || idxs == null) return this;
			for(Integer idx: idxs) {
				if (idx == null || idx > commentsArr.length) continue;
				addComment(commentsArr[idx], args);
			}
			return this;
		}

		public CodeExpressionBuilder setComments(String... comments) {
			return setComments(comments, new Object[]{});
		}
		public CodeExpressionBuilder setComments(String[] comments, Object... args) {
			if (comments == null) return this;
			List<String> list = Lists.newArrayList(comments);
			Iterators.removeIf(list.iterator(), String::isEmpty);
			if (this.comments == null) {
				this.comments = list;
			} else this.comments.addAll(list);
			return this;
		}

		public CodeExpressionBuilder setValues(Integer... values) {
			if (values == null) return this;
			String[] strValues = new String[values.length];
			for(int i = 0; i < values.length; i++) {
				strValues[i] = "0x" + Integer.toHexString(values[i]);
			}
			return setValues(strValues);
		}
		public CodeExpressionBuilder setValues(String... values) {
			if (values == null) return this;
			List<String> list = Lists.newArrayList(values);
			if (this.values == null) {
				this.values = list;
			} else this.values.addAll(list);
			return this;
		}

		public CodeExpressionBuilder setShifts(Integer... values) {
			if (values == null) return this;
			List<Integer> list = Lists.newArrayList(values);
			if (this.shifts == null) {
				this.shifts = list;
			} else this.shifts.addAll(list);
			return this;
		}

		public CodeExpressionBuilder setCommentParamValue(Integer cind, String param, String value) {
			String comment =  (commentsArr == null || cind >= commentsArr.length) ? "" : commentsArr[cind];
			return setComments(comment).setParamValue(param, value);
		}

		public CodeExpressionBuilder setCommentParamValue(String comment, String param, String value) {
			return setComments(comment).setParamValue(param, value);
		}

		public CodeExpressionBuilder setCommentParamValue(Integer cind, String param, Integer value, Integer shift) {
			String comment =  (commentsArr == null || cind >= commentsArr.length) ? "" : commentsArr[cind];
			return setCommentParamValue(comment, param, value, shift);
		}
		public CodeExpressionBuilder setCommentParamValue(String comment, String param, Integer value, Integer shift) {
			return setCommentParamValue(comment, param, value + "", shift);
		}
		public CodeExpressionBuilder setCommentParamValue(String comment, String param, String value, Integer shift) {
			return setCommentParamValue(comment, param, value).setShifts(shift);
		}

		public CodeExpressionBuilder setParamValue(String param, String value) {
			return setParam(param).setValues(value);
		}

		public CodeExpressionBuilder setParamValue(String param, Integer value, Integer shift) {
			return setParamValue(param, value + "", shift);
		}
		public CodeExpressionBuilder setParamValue(String param, String value, Integer shift) {
			return setParam(param).setValues(value).setShifts(shift);
		}

		public CodeExpressionBuilder setCommentCommand(String comment, String command) {
			return setComments(comment).setCommand(command);
		}

		public CodeExpressionBuilder setWhileCommand(String param, String suff, String cond) {
			return setWhileCommand("", param, suff, cond);
		}
		public CodeExpressionBuilder setWhileCommand(String comment, String param, String suff, String cond) {
			setComments(comment).setParam(param);
			setCommand(String.format("while ((%s & (%s)) %s);", getFullParam(), suff, cond));
			return this;
		}

		private CodeExpressionBuilder validate() {
			if (comments == null) comments = Lists.newArrayList();
			if (values == null) values = Lists.newArrayList();
			if (shifts == null) shifts = Lists.newArrayList();
			if (comments.isEmpty()) comments.add("");
			if (values.isEmpty()) values.add("");
			if (opp == null) opp = "";
			if (command == null) command = "";
			if (commentPref == null) commentPref = "";
			if (commentPrefs == null) commentPrefs = new CommentKind[]{};
			return this;
		}

		public void buildParam(List<String> codeList) {
			validate();
			String value = values.get(0);
			if (shifts != null && !shifts.isEmpty()) value += " << " + shifts.get(0);
			CodeGenerator.this.setCodeParameter(codeList, commentPref + comments.get(0),
					getFullParam(), value, opp);
			clear();
		}

		private String getFullParam() {
			String fullParam;
			if (module != null && !module.isEmpty()) {
				fullParam = module + "->" + param;
			} else fullParam = param;
			return fullParam;
		}

		public void buildParams(List<String> codeList) {
			validate();
			String[] fullComments = new String[comments.size()];
			if (fullComments.length > 0) for(int i = 0; i < comments.size(); i++) {
				fullComments[i] = (commentPrefs.length <= i ? ""
						: definedComments.get(commentPrefs[i])) + comments.get(i);
			}
			CodeGenerator.this.setCodeParameters(codeList, getFullParam(),
					commentPref, fullComments,
					values.toArray(new String[]{}), shifts.toArray(new Integer[]{}), opp);
			clear();
		}

		public void buildCommand(List<String> codeList) {
			validate();
			CodeGenerator.this.execCodeCommand(codeList, commentPref + comments.get(0),
					command, values.toArray(new String[]{}));
			clear();
		}
	}
}
