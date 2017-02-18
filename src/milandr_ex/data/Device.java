package milandr_ex.data;

import milandr_ex.McuType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by lizard on 14.02.17 at 16:25.
 */
public class Device {
    public enum EPairNames {
        ADC(1) {
            @Override
            public void set(Device device, int val) {
                device.setAdc(val);
            }
        }, COMP(1){
            @Override
            public void set(Device device, int val) {
                device.setComporator(val);
            }
        },  USB(1){
            @Override
            public void set(Device device, int val) {
                device.setUsb(val);
            }
        },  UART(2){
            @Override
            public void set(Device device, int val) {
                device.setUart(val);
            }
        },  CAN(2){
            @Override
            public void set(Device device, int val) {
                device.setCan(val);
            }
        },  SPI(4){
            @Override
            public void set(Device device, int val) {
                device.setSpi(val);
            }
        },  I2C(2){
            @Override
            public void set(Device device, int val) {
                device.setI2c(val);
            }
        },  DAC(1){
            @Override
            public void set(Device device, int val) {
                device.setDac(val);
            }
        },  TMR(4){
            @Override
            public void set(Device device, int val) {
                device.addPair(this, val);
//                device.set(val);
            }
        };

        private final int size;
        EPairNames(int size) {
            this.size = size;
        }

        public boolean ext() {
            for(EPairNames pair: extPairs)
                if (pair.equals(this)) return true;
            return false;
        }
        public int getSize() {
            return size;
        }
        public abstract void set(Device device, int val);
    }
    private static EPairNames[] extPairs = {EPairNames.UART, EPairNames.USB, EPairNames.I2C, EPairNames.SPI, EPairNames.CAN };
    public enum EPortNames {
        A, B, C, D, E, F
    }
    private String name;
    private String body;
    private Integer flash = 128;
    private Integer ram = 32;
    private Integer io = 96;

    private String core = "ARM Cortex-M3";
    private String usbType = "HSY";

    private Map<EPairNames, Integer> pairSets = Maps.newHashMap();
    private Map<EPortNames, Integer> portCounts = Maps.newHashMap();
    private Map<String, Integer> viewsCounts = Maps.newHashMap();

    private List<Integer> exludedPorts = Lists.newArrayList();
    private List<Integer> pairCounts = Lists.newArrayList();;
    private List<Integer> portSizes = Lists.newArrayList();;

    private McuType mcu;

    public Device(String name, String body) {
        this.name = name;
        this.body = body;
        this.mcu = new McuType(name, body);
    }
    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getCore() {
        return core;
    }

    public McuType getMcu() {
        return mcu;
    }

    public Device setMcu(McuType mcu) {
        this.mcu = mcu;
        return this;
    }

    public Device setFlash(Integer flash) {
        this.flash = flash;
        PropsFactory.Basic.flash.set(mcu, flash + " kb");
        return this;
    }

    public Device setRam(Integer ram) {
        this.ram = ram;
        PropsFactory.Basic.ram.set(mcu, ram + " kb");
        return this;
    }

    public Device setIo(Integer io) {
        this.io = io;
        PropsFactory.Basic.io.set(mcu, io + "");
        return this;
    }
    public Device setFlashRamIo(Integer flash, Integer ram, Integer io) {
        setFlash(flash);
        setRam(ram);
        setIo(io);
        return this;
    }

    public Device setCore(String core) {
        this.core = core;
        mcu.setProp("core", core);
        return this;
    }

    public Device setVcc(Double... vcc) {
        PropsFactory.Basic.vcc.set(mcu, String.format("%f...%fV", vcc[0], vcc[1]));
        return this;
    }

    public Device setFreq(Integer freq) {
        PropsFactory.Basic.freq.set(mcu, freq + " MHz");
        return this;
    }

    public Device setTemp(Integer... temp) {
        PropsFactory.Basic.temp.set(mcu, String.format("neg %d°C...+%d°C", temp[0], temp[1]));
        return this;
    }

