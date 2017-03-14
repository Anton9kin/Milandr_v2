package milandr_ex.model;

import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import milandr_ex.data.*;
import milandr_ex.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static milandr_ex.data.McuBlockProperty.istList;
import static milandr_ex.utils.GuiUtils.bcDef;
import static milandr_ex.utils.GuiUtils.bcTxt;

/**
 * Created by lizard on 20.02.17 at 12:40.
 */
public abstract class BasicController implements ChangeCallbackOwner {
	protected static final Logger log	= LoggerFactory.getLogger(BasicController.class);
	private AppScene scene;
	private ResourceBundle messages;
	private Device.EPairNames devicePair = Device.EPairNames.NON;

	public AppScene getScene() {
		return scene;
	}

	public void setScene(AppScene scene) {
		this.scene = scene;
		messages = scene.getBundle();
	}

	protected ChangeCallback changeCallback;
	protected List<BasicController> subControllers = Lists.newArrayList();

	public <T extends BasicController> T preInit() {
		preInit(getScene());
		return (T) this;
	}
	@SuppressWarnings("unchecked")
	public <T extends BasicController> T postInit() {
		if (getParentController() != null) {
			getParentController().addChildController(this);
		}
		changeCallback = new ChangeCallBackImpl(this) {
			@Override
			public void callListener(String key, String prev, String value) {
				checker.callListener(key, prev, value);
			}
			@Override
			public void callGuiListener(String key, String prev, String value) {
				checker.callGuiListener(key, prev, value);
			}
		};
		getScene().startSetupProcess();
		postInit(getScene());
		Platform.runLater(()->initLater(getScene()));
		initProps(getDevicePair());
		getScene().stopSetupProcess();
		return (T) this;
	}
	protected BasicController parentController;
	public void setParentController(BasicController parentController) { this.parentController = parentController; }
	protected BasicController getParentController() {
		return parentController;
	}
	protected Parent getGPIOControl() { return null; }
	protected Pane getPropControl() { return null; }
	protected void addChildController(BasicController child) {
		subControllers.add(child);
	}
	protected void preInit(AppScene scene) {}
	protected abstract void postInit(AppScene scene);
	protected void initLater(AppScene scene){}
	protected void initSubControllers(BasicController... controllers) {
		initSubControllers(Lists.newArrayList(controllers));
	}
	protected void initSubControllers() {
		initSubControllers(subControllers);
	}
	protected void initSubControllers(List<BasicController> controllers) {
		iterateSubs(controllers, (c) -> initSubController(c));
	}
	protected void initSubController(BasicController subController) {
		subController.setParentController(this);
		subController.setScene(scene);
		subController.postInit();
		Device.EPairNames pair = subController.getDevicePair();
		if (pair.ordinal() > 0) {
			pair.model().setController(subController);
		}
	}

	private void initProps(Device.EPairNames pair) {
		if (pair == null) return;
		if (getParentController() != null) {
			getParentController().hideChildPane(pair);
		}
		if (getPropControl() != null) {
			McuBlockModel model = pair.model();
			model.setPropsPane(getPropControl());
			makeUI(pair);
		}
	}


	protected boolean checkPairForHide(String pairName) {
		if (getScene().isDebugMode()) { return false; }
		if (getScene().isTestMode()) {
			if (Device.testPairNames().contains(pairName)) return false;
		}
		return !Device.prodPairNames().contains(pairName);
	}

	protected void hideChildPane(Device.EPairNames pair) {
		//do nothing by default
	}
	private void makeColumnConstraint(List<ColumnConstraints> cc, double percent) {
		ColumnConstraints cci = new ColumnConstraints();
		cci.setPercentWidth(percent);
		cci.setFillWidth(true);
		cc.add(cci);
	}
	private void makeColumnConstraints(GridPane gp, double... percents) {
		List<ColumnConstraints> cc = gp.getColumnConstraints();
		cc.clear();
		for(double percent: percents) makeColumnConstraint(cc, percent);
	}

