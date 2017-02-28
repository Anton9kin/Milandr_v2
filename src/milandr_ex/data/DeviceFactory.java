package milandr_ex.data;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lizard on 16.02.17 at 14:57.
 */
public class DeviceFactory {
    public static Device getDevice(String name) {
        return devices.get(name);
    }

    private static Device makeDevice(String name, String body, int[] pairs, int[] ports) {
        Device device = getDevice(name);
        if (device == null) device = new Device(name, body);
        return device.addPorts(ports).addPairs(pairs);
    }
    private static Device makeDevice(String name, String body, int[] pairs, int[] ports, int[] excls) {
        return makeDevice(name, body, pairs, ports).remPorts(excls);
    }

    public static void registerDevice(Device device) {
        devices.put(device.getBody(), device);
    }

    public static Map<String, Device> devices = Maps.newHashMap();

    public static Device createDefDevice(String name, String body) {
        return createDevice(name, body, "ARM Cortex-M3", 128, 32, 43);
    }
    public static Device createDefDevice(String name, String body, int io) {
        return createDevice(name, body, "ARM Cortex-M3", 128, 32, io);
    }
    public static Device createDevice(String name) {
        return createDevice(name, name, "ARM Cortex-M3");
    }
    public static Device createDevice(String name, String body, String core) {
        return createDevice(name, body, core, 128, 32, 43);
    }
    public static Device createDevice(String name, String body, String core, int flash, int ram, int io) {
        return createDevice(name, body, core, flash, ram, io, new double[]{2.2, 3.6});
    }
    public static Device createDevice(String name, String body, String core, int flash, int ram, int io, double[] vcc) {
        return createDevice(name, body, core, flash, ram, io, vcc, new int[]{60, 125});
    }
    public static Device createDevice(String name, String body, String core, int flash, int ram, int io,
                                      double[] vcc, int[] temp) {
        return createDevice(name, body, core, flash, ram, io, vcc, temp, 80);
    }
    public static Device createDevice(String name, String body, String core, int flash, int ram, int io,
                                      double[] vcc, int[] temp, int freq) {
        return createDevice(name, body, core, flash, ram, io, vcc, temp, freq, 8);
    }
    public static Device createDevice(String name, String body, String core, int flash, int ram, int io,
                                      double[] vcc, int[] temp, int freq, int ext) {
        Device device = new Device(name, body);
        device.setFlashRamIo(flash, ram, io);
        device.setCore(core);
        device.setVcc(vcc[0], vcc[1]);
        device.setTemp(temp[0], temp[1]);
        device.setFreq(freq);
        device.addPairs(2, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2);
        device.addPair(Device.EPairNames.TMR, 3);
        device.addPorts(16, 16, 16, 16, 16, 16);
        device.setUsbType("PHY");
        device.setExtWire(ext);
        registerDevice(device);
        return device;
    }

    public static Device updateDevice(Device device, double[] vcc, int[] temp, int freq, int ext) {
        device.setVcc(vcc[0], vcc[1]);
        device.setTemp(temp[0], temp[1]);
        device.setFreq(freq);
        device.setExtWire(ext);
        return device;
    }

    public static Device updateDevice(Device device, int[] pairs, int[] ports) {
        return updateDevice(device, pairs, ports, null);
    }

    public static Device updateDevice(Device device, int[] pairs, int[] ports, int[] exl) {
        if (pairs != null) device.addPairs(pairs);
        if (ports != null) device.addPorts(ports);
        if (exl != null) device.remPorts(exl);
        return device;
    }
}
