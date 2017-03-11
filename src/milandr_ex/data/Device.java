package milandr_ex.data;

import com.google.common.collect.Sets;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import io.swagger.models.auth.In;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import milandr_ex.McuType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizard on 14.02.17 at 16:25.
 */
public class Device {
    public enum EPairNames {
        NON(0), GPIO(0),
        ADC(1) {
            @Override
            public void set(Device device, int val) {
                device.setAdc(val);
            }
        }, COMP(3){
            @Override
            public void set(Device device, int val) {
                device.setComporator(val);
            }
        },
        USB(1), UART(2), CAN(2), SPI(4), I2C(2), DAC(1), DMA(1),
        SYST(1), MPU(1), BKP(1), PWR(1), EBC(1), IWDG(1), WWDG(1),
        TMR(3);

        private final int size;
        EPairNames(int size) {
            this.size = size;
        }

        public boolean ext() {
            for(EPairNames pair: extPairs)
                if (pair.equals(this)) return true;
            return false;
        }
        public boolean real() {
            return ordinal() >= ADC.ordinal();
        }
        public int getSize() {
            return size;
        }
        public void set(Device device, int val) {
            device.addPair(this, val, true);
        }
		private McuBlockModel model;
        public McuBlockModel model() {
        	if (model == null) model = new McuBlockModel(this);
        	return model;
		}
    }
    private static EPairNames[] extPairs = {EPairNames.UART, EPairNames.USB,
            EPairNames.I2C, EPairNames.SPI, EPairNames.EBC, EPairNames.CAN };
    private static List<String> extPairNames;
    public static List<String> extPairNames() {
        if (extPairNames == null) {
            extPairNames = Lists.newArrayList();
            for(EPairNames pair: extPairs) extPairNames.add(pair.name());
        }
        return extPairNames;
    }
    private static List<EPairNames> pairValues = Lists.newArrayList(EPairNames.values());
    private static Set<String> pairsSet = Sets.newHashSet();
    public static boolean pairExists(String name) {
        if (pairsSet.isEmpty()) {
            for (EPairNames c : EPairNames.values()) {
                pairsSet.add(c.name());
            }
        }
        return pairsSet.contains(name);
    }
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

    private final Map<String, PropsFactory.PropSetter<String>> sprops = Maps.newHashMap();
    private final Map<String, PropsFactory.PropSetter<Integer>> iprops = Maps.newHashMap();
    private final Map<String, PropsFactory.Prop2Setter<Integer>> i2props = Maps.newHashMap();
    private final Map<String, PropsFactory.Prop2Setter<Double>> d2props = Maps.newHashMap();
    private Map<EPairNames, Integer> pairSets = Maps.newHashMap();
    private Map<EPortNames, Integer> portCounts = Maps.newHashMap();
    private Map<String, Integer> viewsCounts = Maps.newHashMap();

    private List<Integer> exludedPorts = Lists.newArrayList();
    private List<Integer> pairCounts = Lists.newArrayList();;
    private List<Integer> portSizes = Lists.newArrayList();;

    private McuType mcu;

