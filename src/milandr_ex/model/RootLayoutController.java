package milandr_ex.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.common.collect.Lists;
import com.sun.xml.internal.ws.api.model.MEP;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import milandr_ex.McuType;
import milandr_ex.MilandrEx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import milandr_ex.data.Constants;
import milandr_ex.data.DeviceFactory;
import milandr_ex.data.PinoutsModel;

import javax.swing.*;

public class RootLayoutController {

	private ResourceBundle messages;
	public enum MenuKind {
		NONE, PROJECT, PROCESSOR, PINOUTS, CLOCK, TIMER, ERROR
	}

	public RootLayoutController() {

	}

	@FXML
	private void initialize() {
		messages = Constants.loadBundle("messages", "ru");
		loadOptions();
	}

	public static void LoadProjectFromFile(ResourceBundle messages, File selectedFile) {
		if (selectedFile != null) {
			MilandrEx.pinoutsModel = PinoutsModel.load(selectedFile);
			ChooseController.setDefaultMCU();
			MilandrEx.mcuMain = DeviceFactory.getDevice(MilandrEx.pinoutsModel.getSelectedBody()).getMcu();
			LoadProject(messages);
		}
	}
	public static void LoadProject(ResourceBundle messages) {
		if (messages == null) messages = Constants.loadBundle("messages", "ru");
		McuType mcu = MilandrEx.mcuMain;
		if (mcu != null){
			URL location = MilandrEx.class.getResource("model/mainMCU.fxml");
			FXMLLoader loader = new FXMLLoader(location);
			loader.setResources(messages);
			loader.setLocation(location);
			if (MilandrEx.pinoutsModel == null) {
				MilandrEx.pinoutsModel = new PinoutsModel().setSelectedBody(mcu.getProp("pack"));
			}
			MilandrEx.mainLayout = loaderLoad(loader);
			MilandrEx.observe("pinouts");
			MilandrEx.rootLayout.setCenter(MilandrEx.mainLayout);
			MilandrEx.primaryStage.setWidth(1200);
			MilandrEx.primaryStage.setHeight(800);
			MilandrEx.primaryStage.setTitle(messages.getString("main.title") + " - " + MilandrEx.mcuMain.getProp("type"));
			MilandrEx.primaryStage.centerOnScreen();
		}
	}

	private static AnchorPane loaderLoad(FXMLLoader loader) {
		try {
			return  (AnchorPane)loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void NewProject(ResourceBundle messages) {
		if (messages == null) messages = Constants.loadBundle("messages", "ru");
		URL location = MilandrEx.class.getResource("model/selectDevice.fxml");
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(messages);
		loader.setLocation(location);
		AnchorPane page = loaderLoad(loader);
		
		//Create dialog winStage
		Stage chooseStage = new Stage();
		chooseStage.initModality(Modality.WINDOW_MODAL);
		chooseStage.initOwner(MilandrEx.primaryStage);
		Scene scene = new Scene(page);
		chooseStage.setScene(scene);
		chooseStage.setTitle(messages.getString("main.choose.title"));

		ChooseController chContr = loader.getController();
		chContr.setDialogStage(chooseStage);
		
		chooseStage.showAndWait();
		LoadProject(messages);
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
				NewProject(messages);
				break;
		}
	}

	FileChooser chooser = initFileChooser();
	String lastSelectedPath = "";

    @FXML
    private void handleOpen(ActionEvent event) {
    	loadOptions();
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				fillFileChooser();
				File selectedFile = chooser.showOpenDialog(null);
				if (selectedFile != null) {
					lastSelectedPath = selectedFile.getAbsolutePath();
					saveOptions();
					LoadProjectFromFile(messages, selectedFile);
				}
				break;
		}
    }

	private void fillFileChooser() {
		if (!lastSelectedPath.isEmpty()) {
			String pathname = lastSelectedPath.substring(0, lastSelectedPath.lastIndexOf("\\"));
			chooser.setInitialDirectory(new File(pathname));
			chooser.setInitialFileName(lastSelectedPath.substring(pathname.length() + 1));
		}
	}

	private FileChooser initFileChooser() {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Projects", "*.mpr");
		chooser.getExtensionFilters().add(filter);
		chooser.setSelectedExtensionFilter(filter);
		return chooser;
	}

	@FXML
	private void handleSave(ActionEvent event){
		loadOptions();
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				saveCurrentProject(new File(lastSelectedPath));
				break;
		}
	}

	@FXML
	private void handleSaveAs(ActionEvent event){
		loadOptions();
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				saveCurrentProject();
				break;
		}
	}

	private File saveCurrentProject() {
		fillFileChooser();
		return chooser.showSaveDialog(null);
	}
	private File saveCurrentProject(File selectedFile) {
		if (selectedFile != null) {
			lastSelectedPath = selectedFile.getAbsolutePath();
			saveOptions();
		}
		if (MilandrEx.pinoutsModel != null) {
			MilandrEx.pinoutsModel.save(selectedFile);
		}
		return selectedFile;
	}

	@FXML
	private void handleAbout(){
		
	}
	
	@FXML
    private void handleExit() {
		saveOptions();
		if (MilandrEx.pinoutsModel != null && MilandrEx.pinoutsModel.isHasUnsavedChanges()) {
			Optional<ButtonType> result = showAlertDialog().showAndWait();
			if (result.isPresent()) {
				if (result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
					if (saveCurrentProject() != null) System.exit(0);
				}
				if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
					System.exit(0);
				}
			}
		} else  System.exit(0);
    }

    private Dialog<ButtonType> showAlertDialog() {
		ButtonType dlgBtnOk = new ButtonType("Save", ButtonBar.ButtonData.APPLY);
		ButtonType dlBtnNo = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
		ButtonType dlgBtnCan = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Save confirmation");
		dialog.setContentText("We have unsaved changes, save or process quit directly?");
		dialog.getDialogPane().getButtonTypes().add(dlgBtnCan);
		dialog.getDialogPane().getButtonTypes().add(dlgBtnOk);
		dialog.getDialogPane().getButtonTypes().add(dlBtnNo);
		boolean disabled = false; // computed based on content of text fields, for example
//		dialog.getDialogPane().lookupButton(dlgBtnOk).lookupButton(dlgBtnNo).lookupButton(dlgBtnOk).setDisable(disabled);
		return dialog;
	}
	private void loadOptions() {
		List<String> opts = Constants.loadTxtStrings(new File("opts.cfg"));
		for(String opt: opts) {
			String[] props = opt.split("=");
			if (props[0].equals("lastPath")) lastSelectedPath = props[1];
		}
	}

	private void saveOptions() {
		List<String> options = Lists.newArrayList();
		options.add("lastPath=" + lastSelectedPath);
		Constants.saveTxtList(new File("opts.cfg"), options, true);
	}
}
