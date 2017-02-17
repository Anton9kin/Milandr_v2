package milandr_ex.model;

import java.io.IOException;

import com.sun.xml.internal.ws.api.model.MEP;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import milandr_ex.MilandrEx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootLayoutController {
	public enum MenuKind {
		NONE, PROJECT, PROCESSOR, PINOUTS, CLOCK, TIMER, ERROR
	}
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

	private MenuKind parseMenuKind(MenuItem item) {
		Menu mn = item.getParentMenu();
		String menuCaption = mn.getText();
		return MenuKind.valueOf(menuCaption.toUpperCase());
	}

	@FXML
	private void handleNew(ActionEvent event) throws IOException{
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				NewProject();
				break;
		}
	}
	
    @FXML
    private void handleOpen(ActionEvent event) {
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				break;
		}
    }
	
	@FXML
	private void handleSave(ActionEvent event){
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				break;
		}
	}
	
	@FXML
	private void handleAbout(){
		
	}
	
	@FXML
    private void handleExit() {
        System.exit(0);
    }
}
