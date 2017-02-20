package milandr_ex.model;

import javafx.scene.layout.GridPane;
import milandr_ex.data.*;
import milandr_ex.McuType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ChooseController extends BasicController {

	private Stage dialogStage;

	private ObservableList<McuType> mcuData = FXCollections.observableArrayList();
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCansel;
	@FXML
	private TableView<McuType> mcuTable;
	@FXML
	private TableColumn<McuType, String> typeColumn;
	@FXML
	private TableColumn<McuType, String> packColumn;
	@FXML
	private TableColumn<McuType, String> flashColumn;
	@FXML
	private TableColumn<McuType, String> ramColumn;
	@FXML
	private TableColumn<McuType, String> ioColumn;
	
	@FXML
	private GridPane contLabel;

	
	public ChooseController(){
		
	}

	@SuppressWarnings("unused")
	@FXML
	private void initialize() {
		typeColumn.setCellValueFactory(cellData->cellData.getValue().getSProp("type"));
		packColumn.setCellValueFactory(cellData->cellData.getValue().getSProp("pack"));
		flashColumn.setCellValueFactory(cellData->cellData.getValue().getSProp("flash"));
		ramColumn.setCellValueFactory(cellData->cellData.getValue().getSProp("ram"));
		ioColumn.setCellValueFactory(cellData->cellData.getValue().getSProp("io"));
		mcuTable.setEditable(false);


		initView();
		setDefaultMCU(mcuTable);
		
		showMCUDetails(null);
		
		mcuTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showMCUDetails(newValue));
		
	}

	@Override
	protected void postInit(AppScene scene) {
		//do nothing
	}

	private void initView(){
		String[] names = PropsFactory.nameProps().toArray(new String[]{});
		for(int i = 0; i < names.length; i++) {
			PropsFactory.Prop prop = PropsFactory.getProp(names[i]);
			Label caption = new Label(prop.getText());
			contLabel.getChildren().add(caption);
			GridPane.setRowIndex(caption, i);
			Label label = new Label(prop.getName());
			contLabel.getChildren().add(label);
			prop.setLabel(label);
			GridPane.setRowIndex(label, i);
			GridPane.setColumnIndex(label, 1);
		}
	}
	public static void setDefaultMCU(){
		setDefaultMCU(null);
	}
	@SuppressWarnings("unchecked")
	private static void setDefaultMCU(TableView mcuTable){
		if (mcuTable != null) mcuTable.getItems().clear();

		Device d;
		d = DeviceFactory.createDefDevice("1986ВЕ92У", "Н18.64-1В");
		DeviceFactory.updateDevice(d, null, new int[]{8, 11, 3, 8, 8, 7}, new int[]{404, 405});
		if (mcuTable != null) mcuTable.getItems().add(d.getMcu());

		d = DeviceFactory.createDefDevice("К1986ВЕ92QI", "LQFP64");
		DeviceFactory.updateDevice(d, null, new int[]{8, 11, 3, 8, 8, 6}, new int[]{404, 405});
		if (mcuTable != null) mcuTable.getItems().add(d.getMcu());

		d = DeviceFactory.createDefDevice("1986ВЕ91T", "4229.132-3", 96);
		d.setDac(2).setComporator(3).setExtWire(32);
		if (mcuTable != null) mcuTable.getItems().add(d.getMcu());

		d = DeviceFactory.createDefDevice("1986ВЕ94T", "4229.132-3", 96);
		d.setDac(2).setComporator(3).setExtWire(32);
		if (mcuTable != null) mcuTable.getItems().add(d.getMcu());

		d = DeviceFactory.createDefDevice("1986ВЕ93У", "Н16.48-1В", 30);
		DeviceFactory.updateDevice(d, null, new int[]{8, 7, 1, 4, 7, 6}, new int[]{401, 404, 405});
		d.setI2c(0).setExtWire(0);
		if (mcuTable != null) mcuTable.getItems().add(d.getMcu());

		fillGenericDevices(mcuTable);
	}

	private static void fillGenericDevices(TableView mcuTable) {
		String[] bodies = {
			"H02.8-1B", "4116.8-3", "4105.14-16", "H02.16-1B", "H04.16-2B",
			"5119.16-A", "4140.20-1", "H06.24-1B", "5101.24-1K", "H09.28-1B",
			"4119.28-6", "4119.28-8", "4119.28-11", "5102.32-2K", "5156.40-1_H3_K",
			"H14.42-1B", "H16.48-1B", "4134.48-2", "5142.48-A", "5152.52-1",
			"H18.64-1B", "H18.64-2B", "H18.64-3B", "5134.64-6", "5153.64-1",
			"5153.64-2", "4229.132-3", "4245.240-5", "4244.256-3", "8303.576-1"};
		for(String body: bodies) {
			Device d = DeviceFactory.createDevice(body);
			if (mcuTable == null) continue;
			mcuTable.getItems().add(d.getMcu());
		}
	}

	@FXML
	private void handleCancel() {
		getScene().setMcuMain(null);
		dialogStage.close();
	}
	
	@FXML
	private void handleOK() {
		
		getScene().setMcuMain(mcuTable.getSelectionModel().getSelectedItem());
		
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Ваш выбор");
//		alert.setHeaderText("Ваш контроллер - " + chooseMCU.getType());
		
//		String param = String.format("Тип корпуса - %s\r\n", chooseMCU.getPack());
//		param += String.format("Объем ПЗУ - %s\r\n", chooseMCU.getFlash());
//		param += String.format("Объем ОЗУ - %s\r\n", chooseMCU.getRam());
//		param += String.format("Кол-во настраиваемых выводов - %s\r\n", chooseMCU.getIO());
//		
//		alert.setContentText(param);
//		alert.showAndWait();
		
		dialogStage.close();
	}
	
	
	public void showMCUDetails(McuType mcu){
		String[] names = PropsFactory.nameProps().toArray(new String[]{});
		for(int i = 0; i < names.length; i++) {
			PropsFactory.Prop prop = PropsFactory.getProp(names[i]);
			prop.getLabel().setText(mcu == null ? "" : mcu.getProp(prop.getName()));
		}
	}
	
	public ObservableList<McuType> getMCUData(){
		return mcuData;
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
