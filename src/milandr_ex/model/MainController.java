package milandr_ex.model;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import milandr_ex.data.AppScene;
import milandr_ex.data.Constants;

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
		loadImage(openIm, "open.png");
		loadImage(newIm, "new.png");
		loadImage(helpIm, "helpicon.png");
		loadImage(idIm, "milandr_logo2.jpg");
	}

	@Override
	protected void postInit(AppScene scene) {
		//do nothing
	}

	private void loadImage(ImageView target, String source) {
		Image image;URL imageUrl = getClass().getClassLoader().getResource("resourse/" + source);
		if (imageUrl != null) {
			image = new Image(imageUrl.toString());
			target.setImage(image);
		}
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
