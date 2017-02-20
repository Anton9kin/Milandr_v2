package milandr_ex.model;

import milandr_ex.data.AppScene;

import java.util.ResourceBundle;

/**
 * Created by lizard on 20.02.17 at 12:40.
 */
public abstract class BasicController {
	private AppScene scene;
	private ResourceBundle messages;

	public AppScene getScene() {
		return scene;
	}

	public void setScene(AppScene scene) {
		this.scene = scene;
		messages = scene.getBundle();
	}

	@SuppressWarnings("unchecked")
	public <T extends BasicController> T postInit() {
		postInit(getScene());
		return (T) this;
	}
	protected abstract void postInit(AppScene scene);

	public ResourceBundle getMessages() {
		return messages;
	}
}
