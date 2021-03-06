package milandr_ex.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import milandr_ex.McuType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import milandr_ex.data.*;
import milandr_ex.utils.LoaderUtils;
import milandr_ex.utils.guava.Lists;

public class RootLayoutController extends BasicController {

	public void genSmpl(ActionEvent actionEvent) {
		getScene().genKind(CodeGenerator.GenKind.SIMPLE);
	}
	public void genCplx(ActionEvent actionEvent) {
		getScene().genKind(CodeGenerator.GenKind.COMPLEX);
	}
	public void genBldr(ActionEvent actionEvent) {
		getScene().genKind(CodeGenerator.GenKind.BUILDER);
	}
	public void genMdl(ActionEvent actionEvent) {
		getScene().genKind(CodeGenerator.GenKind.MODEL);
	}

	public enum MenuKind {
		NONE, PROJECT, PROCESSOR, PINOUTS, CLOCK, TIMER, ERROR
	}

	@FXML
	private Menu mnuPrj;
	@FXML
	private Menu mnuEdit;
	@FXML
	private Menu mnuProc;
	@FXML
	private Menu mnuPins;
	@FXML
	private Menu mnuClk;
	@FXML
	private Menu mnuTmr;
	@FXML
	private Menu mnuOpts;
	@FXML
	private Menu mnuHelp;
	@FXML
	private Menu mnuExit;

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
		ChooseController.setDefaultMCU(scene.isDebugMode());
		Platform.runLater(() -> {
			scene.setMcuMain(DeviceFactory.getDevice("LQFP64").getMcu());
			LoadProject(true);
		});
		if (!scene.isTestMode()) {
			mnuProc.setVisible(false);
			mnuPins.setVisible(false);
			mnuClk.setVisible(false);
			mnuTmr.setVisible(false);
			mnuOpts.setVisible(false);
			mnuExit.setVisible(false);
		}
		if (!scene.isEditMode()) {
			mnuEdit.setVisible(false);
		}
	}

	public void LoadProjectFromFile(ResourceBundle messages, File selectedFile) {
		if (selectedFile != null) {
			getScene().setPinoutsModel(PinoutsModel.load(selectedFile));
			ChooseController.setDefaultMCU(getScene().isDebugMode());
			getScene().setMcuMain(DeviceFactory.getDevice(
					getScene().getPinoutsModel().getSelectedBody()).getMcu());
			LoadProject(false);
		}
	}
	public void LoadProject(Boolean clean) {
		McuType mcu = getScene().getMcuMain();
		if (mcu != null){
			if (clean || getScene().getPinoutsModel() == null) {
				getScene().setPinoutsModel(PinoutsModel.get(mcu));
				getScene().getPinoutsModel().clearSelected();
			}
			LoaderUtils.initAnyLayout(getScene(), "mainMCU", "main.title", false);
			getScene().observe("pinouts");
			LoaderUtils.initStage(getScene().getAppStage(), 1200, 800, false);
		}
	}

	public void NewProject(ResourceBundle messages) {
//		LoaderUtils.initAnyLayout(getScene(), "selectDevice", "main.choose.title");
		FXMLLoader loader = LoaderUtils.loadLayout(messages, "selectDevice");
		AnchorPane page = loader.getRoot();
		
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
		LoadProject(true);
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
				CloseProject();
				break;
		}
	}

	public void CloseProject() {
		getScene().setMcuMain(null);
		PinoutsModel model = getScene().getPinoutsModel();
		if (model != null) {
			model.setHasUnsavedChanges(false);
			model.clearSelected();
		}
		LoaderUtils.initAnyLayout(getScene(), "Main", "main.title", false);
	}

	@FXML
	private void handleNew(ActionEvent event) throws IOException{
		MenuItem mi = (MenuItem) event.getSource();
		switch (parseMenuKind(mi)) {
			case PROJECT:
			case PROCESSOR:
				NewProject(getMessages());
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
			LoadProjectFromFile(getMessages(), selectedFile);
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
