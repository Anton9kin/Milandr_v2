package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MCUAdcController {

	@FXML
	private ComboBox<String> opU;
	ObservableList<String> opUList = FXCollections.
			observableArrayList("Внутренее", "Внешнее");
	
	@FXML
	private ComboBox<String> typeStart;
	ObservableList<String> typeStartList = FXCollections.
			observableArrayList("Одиночное", "Последовательное");
	
	@FXML
	private CheckBox seriesMes;
	
	@FXML
	private CheckBox enTemperature;
	
	@FXML
	private TextField listChanel;
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCancel;
	
	
	@FXML
	private void initialize(){
		opU.setItems(opUList);
		opU.getSelectionModel().select(0);
		
		typeStart.setItems(typeStartList);
		typeStart.getSelectionModel().select(0);
	}
}
