package milandr_ex.model;

import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import milandr_ex.utils.LoaderUtils;

import static milandr_ex.utils.LoaderUtils.loadImage;

/**
 * Splash Screen Loader for show while app will be initialized
 * Created by lizard on 18.03.17 at 13:17.
 */
public class SplashScreenLoader extends Preloader {
	@FXML
	private ImageView splashIm;
	private Stage splashScreen;

	@Override
	public void start(Stage stage) throws Exception {
		splashScreen = stage;
		splashScreen.setScene(createScene());
		splashScreen.show();
	}

	@FXML
	private void initialize() {
		loadImage(getClass(), splashIm, "milandr_logo2.jpg");
	}

	public Scene createScene() {
		FXMLLoader rootLoader = LoaderUtils.loadLayout(null, "Splash");
		return new Scene(rootLoader.getRoot(), 300, 300);
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification notification) {
		if (notification instanceof StateChangeNotification) {
			splashScreen.hide();
		}
	}
}
