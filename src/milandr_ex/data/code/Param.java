package milandr_ex.data.code;

import milandr_ex.data.CodeGenerator;

import java.util.List;

/**
 * Wrap generation setting single parameter
 * Created by lizard on 14.03.17 at 12:17.
 */
public enum Param {
	LOAD(), VAL(), CTRL(), // sysTick
	CMD(), // MDR_EEPROM
	HS_CONTROL(), CLOCK_STATUS(), CPU_CLOCK(), PLL_CONTROL(),  // MDR_RST_CLK
	REG_0E(), //MDR_BKP
	ERR();
	private final CodeGenerator.CodeExpressionBuilder builder;

	Param() {
		builder = CodeGenerator.instance().builder();
	}
	public Param reset() {
		builder.reset();
		return this;
	}

	public Param comment(Integer cind, Object... args) {
		return comment(new Integer[]{cind}, args);
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
	public Param seti(Integer value, Integer shift) {
		builder.setValues(value).setShifts(shift);
		return this;
	}
	public Param sets(String value, Integer shift) {
		builder.setValues(value).setShifts(shift);
		return this;
	}
	public Param add(String value, Object args) {
		builder.addValue(value, args);
		return this;
	}
	public Param opp(String value) {
		builder.setOpp(value);
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
