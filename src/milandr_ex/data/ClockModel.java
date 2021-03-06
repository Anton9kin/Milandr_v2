package milandr_ex.data;

import milandr_ex.utils.guava.Lists;
import milandr_ex.utils.guava.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizard on 22.02.17 at 13:23.
 */
public class ClockModel {
	private static Map<String, String> pinAliases = new HashMap<String, String>(){{
		put("ADC-CLK", "ADC-C2");put("CPU-CLK", "HCLK");put("USB-CLK", "USB-C2");
	}};

	public void save(List<String> toSave) {
		//do nothing - will saved by pins
		saveInpVal(toSave, "HSE");
	}

	private void saveInpVal(List<String> toSave, String inpName) {
		toSave.add(String.format("clk.%s=%d", inpName, getInpVal(inpName)));
	}

	public void load(List<String> toLoad) {
		for (String loadStr : toLoad) {
			if (loadStr.startsWith("clk")) loadStr = loadStr.substring(4);
			String[] parts = loadStr.split("=");
			if (parts.length < 2 || !parts[1].matches("\\d+")) continue;
			setInput(parts[0], Integer.parseInt(parts[1]));
		}
	}

	public static class InOut {
		private String alias;
		private final String name;
		private final String body;
		private final String from;
		private Integer baseValue;
		private String baseFactor;
		private Integer factorIndex;
		private String restriction = "";
		private Integer value;

		public InOut(String name, String body, String from) {
			this.name = name;
			this.body = body;
			this.from = from;
			initFactor();
		}

		private void initFactor() {
			if (body.contains(" ")) {
				setFactor(body.charAt(0) + " 1", 0);
			}
		}

		public InOut setAlias(String alias) {
			this.alias = alias;
			return this;
		}

		public InOut setFactor(String baseFactor, Integer index) {
			System.out.println(String.format("pin::%s factor changed %s -> %s", this, this.baseFactor, baseFactor));
			this.baseFactor = baseFactor;
			this.factorIndex = index;
			return this;
		}

		public InOut setValue(Integer value) {
			this.baseValue = value;
			return this;
		}

		public String getRestr() {
			return restriction;
		}

		public InOut setRestr(String restr) {
			restriction = restr;
			return this;
		}

		public String getFactor() {
			return baseFactor == null ? body : baseFactor;
		}

		public Integer getFactorIndex() {
			return factorIndex == null ? 0 :  factorIndex;
		}

		public int getValue() {
			checkFullSetup();
			int result = baseValue == null ? 1 : baseValue;
			if (!name.equals("Sim") && body.contains(" ")) {
				String[] parts = getFactor().split("\\s");
				int factor = parts[1].matches("\\d+") ? Integer.parseInt(parts[1]) : 1;
				if (parts[0].equals("*")) {
					return result * factor;
				} else if (parts[0].equals("/")) {
					return result / factor;
				}
			}
			this.value = result;
			return value;
		}
		private InOut checkFullSetup() {
			if (name == null) throw new NullPointerException("Name not set");
			if (body == null) throw new NullPointerException("Body not set");
			if (from == null) throw new NullPointerException("From not set");
			return this;
		}
		public static InOut get(String name, String body, String from) {
			if (pinAliases.containsKey(from)) from = pinAliases.get(from);
			return new InOut(name, body, from).checkFullSetup();
		}

		@Override
		public String toString() {
			return "InOut{" +
					"alias='" + alias + '\'' +
					", name='" + name + '\'' +
					", body='" + body + '\'' +
					", from='" + from + '\'' +
					", baseFactor=" + baseFactor +
					", baseValue=" + baseValue +
					", value=" + getValue() +
					'}';
		}

		public String toStr() {
			return "\tIOt{" +
					", nm='" + name + '\'' +
					", bd='" + body + '\'' +
					", fr='" + from + '\'' +
					", bF=" + baseFactor +
					", bV=" + baseValue +
					", val=" + getValue() +
					'}';
		}
	}
	public static class Block {
		private String name;
		private String body;
		private Map<String, InOut> inputs;
		private InOut selected;
		private InOut output;

		public Block(String name, String body) {
			this.name = name;
			this.body = body;
			initBody();
		}

