package milandr_ex.model.mcu.ext;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;

public class MCUCanController extends MCUExtPairController {

	@FXML
	private ComboBox<String> div;
	ObservableList<String> divList = FXCollections.
			observableArrayList("/1", "/2", "/4", "/8", "/16", "/32", "/64", "/128");
	
	@FXML
	private ComboBox<String> speed;
	ObservableList<String> speedList = FXCollections.
			observableArrayList("10 000", "20 000", "50 000", "100 000", "125 000", "250 000", "500 000", "1000 000");
	
	@FXML
	private TextField listChanel;
	
	@FXML
	private TextField tq;
	@FXML
	private TextField seg1;
	@FXML
	private TextField seg2;
	@FXML
	private TextField pseg;
	
	@FXML
	private TextField realSpeed;
	@FXML
	private TextField errSpeed;
	@FXML
	private GridPane can_gpio;

	@FXML
	private void initialize(){

	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.CAN);
		div.setItems(divList);
		speed.setItems(speedList);
	}

	@Override
	protected void initLater(AppScene scene) {
		super.initLater(scene);
		div.getSelectionModel().select(0);
		speed.getSelectionModel().select(0);
	}

	@Override
	protected Parent getGPIOControl() {
		return can_gpio;
	}
}
