package milandr_ex.model;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import milandr_ex.data.AppScene;

import static milandr_ex.utils.LoaderUtils.loadImage;


public class MainController extends BasicController {

	@FXML
	private ImageView openIm;
	@FXML
	private ImageView newIm;
	@FXML
	private ImageView helpIm;
	@FXML
	private ImageView idIm;
	
	
	public MainController() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@FXML
	private void initialize() {
		loadImage(getClass(), openIm, "open.png");
		loadImage(getClass(), newIm, "new.png");
		loadImage(getClass(), helpIm, "helpicon.png");
		loadImage(getClass(), idIm, "milandr_logo2.jpg");
	}

	@Override
	protected void postInit(AppScene scene) {
		//do nothing
	}

	private RootLayoutController getRootController() {
		return (RootLayoutController) getScene().getRootController();
	}

	@FXML
	private void handleNewImage(){
		getRootController().NewProject(getMessages());
	}

	@FXML
	private void handleOpenImage(){
		getRootController().doOpenEvent();
	}
}
