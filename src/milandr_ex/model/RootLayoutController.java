package milandr_ex.model;

import java.io.IOException;

import milandr_ex.MilandrEx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootLayoutController {
	
	public static void NewProject() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MilandrEx.class.getResource("model/selectDevice.fxml"));
		AnchorPane page = (AnchorPane) loader.load();
		
		//Create dialog winStage
		Stage chooseStage = new Stage();
		chooseStage.setTitle("Выберите контроллер");
		chooseStage.initModality(Modality.WINDOW_MODAL);
		chooseStage.initOwner(MilandrEx.primaryStage);
		Scene scene = new Scene(page);
		chooseStage.setScene(scene);
		
		ChooseController chContr = loader.getController();
		chContr.setDialogStage(chooseStage);
		
		chooseStage.showAndWait();
		
		if (MilandrEx.mcuMain != null){
			loader = new FXMLLoader();
			
			loader.setLocation(MilandrEx.class.getResource("model/mainMCU.fxml"));
			MilandrEx.mainLayout = (AnchorPane)loader.load();
			MilandrEx.rootLayout.setCenter(MilandrEx.mainLayout);
			MilandrEx.primaryStage.setWidth(1200);
			MilandrEx.primaryStage.setHeight(800);
			MilandrEx.primaryStage.setTitle("Генератор кода - " + MilandrEx.mcuMain.getProp("type"));
			MilandrEx.primaryStage.centerOnScreen();
		}
	}
	
	@FXML
	private void handleNew() throws IOException{
		NewProject();
	}
	
    @FXML
    private void handleOpen() {
    }
	
	@FXML
	private void handleSave(){
	}
	
	@FXML
    private void handleSaveAs() {
	}
	
	@FXML
	private void handleAbout(){
		
	}
	
	@FXML
    private void handleExit() {
        System.exit(0);
    }
}
