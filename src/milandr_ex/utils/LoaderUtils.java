package milandr_ex.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import milandr_ex.MilandrEx;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.model.BasicController;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by lizard on 21.02.17 at 16:36.
 */
public class LoaderUtils {
	public static FXMLLoader loadLayout(ResourceBundle bundle, String viewName) {
		if (bundle == null) bundle = Constants.loadBundle("messages", "ru");
		try{
			//load root layout from fmxl file
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(bundle);
			loader.setLocation(MilandrEx.class.getResource("view/" + viewName + ".fxml"));
			loader.load();
			return loader;
		} catch (IOException ioe) {

		}
		return null;
	}

	public static AppScene getAppScene(Stage stage, ResourceBundle bundle, BorderPane rootLayout, String cssName) {
		//show the scene containing root layout
		AppScene scene = new AppScene(rootLayout);
		stage.setScene(scene);
		scene.setAppStage(stage);
		scene.setBundle(bundle);
		scene.getStylesheets().add("milandr_ex/" + cssName + ".css");
		return scene;
	}

	public static AppScene getAppScene(Stage stage, ResourceBundle bundle, BorderPane rootLayout) {
		return getAppScene(stage, bundle, rootLayout, "application");
	}

	public static void initStage(Stage stage, int width, int height) {
		stage.setWidth(width);
		stage.setHeight(height);
		stage.centerOnScreen();
		stage.show();
	}

	public static BorderPane initRootLayout(Stage stage) {
		FXMLLoader loader = loadLayout(null, "RootLayout");
		getAppScene(stage, loader.getResources(), loader.getRoot());
		stage.setTitle(((AppScene)stage.getScene()).getBundle().getString("main.title"));

		initStage(stage, 800, 600);
		AppScene scene = (AppScene) stage.getScene();
		scene.setRootController(addSceneToController(loader, scene).postInit());
		return scene.getRootLayout();
	}

	public static Region initAnyLayout(AppScene scene, String viewName, String titleKey) {
		scene.clearObservers();
		ResourceBundle bundle = scene.getBundle();
		FXMLLoader loader = loadLayout(bundle, viewName);
		scene.getAppStage().setTitle(bundle.getString(titleKey));
		setupNewLayout(scene, loader);
		addSceneToController(loader, scene).postInit();
		return scene.getRootLayout();
	}

	private static void setupNewLayout(AppScene scene, FXMLLoader loader) {
		scene.setBundle(loader.getResources());
		initStage(scene.getAppStage(), 800, 600);
		BorderPane rootLayout = scene.getRootLayout();
		AnchorPane mainLayout = loader.getRoot();
		rootLayout.setCenter(mainLayout);
		scene.setMainLayout(mainLayout);
	}

	public static BasicController addSceneToController(FXMLLoader loader, AppScene scene) {
		BasicController controller = loader.getController();
		controller.setScene(scene);
		return controller;
	}
}
