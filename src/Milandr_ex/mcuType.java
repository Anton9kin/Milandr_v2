package Milandr_ex;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class mcuType {

	private final StringProperty type;
	private final StringProperty pack;
	private final StringProperty flash;
	private final StringProperty ram;
	private final StringProperty io;
	
	private final StringProperty core;
	private final StringProperty vcc;
	private final StringProperty freq;
	private final StringProperty temp;
	private final StringProperty usb;
	private final StringProperty uart;
	private final StringProperty can;
	private final StringProperty spi;
	private final StringProperty i2c;
	private final StringProperty adc;
	private final StringProperty dac;
	private final StringProperty comporator;
	private final StringProperty extWire;
	
	/**
	 * Default constructor
	 */
	public mcuType(){
		this(null,null,null,null,null);
	}
	
	/**
	 * Constructor with some initial data
	 */
	public mcuType(String type, String pack, String flash,
					String ram, String io){
		this.type = new SimpleStringProperty(type);
		this.pack = new SimpleStringProperty(pack);
		this.flash = new SimpleStringProperty(flash);
		this.ram = new SimpleStringProperty(ram);
		this.io = new SimpleStringProperty(io);
		
		this.core = new SimpleStringProperty("core");
		this.vcc = new SimpleStringProperty("vcc");
		this.freq = new SimpleStringProperty("freq");
		this.temp = new SimpleStringProperty("temp");
		this.usb = new SimpleStringProperty("usb");
		this.uart = new SimpleStringProperty("uart");
		this.can = new SimpleStringProperty("can");
		this.spi = new SimpleStringProperty("spi");
		this.i2c = new SimpleStringProperty("i2c");
		this.adc = new SimpleStringProperty("adc");
		this.dac = new SimpleStringProperty("dac");
		this.comporator = new SimpleStringProperty("comporator");
		this.extWire = new SimpleStringProperty("extWire");
	}
	
	
	/**
	 * For type
	 */
	public String getType(){
		return type.get();
	}
	public void setType(String value){
		this.type.set(value);
	}
	public StringProperty typeProperty(){
		return this.type;
	}
	
	
	/**
	 * For flash
	 */
	public String getPack(){
		return pack.get();
	}
	public void setPack(String value){
		this.pack.set(value);
	}
	public StringProperty packProperty(){
		return this.pack;
	}
	
	
	/**
	 * For flash
	 */
	public String getFlash(){
		return flash.get();
	}
	public void setFlash(String value){
		this.flash.set(value);
	}
	public StringProperty flashProperty(){
		return this.flash;
	}
	
	
	/**
	 * For ram
	 */
	public String getRam(){
		return ram.get();
	}
	public void setRam(String value){
		this.ram.set(value);
	}
	public StringProperty ramProperty(){
		return this.ram;
	}
	
	
	/**
	 * For io
	 */
	public String getIO(){
		return io.get();
	}
	public void setIO(String value){
		this.io.set(value);
	}
	public StringProperty ioProperty(){
		return this.io;
	}
	
	/**
	 * For core
	 */
	public String getCore(){
		return core.get();
	}
	public void setCore(String value){
		this.core.set(value);
	}
	public StringProperty coreProperty(){
		return this.core;
	}
	
	/**
	 * For vcc
	 */
	public String getVcc(){
		return vcc.get();
	}
	public void setVcc(String value){
		this.vcc.set(value);
	}
	public StringProperty vccProperty(){
		return this.vcc;
	}
	
	/**
	 * For freq
	 */
	public String getFreq(){
		return freq.get();
	}
	public void setFreq(String value){
		this.freq.set(value);
	}
	public StringProperty freqProperty(){
		return this.freq;
	}
	
	/**
	 * For temp
	 */
	public String getTemp(){
		return temp.get();
	}
	public void setTemp(String value){
		this.temp.set(value);
	}
	public StringProperty tempProperty(){
		return this.temp;
	}
	
	/**
	 * For usb
	 */
	public String getUsb(){
		return usb.get();
	}
	public void setUsb(String value){
		this.usb.set(value);
	}
	public StringProperty usbProperty(){
		return this.usb;
	}
	
	/**
	 * For uart
	 */
	public String getUart(){
		return uart.get();
	}
	public void setUart(String value){
		this.uart.set(value);
	}
	public StringProperty uartProperty(){
		return this.uart;
	}
	
	/**
	 * For can
	 */
	public String getCan(){
		return can.get();
	}
	public void setCan(String value){
		this.can.set(value);
	}
	public StringProperty canProperty(){
		return this.can;
	}
	
	/**
	 * For spi
	 */
	public String getSpi(){
		return spi.get();
	}
	public void setSpi(String value){
		this.spi.set(value);
	}
	public StringProperty spiProperty(){
		return this.spi;
	}
	
	/**
	 * For i2c
	 */
	public String getI2c(){
		return i2c.get();
	}
	public void setI2c(String value){
		this.i2c.set(value);
	}
	public StringProperty i2cProperty(){
		return this.i2c;
	}
	
	/**
	 * For adc
	 */
	public String getAdc(){
		return adc.get();
	}
	public void setAdc(String value){
		this.adc.set(value);
	}
	public StringProperty adcProperty(){
		return this.adc;
	}
	
	/**
	 * For dac
	 */
	public String getDac(){
		return dac.get();
	}
	public void setDac(String value){
		this.dac.set(value);
	}
	public StringProperty dacProperty(){
		return this.dac;
	}
	
	/**
	 * For comporator
	 */
	public String getComporator(){
		return comporator.get();
	}
	public void setComporator(String value){
		this.comporator.set(value);
	}
	public StringProperty comporatorProperty(){
		return this.comporator;
	}
	
	/**
	 * For extWire
	 */
	public String getExtWire(){
		return extWire.get();
	}
	public void setExtWire(String value){
		this.extWire.set(value);
	}
	public StringProperty extWireProperty(){
		return this.extWire;
	}
}
