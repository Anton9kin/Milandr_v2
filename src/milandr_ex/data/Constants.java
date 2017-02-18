package milandr_ex.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import milandr_ex.MilandrEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by lizard on 14.02.17 at 10:31.
 */
public class Constants {
    public static String[][] combostrs = {
            {"CPU-1", "SUB-1", "CPU-2"},
            {"USB-1", "SUB-2", ""},
            {"ADC-1", "ADC-2", ""},
            {
                    "HSI|8Mhz|HSI|/ 2|HSE Mhz|16 MHz|HSE|/ 2|\\Sim",
                    "CPU-1|Sim|CPU-1|* 16|\\/ 256",
                    "HSI|8 Mhz|SUB-1|Sim|LSE|32 Khz|LSI|40 Khz|\\Sim",
                    "HSI|8Mhz|HSI|/ 2|HSE|Mhz|HSE|/ 2|\\Sim",
                    "CPU-1|Sim|CPU-1|* 16|\\/ 2",
                    "",
                    "CPU-1|Sim|USB-1|Sim|CPU-2|Sim|USB-2|Sim|\\Sim",
                    "ADC-1|Sim|LSI|40 Khz|LSE|32 Khz|HSI|/ 256|\\/ 256",
                    "",
            }};

    public static ObservableList<String> clItem = FXCollections.
            observableArrayList("IN-1", "IN-2", "IN-3", "IN-4");
    public static ObservableList<String> dv256Item = FXCollections.
            observableArrayList("/ 1", "/ 2", "/ 4", "/ 8", "/ 16", "/ 32", "/ 64", "/ 128", "/ 256");
    public static ObservableList<String> ml256Item = FXCollections.
            observableArrayList("* 1", "* 2", "* 4", "* 8", "* 16", "* 32", "* 64", "* 128", "* 256");
    public static ObservableList<String> dv16Item = FXCollections.
            observableArrayList("/ 1", "/ 2", "/ 4", "/ 8", "/ 16");
    public static ObservableList<String> ml16Item = FXCollections.
            observableArrayList("* 1", "* 2", "* 4", "* 8", "* 16");
    public static ObservableList<String> dv4Item = FXCollections.
            observableArrayList("/ 1", "/ 2", "/ 4");
    public static ObservableList<String> ml4Item = FXCollections.
            observableArrayList("* 1", "* 2", "* 4");
    public static ObservableList<String> dv2Item = FXCollections.
            observableArrayList("/ 1", "/ 2");
    public static ObservableList<String> ml2Item = FXCollections.
            observableArrayList("* 1", "* 2");

    public static ObservableList[] dvItems = new ObservableList[]{dv2Item, dv4Item, dv16Item, dv256Item};
    public static ObservableList[] mlItems = new ObservableList[]{ml2Item, ml4Item, ml16Item, ml256Item};

