package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MCUIwdgController {

	@FXML
	private ComboBox<String> div;
	ObservableList<String> divList = FXCollections.
			observableArrayList("/4", "/8", "/16", "/32", "/64", "/128", "/256", "/512");
	
	@FXML
	private TextField val;
	
	@FXML
	private ComboBox<String> unit;
	ObservableList<String> unitList = FXCollections.
			observableArrayList("с", "мс", "мкс", "Гц", "кГц", "МГц");
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCansel;
	
	
	@FXML
	private void initialize(){
		div.setItems(divList);
		div.getSelectionModel().select(0);
		
		unit.setItems(unitList);
		unit.getSelectionModel().select(0);
	}
}