    private Device() { }
    public Device(String name, String body) {
        initProps();
        this.name = name;
        this.body = body;
        this.mcu = new McuType(name, body);
    }
    private void initProps() {
        sprops.put("name", this::setName);
        sprops.put("body", this::setBody);
        sprops.put("core", this::setCore);
        sprops.put("usbt", this::setUsbType);
//        iprops.put("usb", this::setUsb);
        iprops.put("flash", this::setFlash);
        iprops.put("ram", this::setRam);
        iprops.put("io", this::setIo);
        iprops.put("ext", this::setExtWire);
        iprops.put("freq", this::setFreq);
        i2props.put("temp", this::setTemp);
        d2props.put("vcc", this::setVcc);
        Device that = this;
        for(EPairNames pair: EPairNames.values()) {
            if (!pair.real()) continue;
            iprops.put(pair.name().toLowerCase(), value -> pair.set(that, value));
        }
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

    public void setProp(String[] nameAndValues) {
        if (nameAndValues.length < 2) return;
        String name = nameAndValues[0];
        String value = nameAndValues[1];
        String value2 = nameAndValues.length > 2 ? nameAndValues[2] : value;
        if (value.matches("\\d+")) {
            if (i2props.containsKey(name)) {
                i2props.get(name).setProp(Integer.parseInt(value), Integer.parseInt(value2));
            }
            if (!iprops.containsKey(name)) return;
            iprops.get(name).setProp(Integer.parseInt(value));
        } else if (value.matches("\\d+\\.\\d+")) {
            if (!d2props.containsKey(name)) return;
            d2props.get(name).setProp(Double.parseDouble(value), Double.parseDouble(value2));
        } else {
            if (!sprops.containsKey(name)) return;
            sprops.get(name).setProp(value);
        }
    }

    public void setName(String name) {
        this.name = name;
        checkNameAndBody();
    }

    public void setBody(String body) {
        this.body = body;
        checkNameAndBody();
    }

    private void checkNameAndBody() {
        if (getMcu() != null) return;
        if (name == null || name.trim().isEmpty()) return;
        if (body == null || body.trim().isEmpty()) return;
        setMcu(new McuType(name, body));
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
        addPair(EPairNames.EBC, extWire);
        PropsFactory.Basic.ext.set(mcu, extWire > 0 ? extWire + " разрядов" : "нет");
        return this;
    }

    public Device addPair(EPairNames pair, int count) {
        addPair(pair, count, false);
        return this;
    }
    public Device addPair(EPairNames pair, int count, boolean withBasic) {
        pairSets.put(pair, count);
        //xtodo fix-it - illegal extending pairs list if already initialized
        if (pairCounts.size() <= pair.ordinal()) {
            pairCounts.add(count);
        } else {
            pairCounts.remove(pair.ordinal());
            pairCounts.add(pair.ordinal(), count);
        }
        if (withBasic) {
            PropsFactory.Basic.valueOf(pair.name().toLowerCase()).set(mcu, count + "");
        }
        return this;
    }

    /**
     * ADC, COMP, USB, UART, CAN, SPI, I2C, DAC<br>
     * SYST, MPU, BKP, PWR, EBC, IWDG, WWDG, TMR
     * @param pairCounts see EPairNames
     * @return this
     */
    public Device addPairs(int... pairCounts) {
        EPairNames[] pairNames = EPairNames.values();
        for(int i = 0; i < pairCounts.length && i < pairNames.length; i++) {
            EPairNames pair = pairNames[i];
            if (!pair.real()) {
				this.pairCounts.add(0);
            	continue;
			}
            pair.set(this, pairCounts[i]);
        }
        return this;
    }
    public void addPort(EPortNames port, int count) {
        portCounts.put(port, count);
        int pInd = port.ordinal();
        if (portSizes.size() <= pInd) {
            portSizes.add(pInd * 100 + count);
        } else {
            portSizes.remove(pInd);
            portSizes.add(pInd,  pInd * 100  + count);
        }
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
    public Integer getPairCount(String pairName) {
        try {
            EPairNames pair = EPairNames.valueOf(pairName);
            return getPairCount(pair);
        } catch (IllegalArgumentException iae) {
            return -1;
        }
    }
    public Integer getPairCount(EPairNames pair) {
        return pairCounts.get(pair.ordinal());
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

    public static Device load(File file) {
        if (file == null || !file.exists()) return null;
        List<String> strings = Constants.loadTxtStrings(file);
        if (strings == null || strings.isEmpty()) return null;
        Device device = new Device();
        for(String str: strings) {
            if (!str.contains("=")) continue;
            device.setProp(str.split("="));
        }
        return device;
    }

    public Integer getMaxPortSizes() {
        int maxSize = 0;
        for(Integer portSize: portSizes) {
            maxSize = Math.max(maxSize, portSize % 100);
        }
        return maxSize;
    }
}
