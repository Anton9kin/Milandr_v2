package milandr_ex.model;

import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.data.Device;
import milandr_ex.data.PinoutsModel;
import milandr_ex.utils.ChangeCallBackImpl;
import milandr_ex.utils.ChangeCallback;
import milandr_ex.utils.ChangeCallbackOwner;
import milandr_ex.utils.ControllerIteration;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lizard on 20.02.17 at 12:40.
 */
public abstract class BasicController implements ChangeCallbackOwner {
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
		getScene().stopSetupProcess();
		return (T) this;
	}
	protected BasicController parentController;
	public void setParentController(BasicController parentController) { this.parentController = parentController; }
	protected BasicController getParentController() {
		return parentController;
	}
	protected Parent getGPIOControl() { return null; }
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
				Label label = new Label(Constants.keyToText(pin));
				label.setMinSize(120.0, 20.0);
				children.add(label);
				GridPane.setRowIndex(label, pinList.indexOf(pin));
			}
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
	}
	public void callGuiListener(String key, String prev, String value) {
		//do nothing by default
	}
	public ResourceBundle getMessages() {
		return messages;
	}

	protected void log_debug(Logger log, String logText) {
		if (log != null) log.debug(logText);
		System.out.println(logText);
	}

	protected String makeHzText(int pinOut) {
		String pinSuff = "Hz";
		if (pinOut > 1000) { pinOut /=1000; pinSuff = "KHz"; }
		if (pinOut > 1000) { pinOut /=1000; pinSuff = "MHz"; }
		return "  " + pinOut + pinSuff + "  ";
	}

	protected void saveSelectedPin(String comboKey, String value) {
		PinoutsModel pinoutsModel = getScene().isSetupInProcess() ? null : getScene().getPinoutsModel();
		if (value != null && !value.equals("null") && pinoutsModel != null) {
			pinoutsModel.setSelectedPin(comboKey, value);
			if (getParentController() != null) {
				getParentController().fillAllGpio();
			}
		}
	}

	protected void setDevicePair(Device.EPairNames devicePair) {
		this.devicePair = devicePair;
	}

	public Device.EPairNames getDevicePair() {
		return devicePair;
	}

	protected boolean isExtPair() { return false; }
	protected List<String> getPinList() {
		return getScene().getPinoutsModel().getBlockModel(getDevicePair().name()).getPinsList();
	}
}
