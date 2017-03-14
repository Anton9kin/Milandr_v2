package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.List;

/**
 * Wrap generation one line command
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Command {
	WHILE();
	private final CodeGenerator.CodeExpressionBuilder builder;

	Command() {
		builder = CodeGenerator.instance().builder();
	}
	public Command reset() {
		builder.reset();
		return this;
	}
	public Command set(String param, String suff, String cond) {
		builder.setWhileCommand(param, suff, cond);
		return this;
	}
	public Command build(List<String> codeLines) {
		builder.buildCommand(codeLines);
		return this;
	}
}
