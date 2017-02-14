package Milandr_ex.model;

import java.io.IOException;

import Milandr_ex.Milandr_ex;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootLayoutController {
	
	@FXML
	private void handleNew() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Milandr_ex.class.getResource("model/selectDevice.fxml"));
		AnchorPane page = (AnchorPane) loader.load();
		
		//Create dialog winStage
		Stage chooseStage = new Stage();
		chooseStage.setTitle("Выберите контроллер");
		chooseStage.initModality(Modality.WINDOW_MODAL);
		chooseStage.initOwner(Milandr_ex.primaryStage);
		Scene scene = new Scene(page);
		chooseStage.setScene(scene);
		
		chooseController chContr = loader.getController();
		chContr.setDialogStage(chooseStage);
		
		chooseStage.showAndWait();
		
		if (Milandr_ex.mcuMain != null){
			loader = new FXMLLoader();
			
			loader.setLocation(Milandr_ex.class.getResource("model/mainMCU.fxml"));
			Milandr_ex.mainLayout = (AnchorPane)loader.load();
			Milandr_ex.rootLayout.setCenter(Milandr_ex.mainLayout);
			Milandr_ex.primaryStage.setWidth(1200);
			Milandr_ex.primaryStage.setHeight(800);
			Milandr_ex.primaryStage.setTitle("Генератор кода - " + Milandr_ex.mcuMain.getType());
			Milandr_ex.primaryStage.centerOnScreen();
		}
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
