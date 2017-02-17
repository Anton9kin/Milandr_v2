package milandr_ex.model;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
	
	@FXML
	private void initialize(){
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
		try {
			RootLayoutController.NewProject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
