package Milandr_ex.model;

import Milandr_ex.Milandr_ex;
import Milandr_ex.mcuType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class chooseController {

	private Stage dialogStage;
	
	private ObservableList<mcuType> mcuData = FXCollections.observableArrayList();
	
	@FXML
	private Button btnOK;
	@FXML
	private Button btnCansel;
	@FXML
	private TableView<mcuType> mcuTable;
	@FXML
	private TableColumn<mcuType, String> typeColumn;
	@FXML
	private TableColumn<mcuType, String> packColumn;
	@FXML
	private TableColumn<mcuType, String> flashColumn;
	@FXML
	private TableColumn<mcuType, String> ramColumn;
	@FXML
	private TableColumn<mcuType, String> ioColumn;
	
	@FXML
	private Label typeLabel;
	@FXML
	private Label packLabel;
	@FXML
	private Label flashLabel;
	@FXML
	private Label ramLabel;
	@FXML
	private Label ioLabel;
	@FXML
	private Label coreLabel;
	@FXML
	private Label vccLabel;
	@FXML
	private Label freqLabel;
	@FXML
	private Label tempLabel;
	@FXML
	private Label usbLabel;
	@FXML
	private Label uartLabel;
	@FXML
	private Label canLabel;
	@FXML
	private Label spiLabel;
	@FXML
	private Label i2cLabel;
	@FXML
	private Label adcLabel;
	@FXML
	private Label dacLabel;
	@FXML
	private Label comporatorLabel;
	@FXML
	private Label extwireLabel;
	
	
	public chooseController(){
		
	}
	
	@FXML
	private void initialize(){
		
		typeColumn.setCellValueFactory(cellData->cellData.getValue().typeProperty());
		packColumn.setCellValueFactory(cellData->cellData.getValue().packProperty());
		flashColumn.setCellValueFactory(cellData->cellData.getValue().flashProperty());
		ramColumn.setCellValueFactory(cellData->cellData.getValue().ramProperty());
		ioColumn.setCellValueFactory(cellData->cellData.getValue().ioProperty());
		mcuTable.setEditable(false);
		
		setDefaultMCU();
		
		showMCUDetails(null);
		
		mcuTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showMCUDetails(newValue));
		
	}
	
	private void setDefaultMCU(){
		mcuType mil_198692Y = new mcuType("1986ВЕ92У", "Н18.64-1В", "128 кбайт", "32 кбайт", "43");
		mil_198692Y.setCore("ARM Cortex-M3");
		mil_198692Y.setVcc("2.2...3.6В");
		mil_198692Y.setFreq("80 МГц");
		mil_198692Y.setTemp("минус 60°С...+125°С");
		mil_198692Y.setUsb("Device и Host FS (до 12 Мбит/с), PHY");
		mil_198692Y.setUart("2");
		mil_198692Y.setCan("2");
		mil_198692Y.setSpi("2");
		mil_198692Y.setI2c("1");
		mil_198692Y.setAdc("2 (12 разрядов, 1 Мвыб/с, 8 каналов)");
		mil_198692Y.setDac("1 (12 разрядов)");
		mil_198692Y.setComporator("2 входа");
		mil_198692Y.setExtWire("8 разрядов");
		
		mcuType mil_K1986BE92QI	= new mcuType("К1986ВЕ92QI", "LQFP64", "128 кбайт", "32 кбайт", "43");
		mil_K1986BE92QI.setCore("ARM Cortex-M3");
		mil_K1986BE92QI.setVcc("2.2...3.6В");
		mil_K1986BE92QI.setFreq("80 МГц");
		mil_K1986BE92QI.setTemp("минус 60°С...+125°С");
		mil_K1986BE92QI.setUsb("Device и Host FS (до 12 Мбит/с), PHY");
		mil_K1986BE92QI.setUart("2");
		mil_K1986BE92QI.setCan("2");
		mil_K1986BE92QI.setSpi("2");
		mil_K1986BE92QI.setI2c("1");
		mil_K1986BE92QI.setAdc("2 (12 разрядов, 1 Мвыб/с, 8 каналов)");
		mil_K1986BE92QI.setDac("1 (12 разрядов)");
		mil_K1986BE92QI.setComporator("2 входа");
		mil_K1986BE92QI.setExtWire("8 разрядов");
		
		mcuType mil_1986ВЕ91T	= new mcuType("1986ВЕ91T", "4229.132-3", "128 кбайт", "32 кбайт", "96");
		mil_1986ВЕ91T.setCore("ARM Cortex-M3");
		mil_1986ВЕ91T.setVcc("2.2...3.6В");
		mil_1986ВЕ91T.setFreq("80 МГц");
		mil_1986ВЕ91T.setTemp("минус 60°С...+125°С");
		mil_1986ВЕ91T.setUsb("Device и Host FS (до 12 Мбит/с), PHY");
		mil_1986ВЕ91T.setUart("2");
		mil_1986ВЕ91T.setCan("2");
		mil_1986ВЕ91T.setSpi("2");
		mil_1986ВЕ91T.setI2c("1");
		mil_1986ВЕ91T.setAdc("2 (12 разрядов, 1 Мвыб/с, 16 каналов)");
		mil_1986ВЕ91T.setDac("2 (12 разрядов)");
		mil_1986ВЕ91T.setComporator("3 входа");
		mil_1986ВЕ91T.setExtWire("32 разрядов");
		
		mcuType mil_1986ВЕ94T	= new mcuType("1986ВЕ94T", "4229.132-3", "128 кбайт", "32 кбайт", "96");
		mil_1986ВЕ94T.setCore("ARM Cortex-M3");
		mil_1986ВЕ94T.setVcc("2.2...3.6В");
		mil_1986ВЕ94T.setFreq("80 МГц");
		mil_1986ВЕ94T.setTemp("минус 60°С...+125°С");
		mil_1986ВЕ94T.setUsb("Device и Host FS (до 12 Мбит/с), PHY");
		mil_1986ВЕ94T.setUart("2");
		mil_1986ВЕ94T.setCan("2");
		mil_1986ВЕ94T.setSpi("2");
		mil_1986ВЕ94T.setI2c("1");
		mil_1986ВЕ94T.setAdc("2 (12 разрядов, 1 Мвыб/с, 16 каналов)");
		mil_1986ВЕ94T.setDac("2 (12 разрядов)");
		mil_1986ВЕ94T.setComporator("3 входа");
		mil_1986ВЕ94T.setExtWire("32 разрядов");
		
		mcuType mil_1986ВЕ93У	= new mcuType("1986ВЕ93У", "Н16.48-1В", "128 кбайт", "32 кбайт", "30");
		mil_1986ВЕ93У.setCore("ARM Cortex-M3");
		mil_1986ВЕ93У.setVcc("2.2...3.6В");
		mil_1986ВЕ93У.setFreq("80 МГц");
		mil_1986ВЕ93У.setTemp("минус 60°С...+125°С");
		mil_1986ВЕ93У.setUsb("Device и Host FS (до 12 Мбит/с), PHY");
		mil_1986ВЕ93У.setUart("2");
		mil_1986ВЕ93У.setCan("2");
		mil_1986ВЕ93У.setSpi("1");
		mil_1986ВЕ93У.setI2c("нет");
		mil_1986ВЕ93У.setAdc("2 (12 разрядов, 1 Мвыб/с, 4 канала)");
		mil_1986ВЕ93У.setDac("1 (12 разрядов)");
		mil_1986ВЕ93У.setComporator("2 входа");
		mil_1986ВЕ93У.setExtWire("нет");
		
		mcuTable.getItems().clear();
		mcuTable.getItems().add(mil_198692Y);
		mcuTable.getItems().add(mil_K1986BE92QI);
		mcuTable.getItems().add(mil_1986ВЕ91T);
		mcuTable.getItems().add(mil_1986ВЕ94T);
		mcuTable.getItems().add(mil_1986ВЕ93У);
	}
	
	@FXML
	private void handleCancel() {
		Milandr_ex.mcuMain = null;
		dialogStage.close();
	}
	
	@FXML
	private void handleOK() {
		
		Milandr_ex.mcuMain = mcuTable.getSelectionModel().getSelectedItem();
		
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
	
	
	public void showMCUDetails(mcuType mcu){
		if (mcu != null){
			typeLabel.setText(mcu.getType());
			packLabel.setText(mcu.getPack());
			coreLabel.setText(mcu.getCore());
			flashLabel.setText(mcu.getFlash());
			ramLabel.setText(mcu.getRam());
			vccLabel.setText(mcu.getVcc());
			freqLabel.setText(mcu.getFreq());
			tempLabel.setText(mcu.getTemp());
			ioLabel.setText(mcu.getIO());
			usbLabel.setText(mcu.getUsb());
			uartLabel.setText(mcu.getUart());
			canLabel.setText(mcu.getCan());
			spiLabel.setText(mcu.getSpi());
			i2cLabel.setText(mcu.getI2c());
			adcLabel.setText(mcu.getAdc());
			dacLabel.setText(mcu.getDac());
			comporatorLabel.setText(mcu.getComporator());
			extwireLabel.setText(mcu.getExtWire());
		}else{
			typeLabel.setText("");
			packLabel.setText("");
			coreLabel.setText("");
			flashLabel.setText("");
			ramLabel.setText("");
			vccLabel.setText("");
			freqLabel.setText("");
			tempLabel.setText("");
			ioLabel.setText("");
			usbLabel.setText("");
			uartLabel.setText("");
			canLabel.setText("");
			spiLabel.setText("");
			i2cLabel.setText("");
			adcLabel.setText("");
			dacLabel.setText("");
			comporatorLabel.setText("");
			extwireLabel.setText("");
		}
	}
	
	public ObservableList<mcuType> getMCUData(){
		return mcuData;
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
