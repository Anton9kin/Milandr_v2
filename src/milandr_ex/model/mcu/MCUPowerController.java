package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PowerController {

	@FXML
	private ComboBox<String> ucc;
	ObservableList<String> uccList = FXCollections.
			observableArrayList("2.0 B", "2.2 B", "2.4 B", "2.6 B", "2.8 B", "3.0 B", "3.2 B", "3.4 B");
	
	@FXML
	private ComboBox<String> bucc;
	ObservableList<String> buccList = FXCollections.
			observableArrayList("1.8 B", "2.2 B", "2.6 B", "3.0 B");
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCansel;
	
	
	@FXML
	private void initialize(){
		ucc.setItems(uccList);
		ucc.getSelectionModel().select(0);
		
		bucc.setItems(buccList);
		bucc.getSelectionModel().select(0);
		
	}
}
