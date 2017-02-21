package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SystickController {

	@FXML
	private ComboBox<String> ist;
	ObservableList<String> istList = FXCollections.
			observableArrayList("LSI", "HCLK");
	
	@FXML
	private CheckBox enInt;
	
	@FXML
	private ComboBox<String> mode;
	ObservableList<String> modeList = FXCollections.
			observableArrayList("Таймер", "Задержка");
	
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
		ist.setItems(istList);
		ist.getSelectionModel().select(0);
		
		mode.setItems(modeList);
		mode.getSelectionModel().select(0);
		
		unit.setItems(unitList);
		unit.getSelectionModel().select(0);
	}
}
