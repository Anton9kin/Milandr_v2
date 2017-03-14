package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.List;

/**
 * Wrap generation params and commands for selected module
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Module {
	SysTick(), MDR_EEPROM(), MDR_RST_CLK(), MDR_BKP(),
	MDR_PORTA, MDR_PORTB, MDR_PORTC, MDR_PORTD, MDR_PORTE, MDR_PORTF,
	MDR_ADC, MDR_DAC,
	MDR_ERR();
	private final CodeGenerator.CodeExpressionBuilder builder;
	private Object[] args;

	Module() {
		builder = CodeGenerator.instance().builder();
	}
	public Module get() {
		builder.reset();
		builder.setModule(name());
		param = null;
		command = null;
		return this;
	}
	public Module arr(String[] array) {
		builder.setCommentsArr(array);
		return this;
	}
	public Module set(Param param) {
		this.param = param;
		this.command = null;
		return this;
	}
	public Module set(Command command) {
		this.param = null;
		this.command = command;
		return this;
	}
	public Module pre(String prefix) {
		builder.setCommentPref(prefix);
		return this;
	}
	public Module cmt(Integer... comments) {
		if (param != null) param.comment(comments, args);
		if (command != null) command.comment(comments, args);
		return this;
	}
	public Module cmta(Integer cind, Object... args) {
		if (param != null) param.comment(cind, args);
		if (command != null) command.comment(cind, args);
		return this;
	}
	public Module args(Object... args) {
		this.args = args;
		return this;
	}
	public Module cmt(String... comments) {
		if (param != null) param.comment(comments, args);
		return this;
	}
	private Command command;
	private Param param;

	public Module build(List<String> codeList) {
		if (param != null) param.build(codeList);
		if (command != null) command.build(codeList);
		return this;
	}
}