    public static Map<String, ObservableList> dvmlMap = Maps.newHashMap();
    public static ObservableList getDvMlItems(String code) {
        if (dvmlMap.isEmpty()) {
            for(ObservableList dvItem: dvItems) {
                dvmlMap.put(String.valueOf(dvItem.get(dvItem.size() -1)), dvItem);
            }
            for(ObservableList dvItem: mlItems) {
                dvmlMap.put(String.valueOf(dvItem.get(dvItem.size() -1)), dvItem);
            }
        }
        return dvmlMap.get(code);
    }
    /// Static Constants Block
    public static String[][] comboTexts = {
            {"-", "DATA0", "EXT_INT1", "-"},
            {"-", "DATA1", "TMR1_CH1", "TMR2_CH1"},
            {"-", "DATA2", "TMR1_CH1N", "TMR2_CH1N"},
            {"-", "DATA3", "TMR1_CH2", "TMR2_CH2"},
            {"-", "DATA4", "TMR1_CH2N", "TMR2_CH2N"},
            {"-", "DATA5", "TMR1_CH3", "TMR2_CH3"},
            {"-", "DATA6", "CAN1_TX", "UART1_RXD"},
            {"-", "DATA7", "CAN1_RX", "UART1_TXD"},
            {"-", "DATA8", "TMR1_CH3N", "TMR2_CH3N"},
            {"-", "DATA9", "TMR1_CH4", "TMR2_CH4"},
            {"-", "DATA10", "nUART1DTR", "TMR2_CH4N"},
            {"-", "DATA11", "nUART1RTS", "TMR2_BLK"},
            {"-", "DATA12", "nUART1RI", "TMR2_ETR"},
            {"-", "DATA13", "nUART1DCD", "TMR1_CH4N"},
            {"-", "DATA14", "nUART1DSR", "TMR1_BLK"},
            {"-", "DATA15", "nUART1CTS", "TMR1_ETR"},
            {"-", "DATA16", "TMR3_CH1", "UART1_TXD"},
            {"-", "DATA17", "TMR3_CH1N", "UART2_RXD"},
            {"-", "DATA18", "TMR3_CH2", "CAN1_TX"},
            {"-", "DATA19", "TMR3_CH2N", "CAN1_RX"},
            {"-", "DATA20", "TMR3_BLK", "CAN3_ETR"},
            {"-", "DATA21", "UART1_TXD", "TMR3_CH3"},
            {"-", "DATA22", "UART1_RXD", "TMR3_CH3N"},
            {"-", "DATA23", "nSIROUT1", "TMR3_CH4"},
            {"-", "DATA24", "COMP_OUT", "TMR3_CH4N"},
            {"-", "DATA25", "nSIRIN1", "EXT_INT4"},
            {"-", "DATA26", "EXT_INT2", "nSIROUT1"},
            {"-", "DATA27", "EXT_INT1", "COMP_OUT"},
            {"-", "DATA28", "SSP1_FSS", "SSP2_FSS"},
            {"-", "DATA29", "SSP1_CLK", "SSP2_CLK"},
            {"-", "DATA30", "SSP1_RXD", "SSP2_RXD"},
            {"-", "DATA31", "SSP1_TXD", "SSP2_TXD"},
            {"-", "READY", "SCL1", "SSP2_FSS"},
            {"-", "OE", "SDA1", "SSP2_CLK"},
            {"-", "WE", "TMR3_CH1", "SSP2_RXD"},
            {"-", "BE0", "TMR3_CH1N", "SSP2_TXD"},
            {"-", "BE1", "TMR3_CH2", "TMR1_CH1"},
            {"-", "BE2", "TMR3_CH2N", "TMR1_CH1N"},
            {"-", "BE3", "TMR3_CH3", "TMR1_CH2"},
            {"-", "CLOCK", "TMR3_CH3N", "TMR1_CH2N"},
            {"-", "CAN1_TX", "TMR3_CH4", "TMR1_CH3"},
            {"-", "CAN1_RX", "TMR3_CH4N", "TMR1_CH3N"},
            {"-", "-", "TMR3_ETR", "TMR1_CH4"},
            {"-", "-", "TMR3_BLK", "TMR1_CH4N"},
            {"-", "-", "EXT_INT2", "TMR1_ETR"},
            {"-", "-", "EXT_INT4", "TMR1_BLK"},
            {"-", "-", "SSP2_FSS", "CAN2_RX"},
            {"-", "-", "SSP2_RXD", "CAN2_TX"},
            {"ADC0_REF+", "TMR1_CH1N", "UART2_RXD", "TMR3_CH1"},
            {"ADC1_REF-", "TMR1_CH1", "UART2_TXD", "TMR3_CH1N"},
            {"ADC2", "BUSY1", "SSP2_RXD", "TMR3_CH2"},
            {"ADC3", "-", "SSP2_FSS", "TMR3_CH2N"},
            {"ADC4", "TMR1_ETR", "nSIROUT2", "TMR3_BLK"},
            {"ADC5", "CLE", "SSP2_CLK", "TMR2_ETR"},
            {"ADC6", "ALE", "SSP2_TXD", "TMR2_BLK"},
            {"ADC7", "TMR1_BLK", "nSIRIN2", "UART1_RXD"},
            {"ADC8", "TMR1_CH4N", "TMR2_CH1", "UART1_TXD"},
            {"ADC9", "CAN2_TX", "TMR2_CH1N", "SSP1_FSS"},
            {"ADC10", "TMR1_CH2", "TMR2_CH2", "SSP1_CLK"},
            {"ADC11", "TMR1_CH2N", "TMR2_CH2N", "SSP1_RXD"},
            {"ADC12", "TMR1_CH3", "TMR2_CH3", "SSP1_TXD"},
            {"ADC13", "TMR1_CH3N", "TMR2_CH3N", "CAN1_TX"},
            {"ADC14", "TMR1_CH4", "TMR2_CH4", "CAN1_RX"},
            {"ADC15", "CAN2_RX", "BUSY2", "EXT_INT3"},
            {"DAC2_OUT", "ADDR16", "TMR2_CH1", "CAN1_RX"},
            {"DAC2_REF", "ADDR17", "TMR2_CH1N", "CAN1_TX"},
            {"COMP_IN1", "ADDR18", "TMR2_CH3", "TMR3_CH1"},
            {"COMP_IN2", "ADDR19", "TMR2_CH3N", "TMR3_CH1N"},
            {"COMP_REF+", "ADDR20", "TMR2_CH4N", "TMR3_CH2"},
            {"COMP_REF-", "ADDR21", "TMR2_BLK", "TMR3_CH2N"},
            {"OSC_IN32", "ADDR22", "CAN2_RX", "TMR3_CH3"},
            {"OSC_OUT32", "ADDR23", "CAN2_TX", "TMR3_CH3N"},
            {"COMP_IN3", "ADDR24", "TMR2_CH4", "TMR3_CH4"},
            {"DAC1_OUT", "ADDR25", "TMR2_CH2", "TMR3_CH4N"},
            {"DAC1_REF", "ADDR26", "TMR2_CH2N", "TMR3_ETR"},
            {"-", "ADDR27", "nSIRIN1", "TMR3_BLK"},
            {"-", "ADDR28", "SSP1_RXD", "UART1_RXD"},
            {"-", "ADDR29", "SSP1_FSS", "UART1_TXD"},
            {"-", "ADDR30", "TMR2_ETR", "SCL1"},
            {"-", "ADDR31", "EXT_INT3", "SDA1"},
            {"-", "ADDR0", "SSP1_TXD", "UART2_RXD"},
            {"-", "ADDR1", "SSP1_CLK", "UART2_TXD"},
            {"-", "ADDR2", "SSP1_FSS", "CAN2_RX"},
            {"-", "ADDR3", "SSP1_RXD", "CAN2_TX"},
            {"-", "ADDR4", "-", "-"},
            {"-", "ADDR5", "-", "-"},
            {"-", "ADDR6", "TMR1_CH1", "-"},
            {"-", "ADDR7", "TMR1_CH1N", "TMR3_CH1"},
            {"-", "ADDR8", "TMR1_CH2", "TMR3_CH1N"},
            {"-", "ADDR9", "TMR1_CH2N", "TMR3_CH2"},
            {"-", "ADDR10", "TMR1_CH3", "TMR3_CH2N"},
            {"-", "ADDR11", "TMR1_CH3N", "TMR3_ETR"},
            {"-", "ADDR12", "TMR1_CH4", "SSP2_FSS"},
            {"-", "ADDR13", "TMR1_CH4N", "SSP2_CLK"},
            {"-", "ADDR14", "TMR1_ETR", "SSP2_RXD"},
            {"-", "ADDR15", "TMR1_BLK", "SSP2_TXD"},
    };
    private static Map<String, String[]> pairItems = Maps.newHashMap();
    private static Map<String, Integer[]> pairNumbers = Maps.newHashMap();
    public static String[] genLists(String setName) {
        generateSets(setName);
        return pairItems.get(setName);
    }
    public static Integer[] genNumbers(String setName) {
        generateSets(setName);
        return pairNumbers.get(setName);
    }