    public void setUsbType(String usbType) {
        this.usbType = usbType;
        PropsFactory.Basic.usb.set(mcu, "Device и Host FS (до 12 Мбит/с), " + usbType);
    }

    public Device setUsb(Integer usb) {
        addPair(EPairNames.USB, usb);
        return this;
    }

    public Device setUart(Integer uart) {
        addPair(EPairNames.UART, uart);
        PropsFactory.Basic.uart.set(mcu, uart + "");
        return this;
    }

    public Device setCan(Integer can) {
        addPair(EPairNames.CAN, can);
        PropsFactory.Basic.can.set(mcu, can + "");
        return this;
    }

    public Device setSpi(Integer spi) {
        addPair(EPairNames.SPI, spi);
        PropsFactory.Basic.spi.set(mcu, spi + "");
        return this;
    }

    public Device setI2c(Integer i2c) {
        addPair(EPairNames.I2C, i2c);
        PropsFactory.Basic.i2c.set(mcu, i2c + "");
        return this;
    }

    public Device setAdc(Integer adc) {
        addPair(EPairNames.ADC, adc);
        PropsFactory.Basic.adc.set(mcu, adc + " (12 разрядов, 1 Mb/s, 8 каналов)");
        return this;
    }

    public Device setDac(Integer dac) {
        addPair(EPairNames.DAC, dac);
        PropsFactory.Basic.dac.set(mcu, dac + " (12 разрядов)");
        return this;
    }

    public Device setComporator(Integer comporator) {
        addPair(EPairNames.COMP, comporator);
        PropsFactory.Basic.comp.set(mcu, comporator > 0 ? comporator + " разрядов" : "нет");
        return this;
    }

    public Device setExtWire(Integer extWire) {
        PropsFactory.Basic.ext.set(mcu, extWire > 0 ? extWire + " разрядов" : "нет");
        return this;
    }

    public void addPair(EPairNames pair, int count) {
        pairSets.put(pair, count);
        //todo fix-it - illegal extending pairs list if already initialized
        pairCounts.add(count);
    }

    /**
     * ADC, COMP, USB, UART, CAN, SPI, I2C, DAC, TMR
     * @param pairCounts see EPairNames
     * @return this
     */
    public Device addPairs(int... pairCounts) {
        for(int i = 0; i < pairCounts.length; i++) {
            EPairNames pair = EPairNames.values()[i];
            pair.set(this, pairCounts[i]);
        }
        return this;
    }
    public void addPort(EPortNames port, int count) {
        portCounts.put(port, count);
        portSizes.add(port.ordinal() * 100 + count);
    }
    public Device addPorts(int... portsSizes) {
        for(int i = 0; i < portsSizes.length; i++) {
            addPort(EPortNames.values()[i], portsSizes[i]);
        }
        return this;
    }
    public void remPort(int portInd) {
        exludedPorts.add(portInd);
    }
    public Device remPorts(int... portInds) {
        for(int portInd: portInds) {
            remPort(portInd);
        }
        return this;
    }
    public List<Integer> getExludedPorts() {
        return exludedPorts;
    }
    public List<Integer> getPairCounts() {
        return pairCounts;
    }
    public List<Integer> getPortSizes() {
        return portSizes;
    }
    public Integer[] getExludedPortsArr() {
        return getExludedPorts().toArray(new Integer[]{});
    }
    public Integer[] getPairCountsArr() {
        return getPairCounts().toArray(new Integer[]{});
    }
    public Integer[] getPortSizesArr() {
        return getPortSizes().toArray(new Integer[]{});
    }
    public static String getPortText(int port, int index) {
        return getPortText(EPortNames.values()[port], index);
    }
    public static String getPortText(EPortNames port, int index) {
        return port.name() + "-" + (index > 9 ? "" : "0") + index;
    }
}
