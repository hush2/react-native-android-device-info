package com.github.hush2.deviceinfo;

import android.annotation.SuppressLint;
import android.hardware.Sensor;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.List;

import github.nisrulz.easydeviceinfo.base.BatteryHealth;
import github.nisrulz.easydeviceinfo.base.ChargingVia;
import github.nisrulz.easydeviceinfo.base.DeviceType;
import github.nisrulz.easydeviceinfo.base.EasyBatteryMod;
import github.nisrulz.easydeviceinfo.base.EasyConfigMod;
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.base.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.base.EasyFingerprintMod;
import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod;
import github.nisrulz.easydeviceinfo.base.EasyNetworkMod;
import github.nisrulz.easydeviceinfo.base.EasyNfcMod;
import github.nisrulz.easydeviceinfo.base.EasySensorMod;
import github.nisrulz.easydeviceinfo.base.EasySimMod;
import github.nisrulz.easydeviceinfo.base.NetworkType;
import github.nisrulz.easydeviceinfo.base.OrientationType;
import github.nisrulz.easydeviceinfo.base.PhoneType;
import github.nisrulz.easydeviceinfo.base.RingerMode;

public class RNEasyDeviceInfoModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNEasyDeviceInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

    }

    @Override
    public String getName() {
        return "RNEasyDeviceInfo";
    }

    @ReactMethod
    public void getBatteryInfo(Promise p) {

        EasyBatteryMod easyBatteryMod = new EasyBatteryMod(reactContext);

        WritableMap batt = Arguments.createMap();

        batt.putInt("batteryPercentage", easyBatteryMod.getBatteryPercentage());
        batt.putBoolean("isDeviceCharging", easyBatteryMod.isDeviceCharging());
        batt.putString("batteryTechnology", String.valueOf(easyBatteryMod.getBatteryTechnology()));
        batt.putDouble("batteryTemperature", easyBatteryMod.getBatteryTemperature());
        batt.putInt("batteryVoltage", easyBatteryMod.getBatteryVoltage());
        batt.putBoolean("isBatteryPresent", easyBatteryMod.isBatteryPresent());

        @BatteryHealth
        int batteryHealth = easyBatteryMod.getBatteryHealth();
        switch (batteryHealth) {
            case BatteryHealth.GOOD:
                batt.putString("batteryHealth", "Good");
                break;
            case BatteryHealth.HAVING_ISSUES:
                batt.putString("batteryHealth", "Having issues");
                break;
            default:
                batt.putString("batteryHealth", "Having issues");
                break;
        }

        @ChargingVia
        int isChargingVia = easyBatteryMod.getChargingSource();
        switch (isChargingVia) {
            case ChargingVia.AC:
                batt.putString("chargingSource", "AC");
                break;
            case ChargingVia.USB:
                batt.putString("chargingSource", "USB");
                break;
            case ChargingVia.WIRELESS:
                batt.putString("chargingSource", "Wireless");
                break;
            case ChargingVia.UNKNOWN_SOURCE:
            default:
                batt.putString("chargingSource", "-");
                break;
        }

        p.resolve(batt);
    }

    @ReactMethod
    public void getSensorInfo(Promise p) {

        WritableArray sensorArray = Arguments.createArray();
        WritableMap sensorMap;

        EasySensorMod easySensorMod = new EasySensorMod(reactContext);
        List<Sensor> allSensors = easySensorMod.getAllSensors();

        for (Sensor sensor : allSensors) {
            sensorMap = Arguments.createMap();

            sensorMap.putString("name", sensor.getName());
            sensorMap.putString("vendor", sensor.getVendor());
            sensorMap.putInt("version", sensor.getVersion());
            sensorMap.putDouble("power", sensor.getPower());
            sensorMap.putDouble("resolution", sensor.getResolution());
            sensorMap.putDouble("maximumRange", sensor.getMaximumRange());

            sensorArray.pushMap(sensorMap);
        }

        p.resolve(sensorArray);
    }

    @ReactMethod
    public void getFingerprintInfo(Promise p) {

        EasyFingerprintMod easyFingerprintMod = new EasyFingerprintMod(reactContext);

        WritableMap fp = Arguments.createMap();

        fp.putBoolean("isFingerprintSensorPresent", easyFingerprintMod.isFingerprintSensorPresent());
        fp.putBoolean("areFingerprintsEnrolled", easyFingerprintMod.areFingerprintsEnrolled());

        p.resolve(fp);
    }

    @ReactMethod
    public void getMemoryInfo(String unit, Promise p) {

        EasyMemoryMod easyMemoryMod = new EasyMemoryMod(reactContext);

        float totalRAM;
        float availableInternalMemorySize;
        float availableExternalMemorySize;
        float totalInternalMemorySize;
        float totalExternalMemorySize;

        switch (unit.toLowerCase()) {

            case "kb":
                totalRAM = easyMemoryMod.convertToKb(easyMemoryMod.getTotalRAM());
                availableInternalMemorySize = easyMemoryMod.convertToKb(easyMemoryMod.getAvailableInternalMemorySize());
                availableExternalMemorySize = easyMemoryMod.convertToKb(easyMemoryMod.getAvailableExternalMemorySize());
                totalInternalMemorySize = easyMemoryMod.convertToKb(easyMemoryMod.getTotalInternalMemorySize());
                totalExternalMemorySize = easyMemoryMod.convertToKb(easyMemoryMod.getTotalExternalMemorySize());
                break;

            case "mb":
                totalRAM = easyMemoryMod.convertToMb(easyMemoryMod.getTotalRAM());
                availableInternalMemorySize = easyMemoryMod.convertToMb(easyMemoryMod.getAvailableInternalMemorySize());
                availableExternalMemorySize = easyMemoryMod.convertToMb(easyMemoryMod.getAvailableExternalMemorySize());
                totalInternalMemorySize = easyMemoryMod.convertToMb(easyMemoryMod.getTotalInternalMemorySize());
                totalExternalMemorySize = easyMemoryMod.convertToMb(easyMemoryMod.getTotalExternalMemorySize());
                break;

            case "gb":
                totalRAM = easyMemoryMod.convertToGb(easyMemoryMod.getTotalRAM());
                availableInternalMemorySize = easyMemoryMod.convertToGb(easyMemoryMod.getAvailableInternalMemorySize());
                availableExternalMemorySize = easyMemoryMod.convertToGb(easyMemoryMod.getAvailableExternalMemorySize());
                totalInternalMemorySize = easyMemoryMod.convertToGb(easyMemoryMod.getTotalInternalMemorySize());
                totalExternalMemorySize = easyMemoryMod.convertToGb(easyMemoryMod.getTotalExternalMemorySize());
                break;

            default:
                totalRAM = easyMemoryMod.getTotalRAM();
                availableInternalMemorySize = easyMemoryMod.getAvailableInternalMemorySize();
                availableExternalMemorySize = easyMemoryMod.getAvailableExternalMemorySize();
                totalInternalMemorySize = easyMemoryMod.getTotalInternalMemorySize();
                totalExternalMemorySize = easyMemoryMod.getTotalExternalMemorySize();
                break;
        }

        WritableMap mem = Arguments.createMap();

        mem.putDouble("totalRAM", totalRAM);
        mem.putDouble("availableInternalMemorySize", availableInternalMemorySize);
        mem.putDouble("availableExternalMemorySize", availableExternalMemorySize);
        mem.putDouble("totalInternalMemorySize", totalInternalMemorySize);
        mem.putDouble("totalExternalMemorySize", totalExternalMemorySize);

        p.resolve(mem);
    }

    @ReactMethod
    public void getDeviceInfo(Promise p) {

        EasyDeviceMod easyDeviceMod = new EasyDeviceMod(reactContext);

        WritableMap device = Arguments.createMap();

        device.putString("screenDisplayID", easyDeviceMod.getScreenDisplayID());
        device.putString("buildVersionCodename", easyDeviceMod.getBuildVersionCodename());
        device.putString("buildVersionIncremental", easyDeviceMod.getBuildVersionIncremental());
        device.putInt("buildVersionSDK", easyDeviceMod.getBuildVersionSDK());
        device.putString("buildID", easyDeviceMod.getBuildID());
        device.putString("manufacturer", easyDeviceMod.getManufacturer());
        device.putString("model", easyDeviceMod.getModel());
        device.putString("osCodename", easyDeviceMod.getOSCodename());
        device.putString("osVersion", easyDeviceMod.getOSVersion());
        device.putString("phoneNo", easyDeviceMod.getPhoneNo());
        device.putString("radioVer", easyDeviceMod.getRadioVer());
        device.putString("product", easyDeviceMod.getProduct());
        device.putString("device", easyDeviceMod.getDevice());
        device.putString("board", easyDeviceMod.getBoard());
        device.putString("hardware", easyDeviceMod.getHardware());
        device.putString("bootloader", easyDeviceMod.getBootloader());
        device.putString("fingerprint", easyDeviceMod.getFingerprint());
        device.putBoolean("isDeviceRooted", easyDeviceMod.isDeviceRooted());
        device.putString("buildBrand", easyDeviceMod.getBuildBrand());
        device.putString("buildHost", easyDeviceMod.getBuildHost());
        device.putString("buildTags", easyDeviceMod.getBuildTags());
        device.putDouble("buildTime", easyDeviceMod.getBuildTime());
        device.putString("buildUser", easyDeviceMod.getBuildUser());
        device.putString("buildVersionRelease", easyDeviceMod.getBuildVersionRelease());

        @DeviceType
        int deviceType = easyDeviceMod.getDeviceType(getCurrentActivity());
        switch (deviceType) {
            case DeviceType.WATCH:
                device.putString("deviceType", "watch");
                break;
            case DeviceType.PHONE:
                device.putString("deviceType", "phone");
                break;
            case DeviceType.PHABLET:
                device.putString("deviceType", "phablet");
                break;
            case DeviceType.TABLET:
                device.putString("deviceType", "tablet");
                break;
            case DeviceType.TV:
                device.putString("deviceType", "tv");
                break;
        }

        @PhoneType
        int phoneType = easyDeviceMod.getPhoneType();
        switch (phoneType) {
            case PhoneType.CDMA:
                device.putString("phoneType", "CDMA");
                break;
            case PhoneType.GSM:
                device.putString("phoneType", "GSM");
                break;
            case PhoneType.NONE:
                device.putString("phoneType", "NONE");
                break;
            default:
                device.putString("phoneType", "Uknown");
                break;
        }

        @OrientationType
        int orientationType = easyDeviceMod.getOrientation(getCurrentActivity());
        switch (orientationType) {
            case OrientationType.LANDSCAPE:
                device.putString("orientation", "landscape");
                break;
            case OrientationType.PORTRAIT:
                device.putString("orientation", "portrait");
                break;
            case OrientationType.UNKNOWN:
            default:
                device.putString("orientation", "uknown");
                break;
        }

        p.resolve(device);
    }

    @ReactMethod
    public void getDisplayInfo(Promise p) {

        EasyDisplayMod easyDisplayMod = new EasyDisplayMod(reactContext);

        WritableMap display = Arguments.createMap();

        display.putString("resolution", easyDisplayMod.getResolution());
        display.putString("density", easyDisplayMod.getDensity());
        // display.putString("displayXYCoordinates", easyDisplayMod.getDisplayXYCoordinates());
        display.putDouble("refreshRate", easyDisplayMod.getRefreshRate());
        display.putDouble("physicalSize", easyDisplayMod.getPhysicalSize());

        p.resolve(display);
    }


    @ReactMethod
    public void getConfigInfo(Promise p) {

        EasyConfigMod easyConfigMod = new EasyConfigMod(reactContext);

        WritableMap config = Arguments.createMap();

        config.putBoolean("isRunningOnEmulator", easyConfigMod.isRunningOnEmulator());
        config.putDouble("time", easyConfigMod.getTime());
        config.putDouble("uptime", easyConfigMod.getUpTime());
        config.putString("formattedTime", easyConfigMod.getFormattedTime());
        config.putDouble("currentDate", easyConfigMod.getCurrentDate().getTime());
        config.putString("formattedDate", easyConfigMod.getFormattedDate());
        config.putBoolean("hasSdCard", easyConfigMod.hasSdCard());

        @RingerMode
        int ringermode = easyConfigMod.getDeviceRingerMode();
        switch (ringermode) {
            case RingerMode.NORMAL:
                config.putString("ringerMode", "Normal");
                break;
            case RingerMode.VIBRATE:
                config.putString("ringerMode", "Vibrate");
                break;
            case RingerMode.SILENT:
                config.putString("ringerMode", "Silent");
                break;
            default:
                config.putString("ringerMode", "-");
                break;
        }

        p.resolve(config);
    }

    @SuppressLint("MissingPermission")
    @ReactMethod
    public void getSimInfo(Promise p) {

        EasySimMod easySimMod = new EasySimMod(reactContext);

        WritableMap sim = Arguments.createMap();

        sim.putString("imsi", easySimMod.getIMSI());
        sim.putString("serial", easySimMod.getSIMSerial());
        sim.putString("country", easySimMod.getCountry());
        sim.putString("carrier", easySimMod.getCarrier());
        sim.putBoolean("isSimNetworkLocked", easySimMod.isSimNetworkLocked());
        //sim.putString("activeMultiSimInfo", easySimMod.getActiveMultiSimInfo());
        sim.putBoolean("isMultiSim", easySimMod.isMultiSim());
        sim.putInt("numberOfActiveSim", easySimMod.getNumberOfActiveSim());

        p.resolve(sim);
    }

    @ReactMethod
    public void getNfcInfo(Promise p) {

        EasyNfcMod easyNfcMod = new EasyNfcMod(reactContext);

        WritableMap nfc = Arguments.createMap();

        nfc.putBoolean("isNfcPresent", easyNfcMod.isNfcPresent());
        nfc.putBoolean("isNfcEnabled", easyNfcMod.isNfcEnabled());

        p.resolve(nfc);
    }

    @ReactMethod
    public void getNetworkInfo(Promise p) {

        EasyNetworkMod easyNetworkMod = new EasyNetworkMod(reactContext);

        WritableMap net = Arguments.createMap();

        net.putBoolean("isNetworkAvailable", easyNetworkMod.isNetworkAvailable());
        net.putBoolean("isWifiEnabled", easyNetworkMod.isWifiEnabled());
        net.putString("iPv4Address", easyNetworkMod.getIPv4Address());
        net.putString("iPv6Address", easyNetworkMod.getIPv6Address());
        net.putString("wifiSSID", easyNetworkMod.getWifiSSID());
        net.putString("wifiBSSID", easyNetworkMod.getWifiBSSID());
        net.putString("wifiLinkSpeed", easyNetworkMod.getWifiLinkSpeed());
        net.putString("wifiMAC", easyNetworkMod.getWifiMAC());

        @NetworkType
        int networkType = easyNetworkMod.getNetworkType();
        switch (networkType) {
            case NetworkType.CELLULAR_UNKNOWN:
                net.putString("networkType", "Unknown");
                break;
            case NetworkType.CELLULAR_UNIDENTIFIED_GEN:
                net.putString("networkType", "Cellular Unidentified Generation");
                break;
            case NetworkType.CELLULAR_2G:
                net.putString("networkType", "Cellular 2G");
                break;
            case NetworkType.CELLULAR_3G:
                net.putString("networkType", "Cellular 3G");
                break;
            case NetworkType.CELLULAR_4G:
                net.putString("networkType", "Cellular 4G");
                break;
            case NetworkType.WIFI_WIFIMAX:
                net.putString("networkType", "WIFI/WIFIMAX");
                break;
            case NetworkType.UNKNOWN:
            default:
                net.putString("networkType", "Unknown");
                break;
        }
        p.resolve(net);
    }

    @SuppressLint("MissingPermission")
    @ReactMethod
    public void getLocationInfo(Promise p) {

        EasyLocationMod easyLocationMod = new EasyLocationMod(reactContext);

        WritableMap loc = Arguments.createMap();

        double[] ll = easyLocationMod.getLatLong();

        loc.putString("latt", String.valueOf(ll[0]));
        loc.putString("long", String.valueOf(ll[1]));

        p.resolve(loc);
    }


}