		private Block initBody() {
			String exitStr = body.split("\\\\")[1];
			String[] combos = body.split("\\|");
			for(int i = 0; i < (combos.length - 1); i = i + 2) {
				boolean isInOut = name.contains("PUT");
				addInp(InOut.get(isInOut ? combos[i] : name + "-" + (i/2),
						combos[i + 1], combos[i]));
			}
			setOut(InOut.get("out", exitStr, name));
			return this;
		}

		public Block addInp(InOut input) {
			if (inputs == null) inputs = Maps.newLinkedHashMap();
			if (selected == null) setSelected(input);
			inputs.put(input.name, input);
			return this;
		}
		public Block setOut(InOut output) {
			this.output = output;
			return this;
		}
		public InOut getSelected() {
			checkFullSetup();
			return selected;
		}

		private Block setSelected(InOut selected) {
			this.selected = selected;
			return this;
		}

		public Set<String> getPinNames() {
			checkFullSetup();
			return inputs.keySet();
		}
		public Map<String, InOut> getPins() {
			checkFullSetup();
			return inputs;
		}
		public Integer getPinSize() {
			checkFullSetup();
			return inputs.size();
		}
		public Block select(int ind) {
			checkFullSetup();
			if (ind >= 0 && ind < inputs.size()) {
				select(getPinName(ind));
			}
			return this;
		}
		public Block select(String name) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				select(inputs.get(name));
			}
			return this;
		}
		public Block select(InOut inOut) {
			checkFullSetup();
			if (inputs.values().contains(inOut)) {
				selected = inOut;
			}
			output.setValue(selected.getValue());
			return this;
		}
		public boolean hasPin(String name) {
			return inputs.containsKey(name);
		}
		public Integer getPin(int index) {
			return getPin(getPinName(index));
		}
		public Integer getSel() {
			int ind = 0;
			for(InOut pin: inputs.values()) {
				if (pin.equals(selected)) return ind;
				ind++;
			}
			return -1;
		}
		public Integer getSel(String name) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				String factor = inputs.get(name).getFactor();
				if (factor == null || factor.isEmpty()) return 0;
				return inputs.get(name).getFactorIndex();