    public static String encodeStr(String value) {
        try {
            new String(value.getBytes("ISO-8859-1"), "UTF-8");
//			return  URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static void generateSets(String setName) {
        if (!pairItems.containsKey(setName)) {
            List<String> pairs = Lists.newArrayList();
            List<Integer> numbers = Lists.newArrayList();
            String sName = setName;
            if (!sName.startsWith("ADC")) {
                fillInputs(pairs, numbers, sName);
            }
            Collections.sort(pairs);
            pairs.add(0, "RESET");
            pairItems.put(setName, pairs.toArray(new String[]{}));
            pairNumbers.put(setName, numbers.toArray(new Integer[]{}));
        }
    }

    private static void fillInputs(List<String> pairs, List<Integer> numbers, String sName) {
        if (sName.startsWith("SPI")) sName = "SSP" + (sName.length() > 3 ? sName.substring(3) : "");
        if (sName.startsWith("I2C")) {
            sName = "SDA" + (sName.length() > 3 ? sName.substring(3) : "");
            findInputs(pairs, numbers, sName);
            sName = "SCL" + (sName.length() > 3 ? sName.substring(3) : "");
        }
        findInputs(pairs, numbers, sName);
    }

    private static void findInputs(List<String> pairs, List<Integer> numbers, String sName) {
        for(int i=0; i < comboTexts.length; i++) {
            int idx = -1;
            String str = "";
            String[] comboItems = comboTexts[i];
            for(String cItem: comboItems) {
                if (cItem.contains(sName)) {
                    idx = i;
                    str = cItem;
                    break;
                }
            }
            if (idx >= 0) {
//                String sId = str;//"" + idx;
                    int div = idx % 16;
                    String cbKey = (idx / 16) + (div > 9 ? "" : "0") + div;
                    String sId = str + " " + keyToText("cb" + cbKey);
                if (!pairs.contains(sId)) pairs.add(sId);
                numbers.add(idx);
            }
        }
    }

    public static String keyToText(String key) {
        if (key.matches("cb\\d+")) {
            int portGroup = Integer.parseInt(key.substring(2, 3));
            int portNumber = Integer.parseInt(key.substring(3));
            return String.format("P%s%d", Device.EPortNames.values()[portGroup], portNumber);
        }
        return key;
    }

    public static String textToKey(String text) {
        if (text.matches("P[A-F]\\d+")) {
            int portGroup = Device.EPortNames.valueOf(text.substring(1, 2)).ordinal();
            int portNumber = Integer.parseInt(text.substring(2));
            return String.format("cb%s%s%d", portGroup, portNumber > 9 ? "" : "0", portNumber);
        }
        return text;
    }

    public static ResourceBundle loadBundle(String name, String locale) {
        String resourceName = "resourse/" + name + "_" + locale + ".properties";
        ResourceBundle bundle = null;
        InputStream stream = null;
        URL url = MilandrEx.class.getClassLoader().getResource(resourceName);
        if (url != null) {
            try {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as UTF-8.
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }

}