	private void makeUI(Device.EPairNames pair) {
		Pane propsPane = getPropControl();
		propsPane.getChildren().clear();
		if (propsPane instanceof GridPane) {
			makeColumnConstraints((GridPane) propsPane, 70, 30);
		}
		String body = getScene().getPinoutsModel().getSelectedBody();
		int pairCnt = DeviceFactory.getDevice(body).getPairCounts().get(pair.ordinal());
		int ind = 0;
		if (pairCnt > 1) {
			HBox hBox = makeToggleGroup(pair, propsPane, pairCnt);
			GridPane.setRowIndex(hBox, ind++);
			GridPane.setColumnSpan(hBox, 2);
		}
		List<McuBlockProperty> props = pair.model().getProps();
		if (props != null) {
			for(McuBlockProperty prop: props) {
				prop.makeUI(getScene(), propsPane, ind++);
			}
			changePropsAreaHeight(propsPane, pairCnt, props.size());
		}
	}

	private void changePropsAreaHeight(Pane propsPane, int pairCnt, int propsSize) {
		Pane parent = (Pane)propsPane.getParent();
		parent.setMinWidth(pairCnt * 25 + propsSize * 25 + 25);
		parent.setPrefHeight(pairCnt * 25 + propsSize * 25 + 25);
		parent.setMaxHeight(pairCnt * 25 + propsSize * 25 + 25);
	}

	private HBox makeToggleGroup(Device.EPairNames pair, Pane propsPane, int pairCnt) {
		HBox hBox = new HBox();
		ToggleGroup group = new ToggleGroup();
		for(int i = 0; i < pairCnt; i++) {
//				RadioButton rBtn = new RadioButton(pair.name() + "-" + i);
			ToggleButton rBtn = new ToggleButton(pair.name() + "-" + (i + 1));
			GuiUtils.setupOnHoverStyle(rBtn, bcDef, bcTxt);
			rBtn.setToggleGroup(group);
			rBtn.setMinWidth(80.0);
			hBox.getChildren().add(rBtn);
			if (i == 0) rBtn.setSelected(true);
			final int ind = i;
			rBtn.selectedProperty().addListener((e, o, n) -> { if (n) pair.model().togglePropValues(ind);});
		}
		propsPane.getChildren().add(hBox);
		return hBox;
	}

	protected void iterateSubs(ControllerIteration iteration) {
		iterateSubs(subControllers, iteration);
	}

	protected void iterateSubs(List<BasicController> subControllers, ControllerIteration iteration) {
		for(BasicController controller: subControllers) {
			iteration.process(controller);
		}
	}

	public void fillAllGpio() {
		iterateSubs(BasicController::fillGpio);
	}

	public boolean filterGpio(String key, String item) {
		return item != null && item.contains(" ");
	}

	protected void fillGpio() {
		if (getGPIOControl() != null) {
			ObservableList<Node> children = ((GridPane) getGPIOControl()).getChildren();
			children.clear();
			List<String> pinList = getPinList();
			for(String pin: pinList) {
				TitledPane label = new TitledPane(Constants.keyToText(pin), new Label());
				label.setMinSize(160.0, 20.0);
				label.setExpanded(false);
				children.add(label);
				GridPane.setRowIndex(label, pinList.indexOf(pin));
			}
		}
	}

	 protected void addModelProps(String[] props, List<String>... lists){
		int ind = 0;
		for(String prop: props) {
			getDevicePair().model().addModelProp(McuBlockProperty.getC(getDevicePair(), prop, lists[ind++]));
		}
	 }

	 protected void addModelProps(String[] props, String types, Object... args){
		int ind = 0;
		Device.EPairNames pair = getDevicePair();
		McuBlockProperty mprop;
		for(String prop: props) {
			String cls = types.charAt(ind++) + "";
			Object arg = args == null ? null : (args.length > ind ? args[ind - 1] : null);
			switch (cls) {
				case "B": if (arg == null) arg = true; mprop = McuBlockProperty.get(pair, prop, (boolean)arg); break;
				case "I": if (arg == null) arg = 0; mprop = McuBlockProperty.get(pair, prop, (int)arg); break;
				case "C": mprop = McuBlockProperty.getC(pair, prop, (List<String>) arg); break;
				case "L": mprop = McuBlockProperty.getL(pair, prop, (List<String>) arg); break;
				case "S": default: if (arg == null) arg = "";
					mprop = McuBlockProperty.get(pair, prop, String.valueOf(arg)); break;
			}
			getDevicePair().model().addModelProp(mprop);
		}
	 }

