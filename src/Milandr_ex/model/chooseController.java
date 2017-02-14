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
		mcuType mil_198692Y = new mcuType("1986��92�", "�18.64-1�", "128 �����", "32 �����", "43");
		mil_198692Y.setCore("ARM Cortex-M3");
		mil_198692Y.setVcc("2.2...3.6�");
		mil_198692Y.setFreq("80 ���");
		mil_198692Y.setTemp("����� 60��...+125��");
		mil_198692Y.setUsb("Device � Host FS (�� 12 ����/�), PHY");
		mil_198692Y.setUart("2");
		mil_198692Y.setCan("2");
		mil_198692Y.setSpi("2");
		mil_198692Y.setI2c("1");
		mil_198692Y.setAdc("2 (12 ��������, 1 ����/�, 8 �������)");
		mil_198692Y.setDac("1 (12 ��������)");
		mil_198692Y.setComporator("2 �����");
		mil_198692Y.setExtWire("8 ��������");
		
		mcuType mil_K1986BE92QI	= new mcuType("�1986��92QI", "LQFP64", "128 �����", "32 �����", "43");
		mil_K1986BE92QI.setCore("ARM Cortex-M3");
		mil_K1986BE92QI.setVcc("2.2...3.6�");
		mil_K1986BE92QI.setFreq("80 ���");
		mil_K1986BE92QI.setTemp("����� 60��...+125��");
		mil_K1986BE92QI.setUsb("Device � Host FS (�� 12 ����/�), PHY");
		mil_K1986BE92QI.setUart("2");
		mil_K1986BE92QI.setCan("2");
		mil_K1986BE92QI.setSpi("2");
		mil_K1986BE92QI.setI2c("1");
		mil_K1986BE92QI.setAdc("2 (12 ��������, 1 ����/�, 8 �������)");
		mil_K1986BE92QI.setDac("1 (12 ��������)");
		mil_K1986BE92QI.setComporator("2 �����");
		mil_K1986BE92QI.setExtWire("8 ��������");
		
		mcuType mil_1986��91T	= new mcuType("1986��91T", "4229.132-3", "128 �����", "32 �����", "96");
		mil_1986��91T.setCore("ARM Cortex-M3");
		mil_1986��91T.setVcc("2.2...3.6�");
		mil_1986��91T.setFreq("80 ���");
		mil_1986��91T.setTemp("����� 60��...+125��");
		mil_1986��91T.setUsb("Device � Host FS (�� 12 ����/�), PHY");
		mil_1986��91T.setUart("2");
		mil_1986��91T.setCan("2");
		mil_1986��91T.setSpi("2");
		mil_1986��91T.setI2c("1");
		mil_1986��91T.setAdc("2 (12 ��������, 1 ����/�, 16 �������)");
		mil_1986��91T.setDac("2 (12 ��������)");
		mil_1986��91T.setComporator("3 �����");
		mil_1986��91T.setExtWire("32 ��������");
		
		mcuType mil_1986��94T	= new mcuType("1986��94T", "4229.132-3", "128 �����", "32 �����", "96");
		mil_1986��94T.setCore("ARM Cortex-M3");
		mil_1986��94T.setVcc("2.2...3.6�");
		mil_1986��94T.setFreq("80 ���");
		mil_1986��94T.setTemp("����� 60��...+125��");
		mil_1986��94T.setUsb("Device � Host FS (�� 12 ����/�), PHY");
		mil_1986��94T.setUart("2");
		mil_1986��94T.setCan("2");
		mil_1986��94T.setSpi("2");
		mil_1986��94T.setI2c("1");
		mil_1986��94T.setAdc("2 (12 ��������, 1 ����/�, 16 �������)");
		mil_1986��94T.setDac("2 (12 ��������)");
		mil_1986��94T.setComporator("3 �����");
		mil_1986��94T.setExtWire("32 ��������");
		
		mcuType mil_1986��93�	= new mcuType("1986��93�", "�16.48-1�", "128 �����", "32 �����", "30");
		mil_1986��93�.setCore("ARM Cortex-M3");
		mil_1986��93�.setVcc("2.2...3.6�");
		mil_1986��93�.setFreq("80 ���");
		mil_1986��93�.setTemp("����� 60��...+125��");
		mil_1986��93�.setUsb("Device � Host FS (�� 12 ����/�), PHY");
		mil_1986��93�.setUart("2");
		mil_1986��93�.setCan("2");
		mil_1986��93�.setSpi("1");
		mil_1986��93�.setI2c("���");
		mil_1986��93�.setAdc("2 (12 ��������, 1 ����/�, 4 ������)");
		mil_1986��93�.setDac("1 (12 ��������)");
		mil_1986��93�.setComporator("2 �����");
		mil_1986��93�.setExtWire("���");
		
		mcuTable.getItems().clear();
		mcuTable.getItems().add(mil_198692Y);
		mcuTable.getItems().add(mil_K1986BE92QI);
		mcuTable.getItems().add(mil_1986��91T);
		mcuTable.getItems().add(mil_1986��94T);
		mcuTable.getItems().add(mil_1986��93�);
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
//		alert.setTitle("��� �����");
//		alert.setHeaderText("��� ���������� - " + chooseMCU.getType());
		
//		String param = String.format("��� ������� - %s\r\n", chooseMCU.getPack());
//		param += String.format("����� ��� - %s\r\n", chooseMCU.getFlash());
//		param += String.format("����� ��� - %s\r\n", chooseMCU.getRam());
//		param += String.format("���-�� ������������� ������� - %s\r\n", chooseMCU.getIO());
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
