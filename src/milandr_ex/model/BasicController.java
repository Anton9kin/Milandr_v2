package milandr_ex.model;

import javafx.application.Platform;
import javafx.scene.Node;
import milandr_ex.data.AppScene;
import milandr_ex.utils.ChangeCallBackImpl;
import milandr_ex.utils.ChangeCallback;
import milandr_ex.utils.ChangeCallbackOwner;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lizard on 20.02.17 at 12:40.
 */
public abstract class BasicController implements ChangeCallbackOwner {
	private AppScene scene;
	private ResourceBundle messages;

	public AppScene getScene() {
		return scene;
	}

	public void setScene(AppScene scene) {
		this.scene = scene;
		messages = scene.getBundle();
	}

	protected ChangeCallback changeCallback;

	@SuppressWarnings("unchecked")
	public <T extends BasicController> T postInit() {
		changeCallback = new ChangeCallBackImpl(this) {
			@Override
			public void callListener(String key, String prev, String value) {
				checker.callListener(key, prev, value);
			}
		};
		getScene().startSetupProcess();
		postInit(getScene());
		Platform.runLater(()->initLater(getScene()));
		getScene().stopSetupProcess();
		return (T) this;
	}
	protected abstract void postInit(AppScene scene);
	protected void initLater(AppScene scene){}
	protected void initSubControllers(BasicController... controllers) {
		for(BasicController controller: controllers) {
			controller.setScene(scene);
			controller.postInit();
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
	public ResourceBundle getMessages() {
		return messages;
	}
}
