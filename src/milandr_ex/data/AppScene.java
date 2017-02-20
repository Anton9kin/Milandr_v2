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
	public BorderPane rootLayout;
	public AnchorPane mainLayout;
	public PinoutsModel pinoutsModel;
	public McuType mcuMain = null;
	private ResourceBundle bundle;// = ResourceBundle.getBundle("resourse/messages", locale);
	private Map<String, List<ModelObserver>> observers = Maps.newHashMap();
	private BasicController rootController;
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

	public McuType getMcuMain() {
		return mcuMain;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public PinoutsModel getPinoutsModel() {
		return pinoutsModel;
	}

	public BorderPane getRootLayout() {
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
}