//				return Integer.parseInt(factor.split("\\s")[1]);
			} else if (name.endsWith("-O")) {
				return output.getFactorIndex();
			}
			return -1;
		}
		public Integer getPin(String name) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				return inputs.get(name).getValue();
			}
			return output.getValue();
		}
		private String getPinName(int ind) {
			if (ind < 0 || ind >= inputs.size()) return "out";
			return inputs.keySet().toArray(new String[]{})[ind];
		}
		private String getPinRestr(int ind) {
			return getPinRestr(getPinName(ind));
		}
		private String getPinRestr(String name) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				return inputs.get(name).getRestr();
			}
			return output.getRestr();
		}
		public Block setPin(int ind, Integer value) {
			return setPin(getPinName(ind), value);
		}
		public Block setPin(String name, Integer value) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				inputs.get(name).setValue(value);
			}
			return this;
		}
		public Block setPinFactor(int ind, String value, Integer index) {
			return setPinFactor(getPinName(ind), value, index);
		}
		public Block setPinFactor(String name, String value, Integer index) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				inputs.get(name).setFactor(value, index);
			} else if (name.equals("out")) {
				output.setFactor(value, index);
			}
			return this;
		}
		public String getPinFactor(int ind) {
			return getPinFactor(getPinName(ind));
		}
		public String getPinFactor(String name) {
			if (inputs.containsKey(name)) {
				return inputs.get(name).getFactor();
			} else if (name.equals("out")) {
				return output.getFactor();
			}
			return "";
		}
		public Integer getPinFactorIndex(int ind) {
			return getPinFactorIndex(getPinName(ind));
		}
		public Integer getPinFactorIndex(String name) {
			if (inputs.containsKey(name)) {
				return inputs.get(name).getFactorIndex();
			} else if (name.equals("out")) {
				return output.getFactorIndex();
			}
			return 0;
		}
		public Block setPinRestr(int ind, String value) {
			return setPinRestr(getPinName(ind), value);
		}
		public Block setPinRestr(String name, String value) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				inputs.get(name).setRestr(value);
			} else if (name.equals("out")) {
				output.setRestr(value);
			}
			return this;
		}
		public Block calc() {
			for(InOut pin: inputs.values()) {
				pin.getValue();
			}
			output.setValue(selected.getValue()).getValue();
			return this;
		}
		private Block checkFullSetup() {
			if (inputs == null) throw new NullPointerException("Inputs not set");
			if (output == null) throw new NullPointerException("Output not set");
			if (selected == null) throw new NullPointerException("Selection not set");
			return this;
		}
		public static Block get(String name, String body) {
			return new Block(name, body).checkFullSetup();
		}

		@Override
		public String toString() {
			return "\nBlock{" +
					"name='" + name + '\'' +
					", body='" + body + '\'' +
					",\n inputs=" + inputs +
					", selected=" + selected +
					", output=" + output +
					'}';
		}

		public String toStr() {
			return "\nBlk{" +
					"nm='" + name + '\'' +
					", bd='" + body + '\'' +
					",\n\tinp=" + toIOStr(inputs) +
					",\n\tseld=" + selected.toStr() +
					",\n\tout=" + output.toStr() +
					'}';
		}
	}

	private Block inputs;
	private Block outputs;
	private final List<Block> blocks;
	private final Map<String, Block> blockMap;
	private Map<String, String> restrictions = Maps.newHashMap();
	public ClockModel(String body, List<Block> blocks) {
		this.blocks = blocks;
		this.blockMap = Maps.newHashMap();
		initBlocks();
	}

	public ClockModel(String body) {
		this(body, Lists.newArrayList());
	}

	private void initBlocks() {
		for(Block block: blocks) {
			if (block.name.equals("INPUTS")) inputs = block;
			if (block.name.equals("OUTPUT")) outputs = block;
			blockMap.put(block.name, block);
		}
	}

	public ClockModel addRestriction(String name, String restriction) {
		this.restrictions.put(name, restriction);
		this.outputs.setPinRestr(name, restriction);
		return this;
	}

	public Map<String, String> getRestrictions() {
		return restrictions;
	}

	public ClockModel setInputs(Block inputs) {
		this.inputs = inputs;
		return this;
	}
	public ClockModel setOutputs(Block outputs) {
		this.outputs = outputs;
		return this;
	}
	public ClockModel addBlock(Block block) {
		blocks.add(block);
		return this;
	}
	public ClockModel calc() {
		checkFullSetup();
		inputs.calc();
		for(Block block: blocks) {
			if (block.equals(inputs)) continue;
			if (block.equals(outputs)) continue;
			calcOneBlock(block);
		}
		calcOneBlock(outputs);
		return this;
	}

	private void calcOneBlock(Block block) {
		int cnt = block.getPinSize();
		InOut seld = block.getSelected();
		for(int i = 0; i < cnt; i++) {
			InOut pin = block.select(i).getSelected();//block.getSelected();
			Block fromBlock = blockMap.get(pin.from);
			boolean fromInp = false;
			if (fromBlock == null) {
				fromBlock = inputs;
				fromInp = true;
			}
			if (fromBlock != null) {
				pin.setValue(fromBlock.getPin(fromInp ? pin.from : "out")).getValue();
			}
			block.calc();
		}
		block.setSelected(seld).calc();
	}

	public Integer getOut(String name) {
		checkFullSetup();
		return outputs.getPin(name);
	}
	private String getBlockName(int ind) {
		return blockMap.keySet().toArray(new String[]{})[ind];
	}
	public ClockModel setInput(String name, Integer value) {
		checkFullSetup();
		inputs.setPin(name, value);
		return this;
	}
	public ClockModel setFactor(int blkInd,  int pinInd, String value, Integer fIndex) {
		return setFactor(getBlockName(blkInd), pinInd, value, fIndex);
	}
	public ClockModel setFactor(String block, int index, String value, Integer fIndex) {
		if (index < 0) return this;
		checkFullSetup();
		blockMap.get(block).setPinFactor(index, value, fIndex);
		return this;
	}
	public ClockModel setFactor(String block, String name, String value, Integer fIndex) {
		checkFullSetup();
		blockMap.get(block).setPinFactor(name, value, fIndex);
		return this;
	}
	public ClockModel setSelected(String block, int ind) {
		checkFullSetup();
		blockMap.get(block).select(ind);
		return this;
	}
	public ClockModel setSelected(String block, String name) {
		checkFullSetup();
		blockMap.get(block).select(name);
		return this;
	}
	public ClockModel setInputs(String[] names, int[] values) {
		int ind = 0;
		for(String name: names) {
			inputs.setPin(name, values[ind++]);
		}
		return this;
	}
	public Integer getOut(int blkInd, int index) {
		return getOut(getBlockName(blkInd), index);
	}
	public Integer getOut(String blockName, int index) {
		checkFullSetup();
		for(Block block: blocks) {
			if (block.name.equals(blockName)) {
				return block.getPin(index);
			}
		}
		return outputs.getPin(index);
	}
	public String getRestr(String blockName, int index) {
		checkFullSetup();
		for(Block block: blocks) {
			if (block.name.equals(blockName)) {
				return block.getPinRestr(index);
			}
		}
		return outputs.getPinRestr(index);
	}
	public String getRestr(String blockName, String name) {
		checkFullSetup();
		for(Block block: blocks) {
			if (block.name.equals(blockName)) {
				return block.getPinRestr(name);
			}
		}
		return outputs.getPinRestr(name);
	}
	public Integer getSel(String blockName) {
		if (!blockMap.containsKey(blockName)) {
			int trInd = blockName.lastIndexOf("-");
			if (trInd < 0) return -1;
			String blkName = blockName.substring(0, trInd);
			if (blockMap.containsKey(blkName)) {
				return blockMap.get(blkName).getSel(blockName);
			}
			return -1;
		}
		return blockMap.get(blockName).getSel();
	}
	public Integer getOut(String blockName, String name) {
		checkFullSetup();
		for(Block block: blocks) {
			if (block.name.equals(blockName)) {
				return block.getPin(name);
			}
		}
		return outputs.getPin(name);
	}

	public Integer getInpVal(String name) {
		checkFullSetup();
		return inputs.getPin(name);
	}

	public Integer getOutVal(String name) {
		checkFullSetup();
		return outputs.getPin(name);
	}

	public Integer getOutSel(String name) {
		checkFullSetup();
		return outputs.getSel(name);
	}

	public boolean hasPinIn(String name) {
		return inputs.hasPin(name);
	}

	public boolean hasPinOut(String name) {
		return outputs.hasPin(name);
	}

	private ClockModel checkFullSetup() {
		if (inputs == null) throw new NullPointerException("Inputs not set");
		if (outputs == null) throw new NullPointerException("Outputs not set");
		if (blocks == null) throw new NullPointerException("Blocks not set");
		if (blocks.isEmpty()) throw new NullPointerException("Blocks is empty");
		return this;
	}

	@Override
	public String toString() {
		return "ClockModel{" +
				"inputs=" + inputs +
				", outputs=" + outputs +
				", blocks=" + blocks +
				", blockMap=" + blockMap +
				'}';
	}

	public String toStr() {
		return "Clk{" +
				"in=" + inputs.toStr() +
				", out=" + outputs.toStr() +
				", map=" + toBlStr(blockMap, "") +
				'}';
	}

	public String toStr(String key) {
		return "Clk{" +
				"in=" + inputs.toStr() +
				", out=" + outputs.toStr() +
				", map=" + toBlStr(blockMap, key) +
				'}';
	}

	public static ClockModel get(String body) {
		return get(body, Lists.newArrayList());
	}
	public static ClockModel get(String body, List<ClockModel.Block> blocks) {
		return new ClockModel(body, blocks).checkFullSetup();
	}

	public static String toIOStr(Map<String, InOut> map) {
		String result = "";
		for(String key: map.keySet()) {
			result += String.format("\n\t\t[%s=%s]", key, map.get(key).toStr());
		}
		return result;
	}
	public static String toBlStr(Map<String, Block> map, String key) {
		String result = "";
		for(String mkey: map.keySet()) {
			if (!key.isEmpty() && !mkey.equals(key)) continue;
			result += String.format("\n[%s=%s]", key, map.get(key).toStr());
		}
		return result;
	}
}
