package milandr_ex.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import milandr_ex.McuType;
import milandr_ex.MilandrEx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;
import milandr_ex.data.DeviceFactory;
import milandr_ex.data.PinoutsModel;

public class RootLayoutController extends BasicController {

	private ResourceBundle messages;
	public enum MenuKind {
		NONE, PROJECT, PROCESSOR, PINOUTS, CLOCK, TIMER, ERROR
	}

	public RootLayoutController() {

	}

	@SuppressWarnings("unused")
	@FXML
	private void initialize() {
//		messages = getScene().getBundle();
		loadOptions();
	}

	@Override
	protected void postInit(AppScene scene) {
		ChooseController.setDefaultMCU();
		scene.setMcuMain(DeviceFactory.getDevice("LQFP64").getMcu());
		Platform.runLater(() -> LoadProject(getMessages()));
	}

	public void LoadProjectFromFile(ResourceBundle messages, File selectedFile) {
		if (selectedFile != null) {
			getScene().setPinoutsModel(PinoutsModel.load(selectedFile));
			ChooseController.setDefaultMCU();
			getScene().setMcuMain(DeviceFactory.getDevice(
					getScene().getPinoutsModel().getSelectedBody()).getMcu());
			LoadProject(messages);
		}
	}
	public void LoadProject(ResourceBundle messages) {
		if (messages == null) messages = Constants.loadBundle("messages", "ru");
		McuType mcu = getScene().getMcuMain();
		if (mcu != null){
			String newTitle = messages.getString("main.title") + " - " + mcu.getProp("type");
			URL location = MilandrEx.class.getResource("view/mainMCU.fxml");
			FXMLLoader loader = new FXMLLoader(location);
			loader.setResources(messages);
			loader.setLocation(location);
			if (getScene().getPinoutsModel() == null) {
				getScene().setPinoutsModel(new PinoutsModel().setSelectedBody(mcu.getProp("pack")));
			}
			getScene().setMainLayout(loaderLoad(loader));
			BasicController main = loader.getController();
			main.setScene(getScene());
			main.postInit();
			getScene().observe("pinouts");
			getScene().getRootLayout().setCenter(getScene().getMainLayout());
			Stage stage = getScene().getAppStage();
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.setTitle(newTitle);
			stage.centerOnScreen();
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

	public void NewProject(ResourceBundle messages) {
		if (messages == null) messages = Constants.loadBundle("messages", "ru");
		URL location = MilandrEx.class.getResource("view/selectDevice.fxml");
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(messages);
		loader.setLocation(location);
		AnchorPane page = loaderLoad(loader);
		
		//Create dialog winStage
		Stage chooseStage = new Stage();
		chooseStage.initModality(Modality.WINDOW_MODAL);
		chooseStage.initOwner(getScene().getAppStage());
		Scene scene = new Scene(page);
		chooseStage.setScene(scene);
		chooseStage.setTitle(messages.getString("main.choose.title"));

		ChooseController chContr = loader.getController();
		chContr.setDialogStage(chooseStage);
		chContr.setScene(getScene());
		chContr.postInit();
		chooseStage.showAndWait();
		LoadProject(messages);
	}

	private MenuKind parseMenuKind(MenuItem item) {
		Menu mn = item.getParentMenu();
		String menuCaption = mn.getText();
		return MenuKind.valueOf(menuCaption.toUpperCase());
	}

	@FXML
	public void handleClose(ActionEvent event) {
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				CloseProject(messages);
				break;
		}
	}

	public void CloseProject(ResourceBundle messages) {
		try{
			AppScene scene = getScene();
			scene.setMcuMain(null);
			BorderPane rootLayout = scene.getRootLayout();
			//load receipt overview
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(scene.getBundle());
			loader.setLocation(MilandrEx.class.getResource("view/Main.fxml"));
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

	public void setLastSelectedPath(String lastSelectedPath) {
		if (lastSelectedPath == null) lastSelectedPath = "";
		this.lastSelectedPath = lastSelectedPath;
	}

	@FXML
    private void handleOpen(ActionEvent event) {
    	loadOptions();
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				doOpenEvent();
				break;
		}
    }

	public void doOpenEvent() {
		fillFileChooser();
		File selectedFile = chooser.showOpenDialog(null);
		if (selectedFile != null) {
			setLastSelectedPath(selectedFile.getAbsolutePath());
			saveOptions();
			LoadProjectFromFile(messages, selectedFile);
		}
	}

	private void fillFileChooser() {
		if (!lastSelectedPath.isEmpty()) {
			String pathname = lastSelectedPath.substring(0, lastSelectedPath.lastIndexOf(File.separator ));
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
		return saveCurrentProject(chooser.showSaveDialog(null));
	}
	private File saveCurrentProject(File selectedFile) {
		if (selectedFile != null) {
			String path = selectedFile.getAbsolutePath();
			if (!path.endsWith(".mpr")) {
				selectedFile = new File(path + ".mpr");
			}
			setLastSelectedPath(selectedFile.getAbsolutePath());
			saveOptions();
			if (getScene().getPinoutsModel() != null) {
				getScene().getPinoutsModel().save(selectedFile);
			}
		}
		return selectedFile;
	}

	@FXML
	private void handleAbout(){
		
	}
	
	@FXML
    private void handleExit() {
		saveOptions();
		PinoutsModel model = getScene().getPinoutsModel();
		if (model != null && model.isHasUnsavedChanges()) {
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
			if (props.length < 2) continue;
			if (props[0].equals("lastPath")) setLastSelectedPath(props[1]);
		}
	}

	private void saveOptions() {
		List<String> options = Lists.newArrayList();
		options.add("lastPath=" + lastSelectedPath);
		Constants.saveTxtList(new File("opts.cfg"), options, true);
	}
}
