package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizard on 22.02.17 at 13:23.
 */
public class ClockModel {
	public static class InOut {
		private String alias;
		private final String name;
		private final String body;
		private final String from;
		private Integer baseValue;
		private String baseFactor;
		private Integer value;

		public InOut(String name, String body, String from) {
			this.name = name;
			this.body = body;
			this.from = from;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public void setFactor(String baseFactor) {
			System.out.println(String.format("pin::%s factor changed %s -> %s", this, this.baseFactor, baseFactor));
			this.baseFactor = baseFactor;
		}

		public void setValue(Integer value) {
			this.baseValue = value;
		}

		public int getValue() {
			checkFullSetup();
			int result = baseValue == null ? 1 : baseValue;
			if (!name.equals("Sim") && body.contains("\\s")) {
				String[] parts = body.split("\\s");
				String factorS = baseFactor == null ? parts[1] : baseFactor;
				int factor = Integer.parseInt(factorS);
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
			return new InOut(name, body, from).checkFullSetup();
		}

		@Override
		public String toString() {
			return "InOut{" +
					"alias='" + alias + '\'' +
					", name='" + name + '\'' +
					", body='" + body + '\'' +
					", from='" + from + '\'' +
					", baseValue=" + baseValue +
					", value=" + value +
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

		private void initBody() {
			String exitStr = body.split("\\\\")[1];
			String[] combos = body.split("\\|");
			for(int i = 0; i < (combos.length - 1); i = i + 2) {
				addInp(InOut.get(name + "-" + i, combos[i + 1], combos[i]));
			}
			setOut(InOut.get("out", exitStr, name));
		}

		public Block addInp(InOut input) {
			if (inputs == null) inputs = Maps.newLinkedHashMap();
			if (selected == null) setSelected(input);
			boolean isInOut = name.contains("PUT");
			inputs.put(isInOut ? input.from : input.name, input);
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

		private void setSelected(InOut selected) {
			this.selected = selected;
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
		public Integer getPin(int index) {
			return getPin(getPinName(index));
		}
		public Integer getPin(String name) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				return inputs.get(name).getValue();
			}
			return output.getValue();
		}
		private String getPinName(int ind) {
			return inputs.keySet().toArray(new String[]{})[ind];
		}
		public Block setPin(int ind, Integer value) {
			return setPin(getPinName(ind), value);
		}
		public Block setPin(String name, Integer value) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				inputs.get(name).baseValue = value;
			}
			return this;
		}
		public Block setPinFactor(int ind, String value) {
			return setPinFactor(getPinName(ind), value);
		}
		public Block setPinFactor(String name, String value) {
			checkFullSetup();
			if (inputs.containsKey(name)) {
				inputs.get(name).setFactor(value);
			}
			return this;
		}
		public Block calc() {
			for(InOut pin: inputs.values()) {
				pin.getValue();
			}
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
	}

	private Block inputs;
	private Block outputs;
	private final List<Block> blocks;
	private final Map<String, Block> blockMap;
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
		for(Block block: blocks) {
			if (block.equals(inputs)) continue;
			if (block.equals(outputs)) continue;
			int cnt = block.getPinSize();
			for(int i = 0; i < cnt; i++) {
				InOut pin = block.getSelected();
				Block fromBlock = blockMap.get(pin.from);
				boolean fromInp = false;
				if (fromBlock == null) {
					fromBlock = inputs;
					fromInp = true;
				}
				if (fromBlock != null) {
					pin.setValue(fromBlock.getPin(fromInp ? pin.from : "out"));
				}
			}
		}
		outputs.calc();
		return this;
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
	public ClockModel setFactor(int blkInd,  int pinInd, String value) {
		return setFactor(getBlockName(blkInd), pinInd, value);
	}
	public ClockModel setFactor(String block, int index, String value) {
		if (index < 0) return this;
		checkFullSetup();
		blockMap.get(block).setPinFactor(index, value);
		return this;
	}
	public ClockModel setFactor(String block, String name, String value) {
		checkFullSetup();
		blockMap.get(block).setPinFactor(name, value);
		return this;
	}
	public ClockModel setInputs(String[] names, int[] values) {
		int ind = 0;
		for(String name: names) {
			inputs.setPin(name, values[ind++]);
		}
		return this;
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
	public Integer getOut(String blockName, String name) {
		checkFullSetup();
		for(Block block: blocks) {
			if (block.name.equals(blockName)) {
				return block.getPin(name);
			}
		}
		return outputs.getPin(name);
	}

	private ClockModel checkFullSetup() {
		if (inputs == null) throw new NullPointerException("Inputs not set");
		if (outputs == null) throw new NullPointerException("Outputs not set");
		if (blocks == null) throw new NullPointerException("Blocks not set");
		if (blocks.isEmpty()) throw new NullPointerException("Blocks is empty");
		return this;
	}

	public static ClockModel get(String body) {
		return get(body, Lists.newArrayList());
	}
	public static ClockModel get(String body, List<ClockModel.Block> blocks) {
		return new ClockModel(body, blocks).checkFullSetup();
	}
}
