package milandr_ex.model;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import milandr_ex.MilandrEx;
import milandr_ex.utils.LoaderUtils;

import static milandr_ex.utils.LoaderUtils.loadImage;

/**
 * Splash Screen Loader for show while app will be initialized
 * Created by lizard on 18.03.17 at 13:17.
 */
public class SplashScreenLoader extends Preloader {
	@FXML
	private ImageView splashIm;
	@FXML
	private ProgressBar loadProgress;
	@FXML
	private Label progressText;
	private Stage splashScreen;

	@Override
	public void start(Stage stage) throws Exception {
		splashScreen = stage;
		splashScreen.setScene(createScene());
		splashScreen.setAlwaysOnTop(true);
		splashScreen.show();
	}

	public static void makeFadeSplash(final Stage stage, Parent splashLayout) {
		if (stage == null || splashLayout == null) return;
		if (stage.getScene() == null) {
			stage.hide();
			return;
		}
		FadeTransition fadeSplash = new FadeTransition(Duration.seconds(3), splashLayout);
		fadeSplash.setFromValue(1.0);
		fadeSplash.setToValue(0.5);
		fadeSplash.setOnFinished(actionEvent -> stage.hide());
		fadeSplash.play();
	}

	@FXML
	private void initialize() {
		loadImage(getClass(), splashIm, "milandr_logo2.jpg");
		progressText.setText("Loading hobbits with pie . . .");
	}

	public Scene createScene() {
		FXMLLoader rootLoader = LoaderUtils.loadLayout(null, "Splash");
		LoaderUtils.setSplashStage(splashScreen);
		return new Scene(rootLoader.getRoot(), 300, 300);
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification notification) {
		if (notification instanceof StateChangeNotification) {
			Application app = ((StateChangeNotification) notification).getApplication();
			if (app instanceof MilandrEx) {
				MilandrEx mApp = (MilandrEx) app;
				if (splashScreen != null) {
					((MilandrEx) app).getScene().setSplashStage(splashScreen);
					((MilandrEx) app).getScene().setSplashLayout((AnchorPane) splashScreen.getScene().getRoot());
				}
				if (!mApp.getScene().isSetupInProcess()) {
					makeFadeSplash(splashScreen, splashScreen.getScene().getRoot());
				}
			}
		}
	}
}
