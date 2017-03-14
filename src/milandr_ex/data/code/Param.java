package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.List;

/**
 * Wrap generation setting single parameter
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Param {
	LOAD(), VAL(), CTRL();
	private final CodeGenerator.CodeExpressionBuilder builder;

	Param() {
		builder = CodeGenerator.instance().builder();
	}
	public Param reset() {
		builder.reset();
		return this;
	}

	public Param comment(Integer[] values, Object... args) {
		builder.setComments(values, args);
		return this;
	}
	public Param comment(Integer... values) {
		builder.setComments(values);
		return this;
	}
	public Param comment(String... values) {
		builder.setComments(values);
		return this;
	}
	public Param comment(String[] values, Object... args) {
		builder.setComments(values, args);
		return this;
	}
	public Param set(Integer... values) {
		builder.setValues(values);
		return this;
	}
	public Param set(String... values) {
		builder.setValues(values);
		return this;
	}
	public Param shift(Integer... shifts) {
		builder.setShifts(shifts);
		return this;
	}
	public Param build(List<String> codeLines) {
		builder.setParam(name()).buildParams(codeLines);
		return this;
	}
}
