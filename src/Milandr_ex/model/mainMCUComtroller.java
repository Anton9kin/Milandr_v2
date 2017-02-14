package Milandr_ex.model;

import Milandr_ex.Milandr_ex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class mainMCUComtroller {

	private Background backgroundDefault = null;
	private Background backgroundIO = new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	private Background backgroundPeriph = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
	
	@FXML
	private CheckBox chbADC;
	@FXML
	private CheckBox chbCOMP;
	@FXML
	private CheckBox chbUSB;
	@FXML
	private CheckBox chbUART;
	@FXML
	private CheckBox chbCAN;
	@FXML
	private CheckBox chbSPI;
	@FXML
	private CheckBox chbI2C;
	@FXML
	private CheckBox chbDAC;
	
	@FXML
	private Label lab_PA0; 
	@FXML
	private Label lab_PA1; 
	@FXML
	private Label lab_PA2; 
	@FXML
	private Label lab_PA3; 
	@FXML
	private Label lab_PA4; 
	@FXML
	private Label lab_PA5; 
	@FXML
	private Label lab_PA6; 
	@FXML
	private Label lab_PA7; 
	@FXML
	private Label lab_PA8; 
	@FXML
	private Label lab_PA9; 
	@FXML
	private Label lab_PA10; 
	@FXML
	private Label lab_PA11; 
	@FXML
	private Label lab_PA12; 
	@FXML
	private Label lab_PA13; 
	@FXML
	private Label lab_PA14; 
	@FXML
	private Label lab_PA15; 
	
	@FXML
	private Label lab_PB0; 
	@FXML
	private Label lab_PB1; 
	@FXML
	private Label lab_PB2; 
	@FXML
	private Label lab_PB3; 
	@FXML
	private Label lab_PB4; 
	@FXML
	private Label lab_PB5; 
	@FXML
	private Label lab_PB6; 
	@FXML
	private Label lab_PB7; 
	@FXML
	private Label lab_PB8; 
	@FXML
	private Label lab_PB9; 
	@FXML
	private Label lab_PB10; 
	@FXML
	private Label lab_PB11; 
	@FXML
	private Label lab_PB12; 
	@FXML
	private Label lab_PB13; 
	@FXML
	private Label lab_PB14; 
	@FXML
	private Label lab_PB15; 
	
	@FXML
	private Label lab_PC0; 
	@FXML
	private Label lab_PC1; 
	@FXML
	private Label lab_PC2; 
	@FXML
	private Label lab_PC3; 
	@FXML
	private Label lab_PC4; 
	@FXML
	private Label lab_PC5; 
	@FXML
	private Label lab_PC6; 
	@FXML
	private Label lab_PC7; 
	@FXML
	private Label lab_PC8; 
	@FXML
	private Label lab_PC9; 
	@FXML
	private Label lab_PC10; 
	@FXML
	private Label lab_PC11; 
	@FXML
	private Label lab_PC12; 
	@FXML
	private Label lab_PC13; 
	@FXML
	private Label lab_PC14; 
	@FXML
	private Label lab_PC15; 
	
	@FXML
	private Label lab_PD0; 
	@FXML
	private Label lab_PD1; 
	@FXML
	private Label lab_PD2; 
	@FXML
	private Label lab_PD3; 
	@FXML
	private Label lab_PD4; 
	@FXML
	private Label lab_PD5; 
	@FXML
	private Label lab_PD6; 
	@FXML
	private Label lab_PD7; 
	@FXML
	private Label lab_PD8; 
	@FXML
	private Label lab_PD9; 
	@FXML
	private Label lab_PD10; 
	@FXML
	private Label lab_PD11; 
	@FXML
	private Label lab_PD12; 
	@FXML
	private Label lab_PD13; 
	@FXML
	private Label lab_PD14; 
	@FXML
	private Label lab_PD15; 
	
	@FXML
	private Label lab_PE0; 
	@FXML
	private Label lab_PE1; 
	@FXML
	private Label lab_PE2; 
	@FXML
	private Label lab_PE3; 
	@FXML
	private Label lab_PE4; 
	@FXML
	private Label lab_PE5; 
	@FXML
	private Label lab_PE6; 
	@FXML
	private Label lab_PE7; 
	@FXML
	private Label lab_PE8; 
	@FXML
	private Label lab_PE9; 
	@FXML
	private Label lab_PE10; 
	@FXML
	private Label lab_PE11; 
	@FXML
	private Label lab_PE12; 
	@FXML
	private Label lab_PE13; 
	@FXML
	private Label lab_PE14; 
	@FXML
	private Label lab_PE15; 
	
	@FXML
	private Label lab_PF0; 
	@FXML
	private Label lab_PF1; 
	@FXML
	private Label lab_PF2; 
	@FXML
	private Label lab_PF3; 
	@FXML
	private Label lab_PF4; 
	@FXML
	private Label lab_PF5; 
	@FXML
	private Label lab_PF6; 
	@FXML
	private Label lab_PF7; 
	@FXML
	private Label lab_PF8; 
	@FXML
	private Label lab_PF9; 
	@FXML
	private Label lab_PF10; 
	@FXML
	private Label lab_PF11; 
	@FXML
	private Label lab_PF12; 
	@FXML
	private Label lab_PF13; 
	@FXML
	private Label lab_PF14; 
	@FXML
	private Label lab_PF15; 
	
	@FXML
	private ComboBox<String> combo_PA0;
	ObservableList<String> PA0List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA0","EXT_INT1","-");
	@FXML
	private ComboBox<String> combo_PA1;
	ObservableList<String> PA1List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA1","TMR1_CH1","TMR2_CH1");
	@FXML
	private ComboBox<String> combo_PA2;
	ObservableList<String> PA2List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA2","TMR1_CH1N","TMR2_CH1N");
	@FXML
	private ComboBox<String> combo_PA3;
	ObservableList<String> PA3List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA3","TMR1_CH2","TMR2_CH2");
	@FXML
	private ComboBox<String> combo_PA4;
	ObservableList<String> PA4List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA4","TMR1_CH2N","TMR2_CH2N");
	@FXML
	private ComboBox<String> combo_PA5;
	ObservableList<String> PA5List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA5","TMR1_CH3","TMR2_CH3");
	@FXML
	private ComboBox<String> combo_PA6;
	ObservableList<String> PA6List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA6","CAN1_TX","UART1_RXD");
	@FXML
	private ComboBox<String> combo_PA7;
	ObservableList<String> PA7List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA7","CAN1_RX","UART1_TXD");
	@FXML
	private ComboBox<String> combo_PA8;
	ObservableList<String> PA8List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA8","TMR1_CH3N","TMR2_CH3N");
	@FXML
	private ComboBox<String> combo_PA9;
	ObservableList<String> PA9List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA9","TMR1_CH4","TMR2_CH4");
	@FXML
	private ComboBox<String> combo_PA10;
	ObservableList<String> PA10List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA10","nUART1DTR","TMR2_CH4N");
	@FXML
	private ComboBox<String> combo_PA11;
	ObservableList<String> PA11List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA11","nUART1RTS","TMR2_BLK");
	@FXML
	private ComboBox<String> combo_PA12;
	ObservableList<String> PA12List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA12","nUART1RI","TMR2_ETR");
	@FXML
	private ComboBox<String> combo_PA13;
	ObservableList<String> PA13List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA13","nUART1DCD","TMR1_CH4N");
	@FXML
	private ComboBox<String> combo_PA14;
	ObservableList<String> PA14List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA14","nUART1DSR","TMR1_BLK");
	@FXML
	private ComboBox<String> combo_PA15;
	ObservableList<String> PA15List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA15","nUART1CTS","TMR1_ETR");
	
	@FXML
	private ComboBox<String> combo_PB0;
	ObservableList<String> PB0List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA16","TMR3_CH1","UART1_TXD");
	@FXML
	private ComboBox<String> combo_PB1;
	ObservableList<String> PB1List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA17","TMR3_CH1N","UART2_RXD");
	@FXML
	private ComboBox<String> combo_PB2;
	ObservableList<String> PB2List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA18","TMR3_CH2","CAN1_TX");
	@FXML
	private ComboBox<String> combo_PB3;
	ObservableList<String> PB3List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA19","TMR3_CH2N","CAN1_RX");
	@FXML
	private ComboBox<String> combo_PB4;
	ObservableList<String> PB4List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA20","TMR3_BLK","CAN3_ETR");
	@FXML
	private ComboBox<String> combo_PB5;
	ObservableList<String> PB5List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA21","UART1_TXD","TMR3_CH3");
	@FXML
	private ComboBox<String> combo_PB6;
	ObservableList<String> PB6List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA22","UART1_RXD","TMR3_CH3N");
	@FXML
	private ComboBox<String> combo_PB7;
	ObservableList<String> PB7List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA23","nSIROUT1","TMR3_CH4");
	@FXML
	private ComboBox<String> combo_PB8;
	ObservableList<String> PB8List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA24","COMP_OUT","TMR3_CH4N");
	@FXML
	private ComboBox<String> combo_PB9;
	ObservableList<String> PB9List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA25","nSIRIN1","EXT_INT4");
	@FXML
	private ComboBox<String> combo_PB10;
	ObservableList<String> PB10List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA26","EXT_INT2","nSIROUT1");
	@FXML
	private ComboBox<String> combo_PB11;
	ObservableList<String> PB11List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA27","EXT_INT1","COMP_OUT");
	@FXML
	private ComboBox<String> combo_PB12;
	ObservableList<String> PB12List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA28","SSP1_FSS","SSP2_FSS");
	@FXML
	private ComboBox<String> combo_PB13;
	ObservableList<String> PB13List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA29","SSP1_CLK","SSP2_CLK");
	@FXML
	private ComboBox<String> combo_PB14;
	ObservableList<String> PB14List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA30","SSP1_RXD","SSP2_RXD");
	@FXML
	private ComboBox<String> combo_PB15;
	ObservableList<String> PB15List = FXCollections.observableArrayList("RESET","-","IO out","IO in","DATA31","SSP1_TXD","SSP2_TXD");
	
	@FXML
	private ComboBox<String> combo_PC0;
	ObservableList<String> PC0List = FXCollections.observableArrayList("RESET","-","IO out","IO in","READY","SCL1","SSP2_FSS");
	@FXML
	private ComboBox<String> combo_PC1;
	ObservableList<String> PC1List = FXCollections.observableArrayList("RESET","-","IO out","IO in","OE","SDA1","SSP2_CLK");
	@FXML
	private ComboBox<String> combo_PC2;
	ObservableList<String> PC2List = FXCollections.observableArrayList("RESET","-","IO out","IO in","WE","TMR3_CH1","SSP2_RXD");
	@FXML
	private ComboBox<String> combo_PC3;
	ObservableList<String> PC3List = FXCollections.observableArrayList("RESET","-","IO out","IO in","BE0","TMR3_CH1N","SSP2_TXD");
	@FXML
	private ComboBox<String> combo_PC4;
	ObservableList<String> PC4List = FXCollections.observableArrayList("RESET","-","IO out","IO in","BE1","TMR3_CH2","TMR1_CH1");
	@FXML
	private ComboBox<String> combo_PC5;
	ObservableList<String> PC5List = FXCollections.observableArrayList("RESET","-","IO out","IO in","BE2","TMR3_CH2N","TMR1_CH1N");
	@FXML
	private ComboBox<String> combo_PC6;
	ObservableList<String> PC6List = FXCollections.observableArrayList("RESET","-","IO out","IO in","BE3","TMR3_CH3","TMR1_CH2");
	@FXML
	private ComboBox<String> combo_PC7;
	ObservableList<String> PC7List = FXCollections.observableArrayList("RESET","-","IO out","IO in","CLOCK","TMR3_CH3N","TMR1_CH2N");
	@FXML
	private ComboBox<String> combo_PC8;
	ObservableList<String> PC8List = FXCollections.observableArrayList("RESET","-","IO out","IO in","CAN1_TX","TMR3_CH4","TMR1_CH3");
	@FXML
	private ComboBox<String> combo_PC9;
	ObservableList<String> PC9List = FXCollections.observableArrayList("RESET","-","IO out","IO in","CAN1_RX","TMR3_CH4N","TMR1_CH3N");
	@FXML
	private ComboBox<String> combo_PC10;
	ObservableList<String> PC10List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","TMR3_ETR","TMR1_CH4");
	@FXML
	private ComboBox<String> combo_PC11;
	ObservableList<String> PC11List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","TMR3_BLK","TMR1_CH4N");
	@FXML
	private ComboBox<String> combo_PC12;
	ObservableList<String> PC12List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","EXT_INT2","TMR1_ETR");
	@FXML
	private ComboBox<String> combo_PC13;
	ObservableList<String> PC13List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","EXT_INT4","TMR1_BLK");
	@FXML
	private ComboBox<String> combo_PC14;
	ObservableList<String> PC14List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","SSP2_FSS","CAN2_RX");
	@FXML
	private ComboBox<String> combo_PC15;
	ObservableList<String> PC15List = FXCollections.observableArrayList("RESET","-","IO out","IO in","-","SSP2_RXD","CAN2_TX");
	
	@FXML
	private ComboBox<String> combo_PD0;
	ObservableList<String> PD0List = FXCollections.observableArrayList("RESET","ADC0_REF+","IO out","IO in","TMR1_CH1N","UART2_RXD","TMR3_CH1");
	@FXML
	private ComboBox<String> combo_PD1;
	ObservableList<String> PD1List = FXCollections.observableArrayList("RESET","ADC1_REF-","IO out","IO in","TMR1_CH1","UART2_TXD","TMR3_CH1N");
	@FXML
	private ComboBox<String> combo_PD2;
	ObservableList<String> PD2List = FXCollections.observableArrayList("RESET","ADC2","IO out","IO in","BUSY1","SSP2_RXD","TMR3_CH2");
	@FXML
	private ComboBox<String> combo_PD3;
	ObservableList<String> PD3List = FXCollections.observableArrayList("RESET","ADC3","IO out","IO in","-","SSP2_FSS","TMR3_CH2N");
	@FXML
	private ComboBox<String> combo_PD4;
	ObservableList<String> PD4List = FXCollections.observableArrayList("RESET","ADC4","IO out","IO in","TMR1_ETR","nSIROUT2","TMR3_BLK");
	@FXML
	private ComboBox<String> combo_PD5;
	ObservableList<String> PD5List = FXCollections.observableArrayList("RESET","ADC5","IO out","IO in","CLE","SSP2_CLK","TMR2_ETR");
	@FXML
	private ComboBox<String> combo_PD6;
	ObservableList<String> PD6List = FXCollections.observableArrayList("RESET","ADC6","IO out","IO in","ALE","SSP2_TXD","TMR2_BLK");
	@FXML
	private ComboBox<String> combo_PD7;
	ObservableList<String> PD7List = FXCollections.observableArrayList("RESET","ADC7","IO out","IO in","TMR1_BLK","nSIRIN2","UART1_RXD");
	@FXML
	private ComboBox<String> combo_PD8;
	ObservableList<String> PD8List = FXCollections.observableArrayList("RESET","ADC8","IO out","IO in","TMR1_CH4N","TMR2_CH1","UART1_TXD");
	@FXML
	private ComboBox<String> combo_PD9;
	ObservableList<String> PD9List = FXCollections.observableArrayList("RESET","ADC9","IO out","IO in","CAN2_TX","TMR2_CH1N","SSP1_FSS");
	@FXML
	private ComboBox<String> combo_PD10;
	ObservableList<String> PD10List = FXCollections.observableArrayList("RESET","ADC10","IO out","IO in","TMR1_CH2","TMR2_CH2","SSP1_CLK");
	@FXML
	private ComboBox<String> combo_PD11;
	ObservableList<String> PD11List = FXCollections.observableArrayList("RESET","ADC11","IO out","IO in","TMR1_CH2N","TMR2_CH2N","SSP1_RXD");
	@FXML
	private ComboBox<String> combo_PD12;
	ObservableList<String> PD12List = FXCollections.observableArrayList("RESET","ADC12","IO out","IO in","TMR1_CH3","TMR2_CH3","SSP1_TXD");
	@FXML
	private ComboBox<String> combo_PD13;
	ObservableList<String> PD13List = FXCollections.observableArrayList("RESET","ADC13","IO out","IO in","TMR1_CH3N","TMR2_CH3N","CAN1_TX");
	@FXML
	private ComboBox<String> combo_PD14;
	ObservableList<String> PD14List = FXCollections.observableArrayList("RESET","ADC14","IO out","IO in","TMR1_CH4","TMR2_CH4","CAN1_RX");
	@FXML
	private ComboBox<String> combo_PD15;
	ObservableList<String> PD15List = FXCollections.observableArrayList("RESET","ADC15","IO out","IO in","CAN2_RX","BUSY2","EXT_INT3");
	
	@FXML
	private ComboBox<String> combo_PE0;
	ObservableList<String> PE0List = FXCollections.observableArrayList("RESET","DAC2_OUT","IO out","IO in","ADDR16","TMR2_CH1","CAN1_RX");
	@FXML
	private ComboBox<String> combo_PE1;
	ObservableList<String> PE1List = FXCollections.observableArrayList("RESET","DAC2_REF","IO out","IO in","ADDR17","TMR2_CH1N","CAN1_TX");
	@FXML
	private ComboBox<String> combo_PE2;
	ObservableList<String> PE2List = FXCollections.observableArrayList("RESET","COMP_IN1","IO out","IO in","ADDR18","TMR2_CH3","TMR3_CH1");
	@FXML
	private ComboBox<String> combo_PE3;
	ObservableList<String> PE3List = FXCollections.observableArrayList("RESET","COMP_IN2","IO out","IO in","ADDR19","TMR2_CH3N","TMR3_CH1N");
	@FXML
	private ComboBox<String> combo_PE4;
	ObservableList<String> PE4List = FXCollections.observableArrayList("RESET","COMP_REF+","IO out","IO in","ADDR20","TMR2_CH4N","TMR3_CH2");
	@FXML
	private ComboBox<String> combo_PE5;
	ObservableList<String> PE5List = FXCollections.observableArrayList("RESET","COMP_REF-","IO out","IO in","ADDR21","TMR2_BLK","TMR3_CH2N");
	@FXML
	private ComboBox<String> combo_PE6;
	ObservableList<String> PE6List = FXCollections.observableArrayList("RESET","OSC_IN32","IO out","IO in","ADDR22","CAN2_RX","TMR3_CH3");
	@FXML
	private ComboBox<String> combo_PE7;
	ObservableList<String> PE7List = FXCollections.observableArrayList("RESET","OSC_OUT32","IO out","IO in","ADDR23","CAN2_TX","TMR3_CH3N");
	@FXML
	private ComboBox<String> combo_PE8;
	ObservableList<String> PE8List = FXCollections.observableArrayList("RESET","COMP_IN3","IO out","IO in","ADDR24","TMR2_CH4","TMR3_CH4");
	@FXML
	private ComboBox<String> combo_PE9;
	ObservableList<String> PE9List = FXCollections.observableArrayList("RESET","DAC1_OUT","IO out","IO in","ADDR25","TMR2_CH2","TMR3_CH4N");
	@FXML
	private ComboBox<String> combo_PE10;
	ObservableList<String> PE10List = FXCollections.observableArrayList("RESET","DAC1_REF","IO out","IO in","ADDR26","TMR2_CH2N","TMR3_ETR");
	@FXML
	private ComboBox<String> combo_PE11;
	ObservableList<String> PE11List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR27","nSIRIN1","TMR3_BLK");
	@FXML
	private ComboBox<String> combo_PE12;
	ObservableList<String> PE12List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR28","SSP1_RXD","UART1_RXD");
	@FXML
	private ComboBox<String> combo_PE13;
	ObservableList<String> PE13List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR29","SSP1_FSS","UART1_TXD");
	@FXML
	private ComboBox<String> combo_PE14;
	ObservableList<String> PE14List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR30","TMR2_ETR","SCL1");
	@FXML
	private ComboBox<String> combo_PE15;
	ObservableList<String> PE15List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR31","EXT_INT3","SDA1");
	
	@FXML
	private ComboBox<String> combo_PF0;
	ObservableList<String> PF0List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR0","SSP1_TXD","UART2_RXD");
	@FXML
	private ComboBox<String> combo_PF1;
	ObservableList<String> PF1List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR1","SSP1_CLK","UART2_TXD");
	@FXML
	private ComboBox<String> combo_PF2;
	ObservableList<String> PF2List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR2","SSP1_FSS","CAN2_RX");
	@FXML
	private ComboBox<String> combo_PF3;
	ObservableList<String> PF3List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR3","SSP1_RXD","CAN2_TX");
	@FXML
	private ComboBox<String> combo_PF4;
	ObservableList<String> PF4List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR4","-","-");
	@FXML
	private ComboBox<String> combo_PF5;
	ObservableList<String> PF5List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR5","-","-");
	@FXML
	private ComboBox<String> combo_PF6;
	ObservableList<String> PF6List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR6","TMR1_CH1","-");
	@FXML
	private ComboBox<String> combo_PF7;
	ObservableList<String> PF7List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR7","TMR1_CH1N","TMR3_CH1");
	@FXML
	private ComboBox<String> combo_PF8;
	ObservableList<String> PF8List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR8","TMR1_CH2","TMR3_CH1N");
	@FXML
	private ComboBox<String> combo_PF9;
	ObservableList<String> PF9List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR9","TMR1_CH2N","TMR3_CH2");
	@FXML
	private ComboBox<String> combo_PF10;
	ObservableList<String> PF10List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR10","TMR1_CH3","TMR3_CH2N");
	@FXML
	private ComboBox<String> combo_PF11;
	ObservableList<String> PF11List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR11","TMR1_CH3N","TMR3_ETR");
	@FXML
	private ComboBox<String> combo_PF12;
	ObservableList<String> PF12List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR12","TMR1_CH4","SSP2_FSS");
	@FXML
	private ComboBox<String> combo_PF13;
	ObservableList<String> PF13List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR13","TMR1_CH4N","SSP2_CLK");
	@FXML
	private ComboBox<String> combo_PF14;
	ObservableList<String> PF14List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR14","TMR1_ETR","SSP2_RXD");
	@FXML
	private ComboBox<String> combo_PF15;
	ObservableList<String> PF15List = FXCollections.observableArrayList("RESET","-","IO out","IO in","ADDR15","TMR1_BLK","SSP2_TXD");
	
	@FXML
	private TabPane mainTabPane;
	
	public mainMCUComtroller() {
	
	}
	
	public void mainMCUStart() {
		// TODO Auto-generated constructor stub

		Milandr_ex.primaryStage.setTitle("Генератор кода - " + Milandr_ex.mcuMain.getType());
		
		lab_PA0.setVisible(false); lab_PA1.setVisible(false); lab_PA2.setVisible(false); lab_PA3.setVisible(false); 
		lab_PA4.setVisible(false);lab_PA5.setVisible(false); lab_PA6.setVisible(false); lab_PA7.setVisible(false);
		lab_PA8.setVisible(false);lab_PA9.setVisible(false); lab_PA10.setVisible(false); lab_PA11.setVisible(false);
		lab_PA12.setVisible(false); lab_PA13.setVisible(false); lab_PA14.setVisible(false); lab_PA15.setVisible(false);
		
		lab_PB0.setVisible(false); lab_PB1.setVisible(false); lab_PB2.setVisible(false); lab_PB3.setVisible(false); 
		lab_PB4.setVisible(false);lab_PB5.setVisible(false); lab_PB6.setVisible(false); lab_PB7.setVisible(false);
		lab_PB8.setVisible(false); lab_PB9.setVisible(false); lab_PB10.setVisible(false); lab_PB11.setVisible(false);
		lab_PB12.setVisible(false); lab_PB13.setVisible(false); lab_PB14.setVisible(false); lab_PB15.setVisible(false);
		
		lab_PC0.setVisible(false); lab_PC1.setVisible(false); lab_PC2.setVisible(false); lab_PC3.setVisible(false); 
		lab_PC4.setVisible(false);lab_PC5.setVisible(false); lab_PC6.setVisible(false); lab_PC7.setVisible(false);
		lab_PC8.setVisible(false); lab_PC9.setVisible(false); lab_PC10.setVisible(false); lab_PC11.setVisible(false);
		lab_PC12.setVisible(false); lab_PC13.setVisible(false); lab_PC14.setVisible(false); lab_PC15.setVisible(false);
		
		lab_PD0.setVisible(false); lab_PD1.setVisible(false); lab_PD2.setVisible(false); lab_PD3.setVisible(false); 
		lab_PD4.setVisible(false);lab_PD5.setVisible(false); lab_PD6.setVisible(false); lab_PD7.setVisible(false);
		lab_PD8.setVisible(false); lab_PD9.setVisible(false); lab_PD10.setVisible(false); lab_PD11.setVisible(false);
		lab_PD12.setVisible(false); lab_PD13.setVisible(false); lab_PD14.setVisible(false); lab_PD15.setVisible(false);
		
		lab_PE0.setVisible(false); lab_PE1.setVisible(false); lab_PE2.setVisible(false); lab_PE3.setVisible(false); 
		lab_PE4.setVisible(false);lab_PE5.setVisible(false); lab_PE6.setVisible(false); lab_PE7.setVisible(false);
		lab_PE8.setVisible(false); lab_PE9.setVisible(false); lab_PE10.setVisible(false); lab_PE11.setVisible(false);
		lab_PE12.setVisible(false); lab_PE13.setVisible(false); lab_PE14.setVisible(false); lab_PE15.setVisible(false);
		
		lab_PF0.setVisible(false); lab_PF1.setVisible(false); lab_PF2.setVisible(false); lab_PF3.setVisible(false); 
		lab_PF4.setVisible(false);lab_PF5.setVisible(false); lab_PF6.setVisible(false); lab_PF7.setVisible(false);
		lab_PF8.setVisible(false); lab_PF9.setVisible(false); lab_PF10.setVisible(false); lab_PF11.setVisible(false);
		lab_PF12.setVisible(false); lab_PF13.setVisible(false); lab_PF14.setVisible(false); lab_PF15.setVisible(false);
		
		switch(Milandr_ex.mcuMain.getPack()){
		case "4229.132-3":
			break;
		case "Н18.64-1В":
			combo_PA8.setVisible(false); 
			combo_PA9.setVisible(false); combo_PA10.setVisible(false); combo_PA11.setVisible(false);
			combo_PA12.setVisible(false); combo_PA13.setVisible(false); combo_PA14.setVisible(false); combo_PA15.setVisible(false);
			lab_PA8.setVisible(false); 
			lab_PA9.setVisible(false); lab_PA10.setVisible(false); lab_PA11.setVisible(false);
			lab_PA12.setVisible(false); lab_PA13.setVisible(false); lab_PA14.setVisible(false); lab_PA15.setVisible(false);
			
			
			combo_PB11.setVisible(false); combo_PB12.setVisible(false); combo_PB13.setVisible(false); combo_PB14.setVisible(false);
			combo_PB15.setVisible(false);
			lab_PB11.setVisible(false); lab_PB12.setVisible(false); lab_PB13.setVisible(false); lab_PB14.setVisible(false);
			lab_PB15.setVisible(false);
			
			combo_PC3.setVisible(false); combo_PC4.setVisible(false); combo_PC5.setVisible(false); combo_PC6.setVisible(false);
			combo_PC7.setVisible(false); combo_PC8.setVisible(false); combo_PC9.setVisible(false); combo_PC10.setVisible(false);
			combo_PC11.setVisible(false); combo_PC12.setVisible(false); combo_PC13.setVisible(false); combo_PC14.setVisible(false);
			combo_PC15.setVisible(false);
			lab_PC3.setVisible(false); lab_PC4.setVisible(false); lab_PC5.setVisible(false); lab_PC6.setVisible(false);
			lab_PC7.setVisible(false); lab_PC8.setVisible(false); lab_PC9.setVisible(false); lab_PC10.setVisible(false);
			lab_PC11.setVisible(false); lab_PC12.setVisible(false); lab_PC13.setVisible(false); lab_PC14.setVisible(false);
			lab_PC15.setVisible(false);
			
			combo_PD8.setVisible(false);combo_PD9.setVisible(false);combo_PD10.setVisible(false);combo_PD11.setVisible(false);
			combo_PD12.setVisible(false);combo_PD13.setVisible(false);combo_PD14.setVisible(false);combo_PD15.setVisible(false);
			lab_PD8.setVisible(false);lab_PD9.setVisible(false);lab_PD10.setVisible(false);lab_PD11.setVisible(false);
			lab_PD12.setVisible(false);lab_PD13.setVisible(false);lab_PD14.setVisible(false);lab_PD15.setVisible(false);
			
			combo_PE4.setVisible(false);combo_PE5.setVisible(false);combo_PE8.setVisible(false);combo_PE9.setVisible(false);
			combo_PE10.setVisible(false);combo_PE11.setVisible(false);combo_PE12.setVisible(false);combo_PE13.setVisible(false);
			combo_PE14.setVisible(false);combo_PE15.setVisible(false);
			lab_PE4.setVisible(false);lab_PE5.setVisible(false);lab_PE8.setVisible(false);lab_PE9.setVisible(false);
			lab_PE10.setVisible(false);lab_PE11.setVisible(false);lab_PE12.setVisible(false);lab_PE13.setVisible(false);
			lab_PE14.setVisible(false);lab_PE15.setVisible(false);
			
			combo_PF7.setVisible(false);combo_PF8.setVisible(false);combo_PF9.setVisible(false);combo_PF10.setVisible(false);
			combo_PF11.setVisible(false);combo_PF12.setVisible(false);combo_PF13.setVisible(false);combo_PF14.setVisible(false);
			combo_PF15.setVisible(false);
			lab_PF7.setVisible(false);lab_PF8.setVisible(false);lab_PF9.setVisible(false);lab_PF10.setVisible(false);
			lab_PF11.setVisible(false);lab_PF12.setVisible(false);lab_PF13.setVisible(false);lab_PF14.setVisible(false);
			lab_PF15.setVisible(false);
			
			break;
		case "Н16.48-1В":
			combo_PA8.setVisible(false);combo_PA9.setVisible(false);combo_PA10.setVisible(false);combo_PA11.setVisible(false);
			combo_PA12.setVisible(false);combo_PA13.setVisible(false);combo_PA14.setVisible(false);combo_PA15.setVisible(false);
			lab_PA8.setVisible(false);lab_PA9.setVisible(false);lab_PA10.setVisible(false);lab_PA11.setVisible(false);
			lab_PA12.setVisible(false);lab_PA13.setVisible(false);lab_PA14.setVisible(false);lab_PA15.setVisible(false);
			
			combo_PB7.setVisible(false);combo_PB8.setVisible(false);combo_PB9.setVisible(false);combo_PB10.setVisible(false);
			combo_PB11.setVisible(false);combo_PB12.setVisible(false);combo_PB13.setVisible(false);combo_PB14.setVisible(false);
			combo_PB15.setVisible(false);
			lab_PB7.setVisible(false);lab_PB8.setVisible(false);lab_PB9.setVisible(false);lab_PB10.setVisible(false);
			lab_PB11.setVisible(false);lab_PB12.setVisible(false);lab_PB13.setVisible(false);lab_PB14.setVisible(false);
			lab_PB15.setVisible(false);
			
			combo_PC1.setVisible(false);combo_PC2.setVisible(false);combo_PC3.setVisible(false);combo_PC4.setVisible(false);
			combo_PC5.setVisible(false);combo_PC6.setVisible(false);combo_PC7.setVisible(false);combo_PC8.setVisible(false);
			combo_PC9.setVisible(false);combo_PC10.setVisible(false);combo_PC11.setVisible(false);combo_PC12.setVisible(false);
			combo_PC13.setVisible(false);combo_PC14.setVisible(false);combo_PC15.setVisible(false);
			lab_PC1.setVisible(false);lab_PC2.setVisible(false);lab_PC3.setVisible(false);lab_PC4.setVisible(false);
			lab_PC5.setVisible(false);lab_PC6.setVisible(false);lab_PC7.setVisible(false);lab_PC8.setVisible(false);
			lab_PC9.setVisible(false);lab_PC10.setVisible(false);lab_PC11.setVisible(false);lab_PC12.setVisible(false);
			lab_PC13.setVisible(false);lab_PC14.setVisible(false);lab_PC15.setVisible(false);
			
			combo_PD4.setVisible(false);combo_PD5.setVisible(false);combo_PD6.setVisible(false);combo_PD7.setVisible(false);
			combo_PD8.setVisible(false);combo_PD9.setVisible(false);combo_PD10.setVisible(false);combo_PD11.setVisible(false);
			combo_PD12.setVisible(false);combo_PD13.setVisible(false);combo_PD14.setVisible(false);combo_PD15.setVisible(false);
			lab_PD4.setVisible(false);lab_PD5.setVisible(false);lab_PD6.setVisible(false);lab_PD7.setVisible(false);
			lab_PD8.setVisible(false);lab_PD9.setVisible(false);lab_PD10.setVisible(false);lab_PD11.setVisible(false);
			lab_PD12.setVisible(false);lab_PD13.setVisible(false);lab_PD14.setVisible(false);lab_PD15.setVisible(false);
			
			combo_PE1.setVisible(false);combo_PE4.setVisible(false);combo_PE5.setVisible(false);combo_PE7.setVisible(false);
			combo_PE8.setVisible(false);combo_PE9.setVisible(false);combo_PE10.setVisible(false);combo_PE11.setVisible(false);
			combo_PE12.setVisible(false);combo_PE13.setVisible(false);combo_PE14.setVisible(false);combo_PE15.setVisible(false);
			lab_PE1.setVisible(false);lab_PE4.setVisible(false);lab_PE5.setVisible(false);lab_PE7.setVisible(false);
			lab_PE8.setVisible(false);lab_PE9.setVisible(false);lab_PE10.setVisible(false);lab_PE11.setVisible(false);
			lab_PE12.setVisible(false);lab_PE13.setVisible(false);lab_PE14.setVisible(false);lab_PE15.setVisible(false);
			
			combo_PF6.setVisible(false);combo_PF7.setVisible(false);combo_PF8.setVisible(false);combo_PF9.setVisible(false);
			combo_PF10.setVisible(false);combo_PF11.setVisible(false);combo_PF12.setVisible(false);combo_PF13.setVisible(false);
			combo_PF14.setVisible(false);combo_PF15.setVisible(false);
			lab_PF6.setVisible(false);lab_PF7.setVisible(false);lab_PF8.setVisible(false);lab_PF9.setVisible(false);
			lab_PF10.setVisible(false);lab_PF11.setVisible(false);lab_PF12.setVisible(false);lab_PF13.setVisible(false);
			lab_PF14.setVisible(false);lab_PF15.setVisible(false);
			
			break;
		case "LQFP64":
			combo_PA8.setVisible(false);combo_PA9.setVisible(false);combo_PA10.setVisible(false);combo_PA11.setVisible(false);
			combo_PA12.setVisible(false);combo_PA13.setVisible(false);combo_PA14.setVisible(false);combo_PA15.setVisible(false);
			lab_PA8.setVisible(false);lab_PA9.setVisible(false);lab_PA10.setVisible(false);lab_PA11.setVisible(false);
			lab_PA12.setVisible(false);lab_PA13.setVisible(false);lab_PA14.setVisible(false);lab_PA15.setVisible(false);
			
			combo_PB11.setVisible(false);combo_PB12.setVisible(false);combo_PB13.setVisible(false);combo_PB14.setVisible(false);
			combo_PB15.setVisible(false);
			lab_PB11.setVisible(false);lab_PB12.setVisible(false);lab_PB13.setVisible(false);lab_PB14.setVisible(false);
			lab_PB15.setVisible(false);
			
			combo_PC3.setVisible(false);combo_PC4.setVisible(false);combo_PC5.setVisible(false);combo_PC6.setVisible(false);
			combo_PC7.setVisible(false);combo_PC8.setVisible(false);combo_PC9.setVisible(false);combo_PC10.setVisible(false);
			combo_PC11.setVisible(false);combo_PC12.setVisible(false);combo_PC13.setVisible(false);combo_PC14.setVisible(false);
			combo_PC15.setVisible(false);
			lab_PC3.setVisible(false);lab_PC4.setVisible(false);lab_PC5.setVisible(false);lab_PC6.setVisible(false);
			lab_PC7.setVisible(false);lab_PC8.setVisible(false);lab_PC9.setVisible(false);lab_PC10.setVisible(false);
			lab_PC11.setVisible(false);lab_PC12.setVisible(false);lab_PC13.setVisible(false);lab_PC14.setVisible(false);
			lab_PC15.setVisible(false);
			
			combo_PD8.setVisible(false);combo_PD9.setVisible(false);combo_PD10.setVisible(false);combo_PD11.setVisible(false);
			combo_PD12.setVisible(false);combo_PD13.setVisible(false);combo_PD14.setVisible(false);combo_PD15.setVisible(false);
			lab_PD8.setVisible(false);lab_PD9.setVisible(false);lab_PD10.setVisible(false);lab_PD11.setVisible(false);
			lab_PD12.setVisible(false);lab_PD13.setVisible(false);lab_PD14.setVisible(false);lab_PD15.setVisible(false);
			
			combo_PE4.setVisible(false);combo_PE5.setVisible(false);combo_PE8.setVisible(false);combo_PE9.setVisible(false);
			combo_PE10.setVisible(false);combo_PE11.setVisible(false);combo_PE12.setVisible(false);combo_PE13.setVisible(false);
			combo_PE14.setVisible(false);combo_PE15.setVisible(false);
			lab_PE4.setVisible(false);lab_PE5.setVisible(false);lab_PE8.setVisible(false);lab_PE9.setVisible(false);
			lab_PE10.setVisible(false);lab_PE11.setVisible(false);lab_PE12.setVisible(false);lab_PE13.setVisible(false);
			lab_PE14.setVisible(false);lab_PE15.setVisible(false);
			
			combo_PF7.setVisible(false);combo_PF8.setVisible(false);combo_PF9.setVisible(false);combo_PF10.setVisible(false);
			combo_PF11.setVisible(false);combo_PF12.setVisible(false);combo_PF13.setVisible(false);combo_PF14.setVisible(false);
			combo_PF15.setVisible(false);
			lab_PF7.setVisible(false);lab_PF8.setVisible(false);lab_PF9.setVisible(false);lab_PF10.setVisible(false);
			lab_PF11.setVisible(false);lab_PF12.setVisible(false);lab_PF13.setVisible(false);lab_PF14.setVisible(false);
			lab_PF15.setVisible(false);
			
			break;
		}
	}
	
	@FXML
	private void initialize(){
		
		setItems();
		
		mainMCUStart();
	}
	
	@FXML
	private void changeData(){
		changeCombo();
	}
	
	private void setItems(){
		
		combo_PA0.setItems(PA0List);
		combo_PA0.setBackground(backgroundDefault);
		combo_PA1.setItems(PA1List);
		combo_PA1.setBackground(backgroundDefault);
		combo_PA2.setItems(PA2List);
		combo_PA2.setBackground(backgroundDefault);
		combo_PA3.setItems(PA3List);
		combo_PA3.setBackground(backgroundDefault);
		combo_PA4.setItems(PA4List);
		combo_PA4.setBackground(backgroundDefault);
		combo_PA5.setItems(PA5List);
		combo_PA5.setBackground(backgroundDefault);
		combo_PA6.setItems(PA6List);
		combo_PA6.setBackground(backgroundDefault);
		combo_PA7.setItems(PA7List);
		combo_PA7.setBackground(backgroundDefault);
		combo_PA8.setItems(PA8List);
		combo_PA8.setBackground(backgroundDefault);
		combo_PA9.setItems(PA9List);
		combo_PA9.setBackground(backgroundDefault);
		combo_PA10.setItems(PA10List);
		combo_PA10.setBackground(backgroundDefault);
		combo_PA11.setItems(PA11List);
		combo_PA11.setBackground(backgroundDefault);
		combo_PA12.setItems(PA12List);
		combo_PA12.setBackground(backgroundDefault);
		combo_PA13.setItems(PA13List);
		combo_PA13.setBackground(backgroundDefault);
		combo_PA14.setItems(PA14List);
		combo_PA14.setBackground(backgroundDefault);
		combo_PA15.setItems(PA15List);
		combo_PA15.setBackground(backgroundDefault);
		
		combo_PB0.setItems(PB0List);
		combo_PB0.setBackground(backgroundDefault);
		combo_PB1.setItems(PB1List);
		combo_PB1.setBackground(backgroundDefault);
		combo_PB2.setItems(PB2List);
		combo_PB2.setBackground(backgroundDefault);
		combo_PB3.setItems(PB3List);
		combo_PB3.setBackground(backgroundDefault);
		combo_PB4.setItems(PB4List);
		combo_PB4.setBackground(backgroundDefault);
		combo_PB5.setItems(PB5List);
		combo_PB5.setBackground(backgroundDefault);
		combo_PB6.setItems(PB6List);
		combo_PB6.setBackground(backgroundDefault);
		combo_PB7.setItems(PB7List);
		combo_PB7.setBackground(backgroundDefault);
		combo_PB8.setItems(PB8List);
		combo_PB8.setBackground(backgroundDefault);
		combo_PB9.setItems(PB9List);
		combo_PB9.setBackground(backgroundDefault);
		combo_PB10.setItems(PB10List);
		combo_PB10.setBackground(backgroundDefault);
		combo_PB11.setItems(PB11List);
		combo_PB11.setBackground(backgroundDefault);
		combo_PB12.setItems(PB12List);
		combo_PB12.setBackground(backgroundDefault);
		combo_PB13.setItems(PB13List);
		combo_PB13.setBackground(backgroundDefault);
		combo_PB14.setItems(PB14List);
		combo_PB14.setBackground(backgroundDefault);
		combo_PB15.setItems(PB15List);
		combo_PB15.setBackground(backgroundDefault);
		
		combo_PC0.setItems(PC0List);
		combo_PC0.setBackground(backgroundDefault);
		combo_PC1.setItems(PC1List);
		combo_PC1.setBackground(backgroundDefault);
		combo_PC2.setItems(PC2List);
		combo_PC2.setBackground(backgroundDefault);
		combo_PC3.setItems(PC3List);
		combo_PC3.setBackground(backgroundDefault);
		combo_PC4.setItems(PC4List);
		combo_PC4.setBackground(backgroundDefault);
		combo_PC5.setItems(PC5List);
		combo_PC5.setBackground(backgroundDefault);
		combo_PC6.setItems(PC6List);
		combo_PC6.setBackground(backgroundDefault);
		combo_PC7.setItems(PC7List);
		combo_PC7.setBackground(backgroundDefault);
		combo_PC8.setItems(PC8List);
		combo_PC8.setBackground(backgroundDefault);
		combo_PC9.setItems(PC9List);
		combo_PC9.setBackground(backgroundDefault);
		combo_PC10.setItems(PC10List);
		combo_PC10.setBackground(backgroundDefault);
		combo_PC11.setItems(PC11List);
		combo_PC11.setBackground(backgroundDefault);
		combo_PC12.setItems(PC12List);
		combo_PC12.setBackground(backgroundDefault);
		combo_PC13.setItems(PC13List);
		combo_PC13.setBackground(backgroundDefault);
		combo_PC14.setItems(PC14List);
		combo_PC14.setBackground(backgroundDefault);
		combo_PC15.setItems(PC15List);
		combo_PC15.setBackground(backgroundDefault);
		
		combo_PD0.setItems(PD0List);
		combo_PD0.setBackground(backgroundDefault);
		combo_PD1.setItems(PD1List);
		combo_PD1.setBackground(backgroundDefault);
		combo_PD2.setItems(PD2List);
		combo_PD2.setBackground(backgroundDefault);
		combo_PD3.setItems(PD3List);
		combo_PD3.setBackground(backgroundDefault);
		combo_PD4.setItems(PD4List);
		combo_PD4.setBackground(backgroundDefault);
		combo_PD5.setItems(PD5List);
		combo_PD5.setBackground(backgroundDefault);
		combo_PD6.setItems(PD6List);
		combo_PD6.setBackground(backgroundDefault);
		combo_PD7.setItems(PD7List);
		combo_PD7.setBackground(backgroundDefault);
		combo_PD8.setItems(PD8List);
		combo_PD8.setBackground(backgroundDefault);
		combo_PD9.setItems(PD9List);
		combo_PD9.setBackground(backgroundDefault);
		combo_PD10.setItems(PD10List);
		combo_PD10.setBackground(backgroundDefault);
		combo_PD11.setItems(PD11List);
		combo_PD11.setBackground(backgroundDefault);
		combo_PD12.setItems(PD12List);
		combo_PD12.setBackground(backgroundDefault);
		combo_PD13.setItems(PD13List);
		combo_PD13.setBackground(backgroundDefault);
		combo_PD14.setItems(PD14List);
		combo_PD14.setBackground(backgroundDefault);
		combo_PD15.setItems(PD15List);
		combo_PD15.setBackground(backgroundDefault);
		
		combo_PE0.setItems(PE0List);
		combo_PE0.setBackground(backgroundDefault);
		combo_PE1.setItems(PE1List);
		combo_PE1.setBackground(backgroundDefault);
		combo_PE2.setItems(PE2List);
		combo_PE2.setBackground(backgroundDefault);
		combo_PE3.setItems(PE3List);
		combo_PE3.setBackground(backgroundDefault);
		combo_PE4.setItems(PE4List);
		combo_PE4.setBackground(backgroundDefault);
		combo_PE5.setItems(PE5List);
		combo_PE5.setBackground(backgroundDefault);
		combo_PE6.setItems(PE6List);
		combo_PE6.setBackground(backgroundDefault);
		combo_PE7.setItems(PE7List);
		combo_PE7.setBackground(backgroundDefault);
		combo_PE8.setItems(PE8List);
		combo_PE8.setBackground(backgroundDefault);
		combo_PE9.setItems(PE9List);
		combo_PE9.setBackground(backgroundDefault);
		combo_PE10.setItems(PE10List);
		combo_PE10.setBackground(backgroundDefault);
		combo_PE11.setItems(PE11List);
		combo_PE11.setBackground(backgroundDefault);
		combo_PE12.setItems(PE12List);
		combo_PE12.setBackground(backgroundDefault);
		combo_PE13.setItems(PE13List);
		combo_PE13.setBackground(backgroundDefault);
		combo_PE14.setItems(PE14List);
		combo_PE14.setBackground(backgroundDefault);
		combo_PE15.setItems(PE15List);
		combo_PE15.setBackground(backgroundDefault);
		
		combo_PF0.setItems(PF0List);
		combo_PF0.setBackground(backgroundDefault);
		combo_PF1.setItems(PF1List);
		combo_PF1.setBackground(backgroundDefault);
		combo_PF2.setItems(PF2List);
		combo_PF2.setBackground(backgroundDefault);
		combo_PF3.setItems(PF3List);
		combo_PF3.setBackground(backgroundDefault);
		combo_PF4.setItems(PF4List);
		combo_PF4.setBackground(backgroundDefault);
		combo_PF5.setItems(PF5List);
		combo_PF5.setBackground(backgroundDefault);
		combo_PF6.setItems(PF6List);
		combo_PF6.setBackground(backgroundDefault);
		combo_PF7.setItems(PF7List);
		combo_PF7.setBackground(backgroundDefault);
		combo_PF8.setItems(PF8List);
		combo_PF8.setBackground(backgroundDefault);
		combo_PF9.setItems(PF9List);
		combo_PF9.setBackground(backgroundDefault);
		combo_PF10.setItems(PF10List);
		combo_PF10.setBackground(backgroundDefault);
		combo_PF11.setItems(PF11List);
		combo_PF11.setBackground(backgroundDefault);
		combo_PF12.setItems(PF12List);
		combo_PF12.setBackground(backgroundDefault);
		combo_PF13.setItems(PF13List);
		combo_PF13.setBackground(backgroundDefault);
		combo_PF14.setItems(PF14List);
		combo_PF14.setBackground(backgroundDefault);
		combo_PF15.setItems(PF15List);
		combo_PF15.setBackground(backgroundDefault);
		
	}
	
	private void changeCombo(){
		
		/*
		 * PORT A
		 * */
		
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(combo_PA0.getSelectionModel().getSelectedItem() + " - " + combo_PA0.getSelectionModel().getSelectedIndex());
		alert.show();
		
		switch(combo_PA0.getSelectionModel().getSelectedIndex()){
//		case 0: combo_PA0.setBackground(backgroundDefault); combo_PA0.getSelectionModel().clearSelection(0); break; 
		case 1: case 6: combo_PA0.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA0.setBackground(backgroundIO); break;
		case 4: case 5: combo_PA0.setBackground(backgroundPeriph); break;
		default:break;
		}
		if (combo_PA0.getSelectionModel().getSelectedIndex() == 0){ 
			combo_PA0.setBackground(backgroundDefault); 
			combo_PA0.getSelectionModel().clearSelection(0);
		}
		switch(combo_PA1.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA1.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA2.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA2.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA3.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA3.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA3.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA4.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA4.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA4.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA5.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA5.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA5.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA6.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA6.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA6.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA7.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA7.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA8.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA8.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA9.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA9.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA10.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA10.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA10.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA11.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA11.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA11.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA12.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA12.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA12.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA13.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA13.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA13.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA14.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA14.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA14.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PA15.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PA15.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PA15.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PA15.setBackground(backgroundPeriph); break;
		}
		
		/*
		 * PORT B
		 * */
		
		switch(combo_PB0.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB0.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB0.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB0.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB1.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB1.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB2.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB2.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB3.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB3.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB3.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB4.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB4.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB4.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB5.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB5.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB5.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB6.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB6.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB6.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB7.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB7.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB8.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB8.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB9.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB9.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB10.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB10.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB10.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB11.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB11.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB11.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB12.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB12.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB12.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB13.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB13.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB13.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB14.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB14.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB14.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PB15.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PB15.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PB15.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PB15.setBackground(backgroundPeriph); break;
		}
		
		/*
		 * PORT C
		 * */
		
		switch(combo_PC0.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC0.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC0.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC0.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC1.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC1.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC2.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC2.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC3.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC3.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC3.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC4.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC4.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC4.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC5.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC5.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC5.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC6.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC6.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC6.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC7.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC7.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC8.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC8.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC9.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PC9.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PC9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC10.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC10.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC10.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC11.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC11.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC11.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC12.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC12.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC12.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC13.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC13.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC13.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC14.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC14.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC14.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PC15.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 4: combo_PC15.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PC15.setBackground(backgroundIO); break;
		case 5: case 6: combo_PC15.setBackground(backgroundPeriph); break;
		}
		
		/*
		 * PORT D
		 * */
		
		switch(combo_PD0.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD0.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD0.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD0.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD1.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD1.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD2.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD2.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD3.getSelectionModel().getSelectedIndex()){
		case 0: case 4: combo_PD3.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD3.setBackground(backgroundIO); break;
		case 5: case 6: combo_PD3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD4.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD4.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD4.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD5.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD5.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD5.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD6.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD6.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD6.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD7.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD7.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD8.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD8.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD9.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD9.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD10.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD10.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD10.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD11.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD11.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD11.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD12.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD12.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD12.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD13.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD13.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD13.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD14.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD14.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD14.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PD15.getSelectionModel().getSelectedIndex()){
		case 0: combo_PD15.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PD15.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PD15.setBackground(backgroundPeriph); break;
		}
		
		/*
		 * PORT E
		 * */
		
		switch(combo_PE0.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE0.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE0.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE0.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE1.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE1.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE2.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE2.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE3.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE3.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE3.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE4.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE4.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE4.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE5.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE5.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE5.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE6.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE6.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE6.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE7.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE7.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE8.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE8.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE9.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE9.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE10.getSelectionModel().getSelectedIndex()){
		case 0: combo_PE10.setBackground(backgroundDefault); break;
		case 1: case 2: case 3: combo_PE10.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE11.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PE11.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PE11.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE12.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PE12.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PE12.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE13.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PE13.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PE13.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE14.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PE14.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PE14.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PE15.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PE15.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PE15.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PE15.setBackground(backgroundPeriph); break;
		}
		
		
		/*
		 * PORT F
		 * */
		
		switch(combo_PF0.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF0.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF0.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF0.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF1.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF1.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF1.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF1.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF2.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF2.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF2.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF2.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF3.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF3.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF3.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF3.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF4.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 5: case 6: combo_PF4.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF4.setBackground(backgroundIO); break;
		case 4: combo_PF4.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF5.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 5: case 6: combo_PF5.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF5.setBackground(backgroundIO); break;
		case 4: combo_PF5.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF6.getSelectionModel().getSelectedIndex()){
		case 0: case 1: case 6: combo_PF6.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF6.setBackground(backgroundIO); break;
		case 4: case 5: combo_PF6.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF7.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF7.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF7.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF7.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF8.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF8.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF8.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF8.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF9.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF9.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF9.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF9.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF10.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF10.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF10.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF10.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF11.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF11.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF11.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF11.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF12.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF12.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF12.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF12.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF13.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF13.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF13.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF13.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF14.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF14.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF14.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF14.setBackground(backgroundPeriph); break;
		}
		switch(combo_PF15.getSelectionModel().getSelectedIndex()){
		case 0: case 1: combo_PF15.setBackground(backgroundDefault); break;
		case 2: case 3: combo_PF15.setBackground(backgroundIO); break;
		case 4: case 5: case 6: combo_PF15.setBackground(backgroundPeriph); break;
		}
		
	}
	

	
}
