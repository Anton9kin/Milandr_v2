package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import milandr_ex.McuType;
import milandr_ex.model.BasicController;
import milandr_ex.model.ModelObserver;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lizard on 20.02.17 at 12:31.
 */
public class AppScene extends Scene {
	private Locale locale = new Locale("ru", "RU");
	public AppScene(@NamedArg("root") Parent root) {
		super(root);
	}

	private Stage appStage;
	private Stage splashStage;
	private BorderPane rootLayout;
	private AnchorPane mainLayout;
	private PinoutsModel pinoutsModel;
	private McuType mcuMain = null;
	private ResourceBundle bundle;// = ResourceBundle.getBundle("resourse/messages", locale);
	private Map<String, List<ModelObserver>> observers = Maps.newHashMap();
	private BasicController rootController;
	private BasicController mainController;
	private SetsGenerator setsGenerator;
	private CodeGenerator codeGenerator;
	private boolean setupInProcess = false;
	private boolean testMode = false;
	private boolean debugMode = false;
	private boolean editMode = false;
	private CodeGenerator.GenKind genKind = CodeGenerator.GenKind.MODEL;

	public void addObserver(String key, ModelObserver observer) {
		if (!observers.containsKey(key)) {
			observers.put(key, Lists.newArrayList());
		}
		observers.get(key).add(observer);
	}

	public void observe(String key) {
		List<ModelObserver> observers = this.observers.get(key);
		if (observers == null || observers.isEmpty()) return;
		for (ModelObserver observer : observers) {
			if (observer instanceof PinoutsModel.Observer) {
				((PinoutsModel.Observer) observer).observe(pinoutsModel);
			}
		}
	}

	public CodeGenerator.GenKind genKind() {
		return genKind;
	}

	public void genKind(CodeGenerator.GenKind genKind) {
		this.genKind = genKind;
		Device.EPairNames temp = getMainController().getLastPair();
		for(Device.EPairNames pair: Device.EPairNames.values()) {
			getCodeGenerator().listenPinsChanges(getDevice(), pair, getPinoutsModel());
			if (!temp.equals(pair)) continue;
			getMainController().updateCodeGenerator(pair.name());
		}
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public void clearObservers() {
		observers.clear();
	}

	public boolean isSetupInProcess() {
		return setupInProcess;
	}

	public void startSetupProcess() {
		setSetupInProcess(true);
	}

	public void stopSetupProcess() {
		setSetupInProcess(false);
	}

	public void setSetupInProcess(boolean setupInProcess) {
		this.setupInProcess = setupInProcess;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public void setAppStage(Stage appStage) {
		this.appStage = appStage;
	}

	public void setPinoutsModel(PinoutsModel pinoutsModel) {
		this.pinoutsModel = pinoutsModel;
	}

	public void setMcuMain(McuType mcuMain) {
		this.mcuMain = mcuMain;
	}

	public Stage getAppStage() {
		return appStage;
	}

	public Stage getSplashStage() {
		return splashStage;
	}

	public void setSplashStage(Stage splashStage) {
		this.splashStage = splashStage;
	}

	public McuType getMcuMain() {
		return mcuMain;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public PinoutsModel getPinoutsModel() {
		return pinoutsModel;
	}

	public Device getDevice() {
		if (pinoutsModel == null) return null;
		return DeviceFactory.getDevice(pinoutsModel.getSelectedBody());
	}

	public BorderPane getRootLayout() {
		if (rootLayout == null) {
			return (BorderPane) getRoot();
		}
		return rootLayout;
	}

	public void setRootLayout(BorderPane rootLayout) {
		this.rootLayout = rootLayout;
	}

	public AnchorPane getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(AnchorPane mainLayout) {
		this.mainLayout = mainLayout;
	}

	public BasicController getRootController() {
		return rootController;
	}

	public void setRootController(BasicController rootController) {
		this.rootController = rootController;
	}

	public BasicController getMainController() {
		return mainController;
	}

	public void setMainController(BasicController mainController) {
		this.mainController = mainController;
	}

	public SetsGenerator getSetsGenerator() {
		return setsGenerator;
	}

	public void setSetsGenerator(SetsGenerator setsGenerator) {
		this.setsGenerator = setsGenerator;
	}

	public CodeGenerator getCodeGenerator() {
		return codeGenerator;
	}

	public void setCodeGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}
}