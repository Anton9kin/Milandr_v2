package milandr_ex.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import milandr_ex.MilandrEx;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.model.BasicController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by lizard on 21.02.17 at 16:36.
 */
public class LoaderUtils {
	public static void loadImage(Class clazz, ImageView target, String source) {
		Image image;URL imageUrl = clazz.getClassLoader().getResource("resourse/" + source);
		if (imageUrl != null) {
			image = new Image(imageUrl.toString());
			target.setImage(image);
		}
	}


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
		scene.getStylesheets().add("milandr_ex/css/" + cssName + ".css");
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
		AppScene scene = (AppScene) stage.getScene();
		stage.setTitle(scene.getBundle().getString("main.title"));
		BasicController bc = addSceneToController(loader, scene).preInit();
		initStage(stage, 800, 600);
		scene.setRootController(bc.postInit());
		return scene.getRootLayout();
	}

	public static Region initAnyLayout(AppScene scene, String viewName, String titleKey) {
		scene.clearObservers();
		ResourceBundle bundle = scene.getBundle();
		FXMLLoader loader = loadLayout(bundle, viewName);
		scene.getAppStage().setTitle(bundle.getString(titleKey));
		BasicController bc = addSceneToController(loader, scene).preInit();
		setupNewLayout(scene, loader);
		bc.postInit();
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
