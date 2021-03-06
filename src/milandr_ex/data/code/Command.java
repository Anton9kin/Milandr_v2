package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.List;

/**
 * Wrap generation one line command
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Command {
	FOR(),
	WHILE();
	private final CodeGenerator.CodeExpressionBuilder builder;

	Command() {
		builder = CodeGenerator.instance().builder();
	}
	public Command reset() {
		builder.reset();
		return this;
	}
	public Command comment(Integer cind, Object... args) {
		return comment(new Integer[]{cind}, args);
	}
	public Command comment(Integer[] values, Object... args) {
		builder.setComments(values, args);
		return this;
	}
	public Command comment(Integer... values) {
		builder.setComments(values);
		return this;
	}
	public Command comment(String... values) {
		builder.setComments(values);
		return this;
	}
	public Command comment(String[] values, Object... args) {
		builder.setComments(values, args);
		return this;
	}
	public Command set(Param param, String suff, String cond) {
		return set(param.name(), suff, cond);
	}
	public Command set(String param, String suff, String cond) {
		switch (this) {
			case FOR:
				builder.setForICommand(param, suff, cond);
				break;
			case WHILE:
				builder.setWhileCommand(param, suff, cond);
				break;
		}
		return this;
	}
	public Command build(List<String> codeLines) {
		builder.buildCommand(codeLines);
		return this;
	}
}
