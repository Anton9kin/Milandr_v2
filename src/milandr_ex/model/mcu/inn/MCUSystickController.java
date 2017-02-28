package milandr_ex.model.mcu.inn;

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

public class MCUSystickController extends BasicController {

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
	
	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.SYST);
		ist.setItems(istList);
		mode.setItems(modeList);
		unit.setItems(unitList);
	}

	@Override
	protected void initLater(AppScene scene) {
		super.initLater(scene);
		ist.getSelectionModel().select(0);
		mode.getSelectionModel().select(0);
		unit.getSelectionModel().select(0);
	}
}
