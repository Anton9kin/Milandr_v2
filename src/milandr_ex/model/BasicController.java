package milandr_ex.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static milandr_ex.utils.GuiUtils.bcDef;
import static milandr_ex.utils.GuiUtils.bcTxt;

/**
 * Базовая реализация для всех контроллеров
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

	/**
	 * Предварительная инициализация контроллера в момент получения AppScene
	 * @return self instance
	 */
	public <T extends BasicController> T preInit() {
		preInit(getScene());
		return (T) this;
	}

	private class WrapValue {
		int wrapped = 0;
	}
	/**
	 * Пост инициализация контроллера
	 * @return self instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends BasicController> T postInit() {
		if (getParentController() != null) {
			getParentController().addChildController(this);
		}
//		final WrapValue wrapper = new WrapValue();
		changeCallback = new ChangeCallBackImpl(this) {
			@Override
			public void callListener(String key, String prev, String value) {
//				wrapper.wrapped = Integer.parseInt(value);
//				if (getScene().isSetupInProcess()) return;
				checker.callListener(key, prev, value);
			}
			@Override
			public void callGuiListener(String key, String prev, String value) {
//				if (getScene().isSetupInProcess()) return;
				checker.callGuiListener(key, prev, value);
			}
		};
		getScene().startSetupProcess();
		postInit(getScene());
		Platform.runLater(()->initLater(getScene()));
		initProps(getDevicePair());
//		getScene().stopSetupProcess();
		return (T) this;
	}
	protected BasicController parentController;
	public void setParentController(BasicController parentController) { this.parentController = parentController; }
	protected BasicController getParentController() {
		return parentController;
	}
	protected Parent getGPIOControl() { return null; }
	protected Pane getPropControl() { return null; }
	protected Pane getPropControl(String group) { return getPropControl(); }
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
		if (subController == null) return;
		subController.setParentController(this);
		subController.setScene(scene);
		subController.postInit();
		Device.EPairNames pair = subController.getDevicePair();
		if (pair.ordinal() > 0) {
			pair.model().setController(subController);
		}
	}

	/**
	 * Инициализация "свойств" блоков.
	 * Позволяет скрывает панели родительского контроллера
	 * @param pair указанный блок для инициализации
	 */
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


	/**
	 * Проверка блока на скритие в зависимости от режима запузка приложения (test или debug)
	 * @param pairName указанный блок для проверки
	 * @return true - если необходимо скрыть
	 */
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

	/**
	 * Инициализация UI "свойств" блоков.
	 * Содержит программную инициализация ColumnConstraints
	 * @param pair указанный блок для инициализации
	 */
	private void makeUI(Device.EPairNames pair) {
		makeUI(pair, pair.name());
	}
	private void makeUI(Device.EPairNames pair, String group) {
		Pane propsPane = getPropControl(group);
		if (propsPane == null) return;
		propsPane.getChildren().clear();
		boolean isPropsGrid = propsPane instanceof GridPane;
		if (isPropsGrid) {
			makeColumnConstraints((GridPane) propsPane, pair.colWidth(), 100 - pair.colWidth());
		}
		String body = getScene().getPinoutsModel().getSelectedBody();
		int pairCnt = DeviceFactory.getDevice(body).getPairCounts().get(pair.ordinal());
		int ind = 0;
		if (pairCnt > 1) {
			HBox hBox = makeToggleGroup(pair, propsPane, pairCnt);
			if (isPropsGrid){
			GridPane.setRowIndex(hBox, ind++);
			GridPane.setColumnSpan(hBox, 2);
			}
		}
		List<McuBlockProperty> props = pair.model().getGroup(group);
		if (props != null) {
			for(McuBlockProperty prop: props) {
				prop.makeUI(getScene(), propsPane, ind++);
			}
			changePropsAreaHeight(propsPane, pairCnt, props.size());
		}
	}

	private void changePropsAreaHeight(Pane propsPane, int pairCnt, int propsSize) {
		Pane parent = (Pane)propsPane.getParent();
		if (parent == null) parent = propsPane;
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

	protected ObservableList<Node> clearGpioProps() {
		ObservableList<Node> children = ((GridPane) getGPIOControl()).getChildren();
		getDevicePair().model().clearProps("gpio");
		children.clear();
		return children;
	}
	protected Node getPropsForGpio(TitledPane parent, VBox vbox, String pinName) {
		vbox.getChildren().add(new Label(pinName));
		return vbox;
	}
	protected void fillGpio() {
		if (getGPIOControl() != null) {
			ObservableList<Node> children = clearGpioProps();
			List<String> pinList = getPinList();
			for(String pin: pinList) {
				TitledPane label = makeTitledPane(pin);
				children.add(label);
				GridPane.setRowIndex(label, pinList.indexOf(pin));
				makeUI(getDevicePair(), pin);
			}
		}
	}

	private TitledPane makeTitledPane(String pin) {
		TitledPane label = new TitledPane();
		Node gpioProps = getPropsForGpio(label, new VBox(), pin);
		label.setText(Constants.keyToText(pin));
		label.setContent(gpioProps);
		label.setMinSize(160.0, 20.0);
		label.setExpanded(false);
		GuiUtils.makeListener("gp-" + pin, label, changeCallback);
		return label;
	}

	/**
	 * Add new combo-properties to current block's model
	 * @param props list of properties names
	 * @param lists array of lists of values for combo-boxes
	 */
	 protected void addModelProps(String[] props, List<String>... lists){
	 	addModelProps(props, "", lists);
	 }
	 protected void addModelProps(String[] props, String group, List<String>... lists){
		int ind = 0;
		for(String prop: props) {
			McuBlockProperty blockProp = McuBlockProperty.getC(getDevicePair(), prop, lists[ind++]);
			getDevicePair().model().addModelProp(blockProp.setGroup(group));
		}
	 }

	/**
	 * Add new properties to current block's model with defined types and values
	 * @param props list of properties names
	 * @param types defined types of new properties
	 * @param args default values of new properties
	 */
	 protected void addModelProps(String[] props, String group, String types, Object... args){
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
			getDevicePair().model().addModelProp(mprop.setGroup(group));
		}
	 }

	/**
	 * Add new string properties to current block's model
	 * @param props list of properties names
	 */
	 protected void addModelProps(String... props){
		 addModelProps(props, false);
	 }
	protected void addModelProps(String group, String... props){
		addModelProps(group, props, false);
	 }
	/**
	 * Add new string properties to current block's model
	 * @param props list of properties names
	 * @param ro set true for create read-only properties
	 */
	 protected void addModelProps(String[] props, boolean ro){
	 	addModelProps("", props, ro);
	 }
	 protected void addModelProps(String group, String[] props, boolean ro){
		 Device.EPairNames pair = getDevicePair();
		for(String prop: props) {
			McuBlockProperty blockProp = McuBlockProperty.get(pair, prop, "", ro);
			getDevicePair().model().addModelProp(blockProp.setGroup(group));
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

	/**
	 * Basic method called from auto-created listeners of pinouts|clock tabs
	 * @param key key of selected combo-box
	 * @param prev prev value of combo
	 * @param value new value of combo
	 */
	public void callListener(String key, String prev, String value) {
		//do nothing by default
		getScene().setMainController(this);
		updateCodeGenerator(key);
	}
	protected boolean firstCANInit = false;
	protected boolean firstCANInitialized = false;
	/**
	 * GUI method called from auto-created listeners of pinouts|clock tabs
	 * @param key key of selected combo-box
	 * @param prev prev value of combo
	 * @param value new value of combo
	 */
	public void callGuiListener(String key, String prev, String value) {
		if (firstCANInit && key.startsWith("CAN") && value.equals("RESET")) {
			firstCANInit = false;
			firstCANInitialized = true;
			SplashScreenLoader.makeFadeSplash(getScene().getSplashStage(), getScene().getSplashLayout());
			getScene().getAppStage().show();
		}
		//do nothing by default
		getScene().setMainController(this);
		Boolean inValue = value != null && value.equals("true");
		if (inValue || !key.startsWith("t-")) updateCodeGenerator(key);
	}

	protected void collapseOtherTPanes(Map<String, TitledPane> tpaneMap, String subKey, Boolean inValue) {
		if (inValue) for(String tKey: tpaneMap.keySet()) {
			if (tKey.equals(subKey)) continue;
			tpaneMap.get(tKey).setExpanded(false);
		}
	}

	protected String getPairForComboKey(String comboKey) {
		String pairKey = comboKey.substring(comboKey.lastIndexOf("_") + 1).toUpperCase();
		if (pairKey.endsWith("WD")) pairKey += "G";
		return pairKey;
	}

	private List<String> listenCodeChanges(Device.EPairNames pair) {
		getScene().getCodeGenerator().listenPinsChanges(getScene().getDevice(), pair, getScene().getPinoutsModel());
		return pair.model().getCodeList();
	}
	public void updateCodeGenerator(String pairKey) {
	 	if (pairKey.startsWith("cb")) return;
//		String pairKey = getPairForComboKey(comboKey);
//		if (!Device.pairExists(pairKey)) pairKey = comboKey;
		if (Device.pairExists(pairKey)) {
			Device.EPairNames pair = Device.EPairNames.valueOf(pairKey);
//			regenerateCode(pair);
			List<String> code = pair.model().getCodeList();
			if (code.isEmpty()) {
				code = listenCodeChanges(pair);
			}
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

	/**
	 * Get block's model property by it's name
	 * @param name name for property
	 * @return block property
	 */
	protected McuBlockProperty getModelProp(String name) {
		return getDevicePair().model().getProp(name);
	}

	/**
	 * Set new value for current block's model property
	 * @param name name for property
	 * @param ind value index for setup
	 * @param value new value for property
	 */
	protected void setModelProp(String name, int ind, String value) {
		getModelProp(name).setStrValue(value, ind);
	}
	/**
	 * Set new value for current block's model property
	 * @param name name for property
	 * @param ind value index for setup
	 * @param value new value for property
	 */
	protected void setModelProp(String name, int ind, Integer value) {
		getModelProp(name).setIntValue(value, ind);
	}

	/**
	 * Get Clock property by it's name
	 * @param name name of property
	 * @return current selected value
	 */
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

	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @return current selected value
	 */
	protected Integer getConfPropInt(String name) {
		return getConfPropInt(name, 0);
	}
	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param valueInd index of value of property
	 * @return current selected value
	 */
	protected Integer getConfPropInt(String name, int valueInd) {
		if (name.contentEquals(".")) {
			String[] names = name.split("\\.");
			return getConfPropInt(names[0], names[1], valueInd);
		}
	 	return getDevicePair().model().getProp(name).getIntValue(valueInd);
	}

	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @return current selected value
	 */
	protected String getConfPropStr(String name) {
		return getConfPropStr(name, 0);
	}
	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param valueInd index of value of property
	 * @return current selected value
	 */
	protected String getConfPropStr(String name, int valueInd) {
		if (name.contentEquals(".")) {
			String[] names = name.split("\\.");
			return getConfPropStr(names[0], names[1], valueInd);
		}
	 	return getDevicePair().model().getProp(name).getStrValue(valueInd);
	}

	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param sName sub-name of property
	 * @return current selected value
	 */
	protected String getConfPropStr(String name, String sName) {
		return getConfPropStr(name, sName, 0);
	}
	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param valueInd index of value of property
	 * @return current selected value
	 */
	protected String getConfPropStr(String name, String sName, int valueInd) {
	 	return getDevicePair().model().getProp(name).getProp(sName).getStrValue(valueInd);
	}

	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param sName sub-name of property
	 * @return current selected value
	 */
	protected Integer getConfPropInt(String name, String sName) {
		return getConfPropInt(name, sName, 0);
	}
	/**
	 * Get Configuration's block property by it's name
	 * @param name name of property
	 * @param valueInd index of value of property
	 * @return current selected value
	 */
	protected Integer getConfPropInt(String name, String sName, int valueInd) {
	 	return getDevicePair().model().getProp(name).getProp(sName).getIntValue(valueInd);
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
	/**
	 * Basic method for start code generation with steps and separation by code generation kind
	 *
	 * @param oldCode list of strings with prev generated code
	 * @param step define step of generation for complex code conditions
	 * @return list of strings with new generated code
	 */
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

	protected String methodName() {
		return getDevicePair().name();
	}
	/**
	 * Main method for start code generation
	 *
	 * @param device current selected device
	 * @param oldCode list of strings with prev generated code
	 * @return list of strings with new generated code
	 */
	public List<String> generateCode(Device device, List<String> oldCode) {
		Device.EPairNames pairBlock = getDevicePair();
	 	if (pairBlock == null || pairBlock.model() == null) return Lists.newArrayList();
		oldCode = Lists.newArrayList();
		for(String methodName: methodNames()) {
			if (methodNeeded(methodName)) {
				oldCode.addAll(generateCode(device, Lists.newArrayList(), methodName));
			}
		}
		return oldCode;
	}
	protected boolean methodNeeded(String methodName) {
		return !methodName.isEmpty();
	}
	protected String[] methodNames() {
		return new String[]{methodName()};
	}
	protected List<String> generateCode(Device device, List<String> oldCode, String methodName) {
		if (methodName.equals(getDevicePair().name())) {
			if (methodNames().length < 2) generateCode(oldCode, 0);
		}
	 	int lineInd = 0;
	 	g().resetIndent();
		g().addCodeStr(lineInd++, oldCode, String.format("// code block for %s module", methodName));
		if (methodName.equals(getDevicePair().name())) {
			for(String codeStr: generateDefines(device, Lists.newArrayList())) {
				g().addCodeStr(lineInd++, oldCode, codeStr);
			}
			g().addCodeStr(lineInd++, oldCode, "");
		}
		g().addCodeStr(lineInd, oldCode, String.format("void %s_init( void ){", methodName));
		g().addCodeStr(oldCode, "} //void %s_init", methodName);
		g().addCodeStr(oldCode, "// end of code block for %s module", methodName);
	 	return oldCode;
	}

	/**
	 * Short alias for returning current scene code generator
	 * @return code generator instance
	 */
	protected CodeGenerator g() { return getScene().getCodeGenerator(); }
	/**
	 * Short alias for returning current scene code generator builder
	 * @return code builder instance
	 */
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

	private Device.EPairNames lastPair = Device.EPairNames.NON;

	/**
	 * Set last selected pair from controller
	 * @param lastPairKey last selected pair name
	 */
	protected void setLastPair(String lastPairKey) {
		lastPairKey = lastPairKey.toUpperCase();
		if (lastPairKey.endsWith("WD")) lastPairKey += "G";
		setLastPair(Device.EPairNames.valueOf(lastPairKey));
	}
	/**
	 * Set last selected pair from controller
	 * @param lastPair last selected pair
	 */
	protected void setLastPair(Device.EPairNames lastPair) {
		this.lastPair = lastPair;
	}

	/**
	 * Get last selected pair from controller
	 * @return last selected pair
	 */
	public Device.EPairNames getLastPair() {
		return lastPair;
	}

	private boolean[] cboxChecked = {false, false, false, false, false};
	private Map<String, String> pinSelected = Maps.newHashMap();

	/**
	 * Check if needed checkbox of pair is selected
	 * @param index index of looked checkbox
	 * @return true is checked
	 */
	protected boolean isCboxChecked(int index) {
		if (index <0 || index >= cboxChecked.length) return false;
		return cboxChecked[index];
	}

	/**
	 * Get selected item of needed combo box
	 * @param pinName needed combo box key
	 * @return string value of selected item
	 */
	protected String getPinSelected(String pinName) {
		if (!pinSelected.containsKey(pinName)) return "";
		String value = pinSelected.get(pinName);
		if (value.contains(" ")) value = value.split("\\s")[1];
		return value;
	}
	/**
	 * Get selected port's pin index of needed combo box
	 * @param pinName needed combo box key
	 * @return int index of pin in pair's block
	 */
	protected int getPinSelectedInd(String pinName) {
		String value = getPinSelected(pinName).trim();
		if (value.isEmpty()) return -1;
		if (!value.matches("\\w{2}\\d+")) return 0;
		return Integer.parseInt(value.substring(2));
	}

	/**
	 * Auto-called method from each pin's combo change for
	 * saving pin combos with separation by target controller
	 * @param comboKey key of changed combo-box
	 * @param value new selected value
	 */
	protected void checkSelectedPin(String comboKey, String value) {
		checkSelectedPin(comboKey, value, false);
	}
	protected void checkSelectedPin(String comboKey, String value, boolean force) {
		iterateSubs((s)->s.checkSelectedPin(comboKey, value));
		if (!force && getScene().isSetupInProcess()) return;
		if (value.equals("RESET")) return;
		if (comboKey.matches("c-" + getDevicePair().name() + "-\\d")) {
			String ind = comboKey.substring(comboKey.length() -1, comboKey.length());
			cboxChecked[Integer.parseInt(ind) - 1] = Boolean.parseBoolean(value);
			getScene().genKind(getScene().genKind());
		} else if (comboKey.startsWith(getDevicePair().name())) {
			pinSelected.put(comboKey, value);
		}
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

	/**
	 * Set device pair of current controller for auto-driven method of separation model, props, pins
	 * by controllers that's assigned to pair defined here
	 * @param devicePair pair for assign controller with pair's model
	 */
	protected void setDevicePair(Device.EPairNames devicePair) {
		this.devicePair = devicePair;
		devicePair.model().setBundle(getMessages());
		devicePair.model().setController(this);
	}

	/**
	 * Get device pair of current controller
	 * @return device pair
	 */
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
