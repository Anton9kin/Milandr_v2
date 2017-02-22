package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MCUWwdgController {

	@FXML
	private ComboBox<String> div;
	ObservableList<String> divList = FXCollections.
			observableArrayList("/1", "/2", "/4", "/8");
	
	@FXML
	private CheckBox enInt;
	
	@FXML
	private TextField valCount;
	
	@FXML
	private TextField valWin;
	
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCansel;
	
	
	@FXML
	private void initialize(){
		div.setItems(divList);
		div.getSelectionModel().select(0);
	}
}
