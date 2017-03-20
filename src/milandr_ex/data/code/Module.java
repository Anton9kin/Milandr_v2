package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Wrap generation params and commands for selected module
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Module {
	SysTick(), MDR_EEPROM(), MDR_RST_CLK(), MDR_BKP(),
	MDR_PORTA, MDR_PORTB, MDR_PORTC, MDR_PORTD, MDR_PORTE, MDR_PORTF,
	MDR_ADC, MDR_DAC, MDR_IWDG, MDR_WWDG, MDR_POWER, MDR_USB,
	MDR_ERR();
	private final CodeGenerator.CodeExpressionBuilder builder;

	private Object[] args;
	private List<String> codeList;
	private int cmtIndx;

	Module() {
		builder = CodeGenerator.instance().builder();
	}
	public Module get() {
		builder.reset();
		builder.setModule(name());
		param = null;
		command = null;
		codeList = null;
		cmtIndx = 0;
		return this;
	}
	public Module arr(String[] array) {
		builder.setCommentsArr(array);
		return this;
	}
	public Module sete(Param param, Object... args) {
		return set(param).end(args);
	}
	public Module set(Param param) {
		this.param = param;
		this.command = null;
		return this;
	}
	public Module sete(Command command, Object... args) {
		return set(command).end(args);
	}
	public Module set(Command command) {
		this.param = null;
		this.command = command;
		return this;
	}
	public Module set(List<String> codeList) {
		this.codeList = codeList;
		return this;
	}
	public Module pre(Integer... prefixes) {
		CommentKind[] arr = new CommentKind[prefixes.length];
		CommentKind[] vals = CommentKind.values();
		for(int i = 0; i < prefixes.length; i++) {
			arr[i] = vals[prefixes[i]];
		}
		return pre(arr);
	}
	public Module pre(CommentKind... prefixes) {
		builder.setCommentPrefs(prefixes);
		return this;
	}
	public Module pre(String prefix) {
		builder.setCommentPref(prefix);
		return this;
	}
	public Module cmt() {
		if (param != null) {
			int lastSize = param.lastSize();
			if (lastSize > 1) {
				Integer[] idxs = new Integer[lastSize];
				Object[] args = new Object[cmtIndx + lastSize];
				Arrays.fill(args, 0);
				for(int i = 0; i < lastSize; i++) {
					args[cmtIndx] = this.args.length > i ? this.args[i] : 0;
					idxs[i] = cmtIndx++;
				}
				return args(args).cmt(idxs);
			}
		}
		return cmt(cmtIndx++);
	}
	public Module end(Object... args) {
		return args(args).end();
	}
	public Module end() {
		return cmt().build();
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
		if (args != null && args.length > 0) this.args = args;
		return this;
	}
	public Module cmt(String... comments) {
		if (param != null) param.comment(comments, args);
		return this;
	}
	private Command command;
	private Param param;

	public Module build() {
		return build(codeList);
	}
	public Module build(List<String> codeList) {
		if (codeList == null) return this;
		if (param != null) param.build(codeList);
		if (command != null) command.build(codeList);
		return this;
	}
}
