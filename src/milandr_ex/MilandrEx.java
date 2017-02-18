package milandr_ex;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.aquafx_project.AquaFx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import milandr_ex.data.Constants;
import milandr_ex.data.PinoutsModel;


public class MilandrEx extends Application {
	public static Stage primaryStage;
	public static BorderPane rootLayout;
	public static AnchorPane mainLayout;
	public static String file = "";
	public static PinoutsModel pinoutsModel;
	private static Locale locale = new Locale("ru", "RU");
	public static McuType mcuMain = null;
	private static ResourceBundle bundle;// = ResourceBundle.getBundle("resourse/messages", locale);


	public MilandrEx() {
		bundle = Constants.loadBundle("messages", "ru");
	}

	@Override
	public void start(Stage primStage) {
		primaryStage = primStage;
		primaryStage.setTitle(bundle.getString("main.title"));
//		this.primaryStage.getIcons().add(new Image("file:resourses/images/recept1.png"));
		AquaFx.style();

		initRootLayout();
		
		showMain();
	}

	/**
	 * Initializes the root layout
	 * 
	 * @param args
	 */
	
	public void initRootLayout(){
		try{
			//load root layout from fmxl file
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(bundle);
			loader.setLocation(MilandrEx.class.getResource("model/RootLayout.fxml"));
			
			rootLayout = (BorderPane) loader.load();

			//show the scene containing root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setWidth(800);
			primaryStage.setHeight(600);
			primaryStage.show();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Show recept overview inside root layout
	 * 
	 * @param args
	 */
	public void showMain(){
		try{
			//load receipt overview
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(bundle);
			loader.setLocation(MilandrEx.class.getResource("model/Main.fxml"));
			mainLayout = (AnchorPane) loader.load();
			
			
			//set receipt overview into center of root layout
			rootLayout.setCenter(mainLayout);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * Returns the main stage
	 */
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