	 protected void addModelProps(String... props){
	 	addModelProps(props, false);
	 }
	 protected void addModelProps(String[] props, boolean ro){
		 Device.EPairNames pair = getDevicePair();
		for(String prop: props) {
			getDevicePair().model().addModelProp(McuBlockProperty.get(pair, prop, "", ro));
		}
	 }

	@Override
	public boolean check() {
		return false;
	}

	@Override
	public Map<String, ? extends Node> nodeMap() {
		return null;
	}

	public void callListener(String key, String prev, String value) {
		//do nothing by default
		getScene().setMainController(this);
		updateCodeGenerator(key);
	}
	public void callGuiListener(String key, String prev, String value) {
		//do nothing by default
		getScene().setMainController(this);
		Boolean inValue = value != null && value.equals("true");
		if (inValue || !key.startsWith("t-")) updateCodeGenerator(key);
	}

	protected String getPairForComboKey(String comboKey) {
		String pairKey = comboKey.substring(comboKey.lastIndexOf("_") + 1).toUpperCase();
		if (pairKey.endsWith("WD")) pairKey += "G";
		return pairKey;
	}

	public void updateCodeGenerator(String pairKey) {
	 	if (pairKey.startsWith("cb")) return;
//		String pairKey = getPairForComboKey(comboKey);
//		if (!Device.pairExists(pairKey)) pairKey = comboKey;
		if (Device.pairExists(pairKey)) {
			Device.EPairNames pair = Device.EPairNames.valueOf(pairKey);
//			regenerateCode(pair);
			List<String> code = pair.model().getCodeList();
			SyntaxHighlighter.set(getScene(), code);
		}
	}

	protected final List<String> emptyList = Lists.newArrayList();
	protected int getBasicClockSrc(List<String> istList, String signSrc){
	 	if (istList == null) istList = emptyList;
	 	if (istList.isEmpty()) return 1;
		return getClockProp(istList.get(getConfPropInt(signSrc)));
	}

	protected int getBasicReloadReg(String countProp, String unitsProp){
		return getBasicReloadReg(countProp, unitsProp, getBasicClockSrc(null, ""));
	}
	protected int getBasicReloadReg(String countProp, String unitsProp, int stClock){
		int reloadReg;
		int count = getConfPropInt(countProp);
		int tf_units = getConfPropInt(unitsProp);
		switch (tf_units) {
			case 0:case 1:case 2:
				reloadReg = stClock * count;
				break;
			default:
				reloadReg = stClock / count;
				break;
		}
		return reloadReg / 10 ^ (3 * (tf_units % 3));
	}

	protected McuBlockProperty getModelProp(String name) {
		return getDevicePair().model().getProp(name);
	}
	protected void setModelProp(String name, int ind, String value) {
		getModelProp(name).setStrValue(value, ind);
	}
	protected void setModelProp(String name, int ind, Integer value) {
		getModelProp(name).setIntValue(value, ind);
	}
	protected Integer getClockProp(String name) {
	 	ClockModel clock = getScene().getPinoutsModel().getClockModel();
	 	if (clock == null) return 0;
	 	if (clock.hasPinIn(name)) return clock.getInpVal(name);
	 	if (clock.hasPinOut(name)) return clock.getOutVal(name);
	 	if (!name.contains(".")) return -1;
	 	String[] names = name.split("\\.");
	 	if (names[1].equals("S")) return clock.getSel(names[0]);
	 	return clock.getOut(names[0], names[1]);
	}

	protected Integer getConfPropInt(String name) {
		if (name.contentEquals(".")) {
			String[] names = name.split("\\.");
			return getConfPropInt(names[0], names[1]);
		}
	 	return getDevicePair().model().getProp(name).getIntValue();
	}

	protected String getConfPropStr(String name) {
		if (name.contentEquals(".")) {
			String[] names = name.split("\\.");
			return getConfPropStr(names[0], names[1]);
		}
	 	return getDevicePair().model().getProp(name).getStrValue();
	}

