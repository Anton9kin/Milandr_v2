package milandr_ex;

//import com.aquafx_project.AquaFx;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import milandr_ex.data.AppScene;
import milandr_ex.model.RootLayoutController;
import milandr_ex.utils.LoaderUtils;


public class MilandrEx extends Application {
	private AppScene scene;

	public MilandrEx() {
	}

	public void setScene(AppScene scene) {
		this.scene = scene;
	}

	public AppScene getScene() {
		return scene;
	}

	@Override
	public void start(Stage primStage) {
//		this.primaryStage.getIcons().add(new Image("file:resourses/images/recept1.png"));
//		AquaFx.style();
		initRootLayout(primStage);
		showMain();
	}

	/**
	 * Initializes the root layout
	 * 
	 * @param stage
	 */
	
	public BorderPane initRootLayout(Stage stage){
		LoaderUtils.initRootLayout(stage);
		setScene((AppScene) stage.getScene());
		return getScene().getRootLayout();
	}

	/**
	 * Show recept overview inside root layout
	 * 
	 */
	public void showMain(){
		((RootLayoutController) getScene().getRootController()).CloseProject();
	}

	/**
	 * 
	 * Returns the main stage
	 */
	public Stage getPrimaryStage(){
		return scene.getAppStage();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
