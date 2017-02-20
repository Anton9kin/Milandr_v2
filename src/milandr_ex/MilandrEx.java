package milandr_ex;

import com.aquafx_project.AquaFx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.model.BasicController;
import milandr_ex.model.RootLayoutController;

import java.io.IOException;
import java.util.ResourceBundle;


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
		AquaFx.style();
		showMain(initRootLayout(primStage));
	}

	/**
	 * Initializes the root layout
	 * 
	 * @param stage
	 */
	
	public BorderPane initRootLayout(Stage stage){
		try{
			ResourceBundle bundle = Constants.loadBundle("messages", "ru");
			stage.setTitle(bundle.getString("main.title"));
			//load root layout from fmxl file
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(bundle);
			loader.setLocation(MilandrEx.class.getResource("model/RootLayout.fxml"));
			BorderPane rootLayout = loader.load();

			//show the scene containing root layout
			AppScene scene = new AppScene(rootLayout);
			setScene(scene);
			scene.setAppStage(stage);
			scene.setBundle(bundle);

			stage.setScene(scene);
			stage.setWidth(800);
			stage.setHeight(600);
			stage.show();
			scene.setRootController(addSceneToController(loader, scene).postInit());
			return rootLayout;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Show recept overview inside root layout
	 * 
	 * @param rootLayout
	 */
	public void showMain(BorderPane rootLayout){
		try{
			//load receipt overview
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(scene.getBundle());
			loader.setLocation(MilandrEx.class.getResource("model/Main.fxml"));
			AnchorPane mainLayout = loader.load();

			//set receipt overview into center of root layout
			rootLayout.setCenter(mainLayout);
			getScene().setRootLayout(rootLayout);
			getScene().setMainLayout(mainLayout);
			addSceneToController(loader, scene).postInit();

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private BasicController addSceneToController(FXMLLoader loader, AppScene scene) {
		BasicController controller = loader.getController();
		controller.setScene(scene);
		return controller;
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
