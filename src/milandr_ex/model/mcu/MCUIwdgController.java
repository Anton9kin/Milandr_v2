package milandr_ex.model.mcu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

public class MCUIwdgController extends BasicController {

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
	private void initialize(){
		div.setItems(divList);
		div.getSelectionModel().select(0);
		
		unit.setItems(unitList);
		unit.getSelectionModel().select(0);
	}

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.IWDG);
	}
}
