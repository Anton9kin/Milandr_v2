package milandr_ex.model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import milandr_ex.data.Constants;

public class MainController {

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

	private ResourceBundle messages;
	@FXML
	private void initialize() {
		messages = Constants.loadBundle("messages", "ru");
		loadImage(openIm, "open.png");
		loadImage(newIm, "new.png");
		loadImage(helpIm, "helpicon.png");
		loadImage(idIm, "milandr_logo2.jpg");
	}

	private void loadImage(ImageView target, String source) {
		Image image;URL imageUrl = getClass().getClassLoader().getResource("resourse/" + source);
		if (imageUrl != null) {
			image = new Image(imageUrl.toString());
			target.setImage(image);
		}
	}

	@FXML
	private void handleNewImage(){
		RootLayoutController.NewProject(messages);
	}

	@FXML
	private void handleOpenImage(){
		RootLayoutController.LoadProject(messages);
	}
}
