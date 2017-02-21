package milandr_ex.data;

import com.google.common.collect.Maps;
import javafx.scene.control.Label;
import milandr_ex.McuType;

import java.util.Map;
import java.util.Set;

/**
 * Created by lizard on 16.02.17 at 15:43.
 */
public class PropsFactory {
    public enum Basic {
        type, pack,  core, flash, ram,
        vcc, freq, temp, io, usb, uart,
        tmr, syst, mpu, bkp, pwr, ebc, dma, iwdg, wwdg,
        can, spi, i2c, adc, dac, comp, ext;
        public void set(McuType mcuType, String value) {
            mcuType.setProp(name(), value);
        }
        public void set(Device device, String value) {
            device.setProp(new String[]{name(), value});
        }
    }

    public interface PropSetter<T> {
        public void setProp(T value);
    }
    public interface Prop2Setter<T> {
        public void setProp(T value, T value2);
    }
    public static String[] mcuPropsNames = {"Процессор", "Корпус",  "Ядро", "ПЗУ", "ОЗУ",
            "Питание", "Частота", "Температура", "USER IO", "USB", "UART",
            "tmr", "syst", "mpu", "bkp", "pwr", "ebc", "dma", "iwdg", "wwdg",
            "CAN", "SPI", "I2C", "ADC", "DAC", "Компаратор", "Внешняя шина"};
    private static Map<String, Prop> propFields = Maps.newHashMap();
    private static boolean initialized = false;

    public static class Prop {
        private String name;
        private String text;
        private Label label;

        public Prop(String name, String text) {
            this.name = name;
            this.text = text;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }
    }
    private static void regProperty(Prop prop) {
        propFields.put(prop.name, prop);
    }
    private static void regProperty(String name, String label) {
        regProperty(new Prop(name, label));
    }
    private static void regDefaults() {
        if (initialized) return;
        for(int i = 0; i < Basic.values().length; i++) {
            regProperty(Basic.values()[i].name(), mcuPropsNames[i]);
        }
        initialized = true;
    }
    public static Prop getProp(String name) {
        regDefaults();
        return propFields.get(name);
    }
    public static int cntProps() {
        regDefaults();
        return propFields.size();
    }
    public static Set<String> nameProps() {
        regDefaults();
        return propFields.keySet();
    }
}