	protected String getConfPropStr(String name, String sName) {
	 	return getDevicePair().model().getProp(name).getProp(sName).getStrValue();
	}

	protected Integer getConfPropInt(String name, String sName) {
	 	return getDevicePair().model().getProp(name).getProp(sName).getIntValue();
	}

	protected List<String> generateDefines(Device device, List<String> oldCode) {
		return oldCode;
	}
	protected List<String> generateSimpleCodeStep(List<String> oldCode, int codeStep) {
		return oldCode;
	}
	protected List<String> generateComplexCodeStep(List<String> oldCode, int codeStep) {
		return generateSimpleCodeStep(oldCode, codeStep);
	}
	protected List<String> generateBuilderCodeStep(List<String> oldCode, int codeStep) {
		return generateComplexCodeStep(oldCode, codeStep);
	}
	protected List<String> generateModelCodeStep(List<String> oldCode, int codeStep) {
		return generateBuilderCodeStep(oldCode, codeStep);
	}
	protected List<String> generateCode(List<String> oldCode, int step) {
		switch (getScene().genKind()) {
			case SIMPLE: return generateSimpleCodeStep(oldCode, step);
			case COMPLEX: return generateComplexCodeStep(oldCode, step);
			case BUILDER: return generateBuilderCodeStep(oldCode, step);
			case MODEL: return generateModelCodeStep(oldCode, step);
		}
		b().reset();
		return oldCode;
	}

	public List<String> generateCode(Device device, List<String> oldCode) {
		Device.EPairNames pairBlock = getDevicePair();
	 	if (pairBlock == null || pairBlock.model() == null) return Lists.newArrayList();
	 	int lineInd = 0;
	 	g().resetIndent();
		g().addCodeStr(lineInd++, oldCode, String.format("// code block for %s module", pairBlock.name()));
		for(String codeStr: generateDefines(device, Lists.newArrayList())) {
			g().addCodeStr(lineInd++, oldCode, codeStr);
		}
		g().addCodeStr(lineInd, oldCode, String.format("void %s_init( void ){", pairBlock.name()));
		g().addCodeStr(oldCode, "} //void %s_init", pairBlock.name());
		g().addCodeStr(oldCode, "// end of code block for %s module", pairBlock.name());
	 	return oldCode;
	}

	protected CodeGenerator g() { return getScene().getCodeGenerator(); }
	protected CodeGenerator.CodeExpressionBuilder b() { return g().builder(); }
	public ResourceBundle getMessages() {
		return messages;
	}

	protected void log_debug(Logger log, String logText) {
		if (log != null) log.debug(logText);
		else System.out.println(logText);
	}

	protected String makeHzText(int pinOut) {
		String pinSuff = "Hz";
		if (pinOut > 1000) { pinOut /=1000; pinSuff = "KHz"; }
		if (pinOut > 1000) { pinOut /=1000; pinSuff = "MHz"; }
		return "  " + pinOut + pinSuff + "  ";
	}

	protected void checkSelectedPin(String comboKey, String value) {
		iterateSubs((s)->s.checkSelectedPin(comboKey, value));
	}
	protected void saveSelectedPin(String comboKey, String value) {
		PinoutsModel pinoutsModel = getScene().isSetupInProcess() ? null : getScene().getPinoutsModel();
		if (value != null && !value.equals("null") && pinoutsModel != null) {
			pinoutsModel.setSelectedPin(comboKey, value);
			if (getParentController() != null) {
				getParentController().fillAllGpio();
				getParentController().checkSelectedPin(comboKey, value);
			}
		}
	}

	protected void setDevicePair(Device.EPairNames devicePair) {
		this.devicePair = devicePair;
		devicePair.model().setBundle(getMessages());
		devicePair.model().setController(this);
	}

	public Device.EPairNames getDevicePair() {
		return devicePair;
	}

	protected boolean isExtPair() { return false; }
	protected List<String> getPinList() {
	 	McuBlockModel blockModel = getScene().getPinoutsModel().getBlockModel(getDevicePair().name());
	 	if (blockModel == null) return Lists.newArrayList();
		return blockModel.getPinsList();
	}
}
