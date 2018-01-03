package com.github.hush2.deviceinfo;

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
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod;
import github.nisrulz.easydeviceinfo.base.EasyNfcMod;
import github.nisrulz.easydeviceinfo.base.EasySensorMod;
import github.nisrulz.easydeviceinfo.base.EasySimMod;
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

        WritableMap dev = Arguments.createMap();

        dev.putString("screenDisplayID", easyDeviceMod.getScreenDisplayID());
        dev.putString("buildVersionCodename", easyDeviceMod.getBuildVersionCodename());
        dev.putString("buildVersionIncremental", easyDeviceMod.getBuildVersionIncremental());
        dev.putInt("buildVersionSDK", easyDeviceMod.getBuildVersionSDK());
        dev.putString("buildID", easyDeviceMod.getBuildID());
        dev.putString("manufacturer", easyDeviceMod.getManufacturer());
        dev.putString("model", easyDeviceMod.getModel());
        dev.putString("osCodename", easyDeviceMod.getOSCodename());
        dev.putString("osVersion", easyDeviceMod.getOSVersion());
        dev.putString("phoneNo", easyDeviceMod.getPhoneNo());
        dev.putString("radioVer", easyDeviceMod.getRadioVer());
        dev.putString("product", easyDeviceMod.getProduct());
        dev.putString("device", easyDeviceMod.getDevice());
        dev.putString("board", easyDeviceMod.getBoard());
        dev.putString("hardware", easyDeviceMod.getHardware());
        dev.putString("bootloader", easyDeviceMod.getBootloader());
        dev.putString("fingerprint", easyDeviceMod.getFingerprint());
        dev.putBoolean("isDeviceRooted", easyDeviceMod.isDeviceRooted());
        dev.putString("buildBrand", easyDeviceMod.getBuildBrand());
        dev.putString("buildHost", easyDeviceMod.getBuildHost());
        dev.putString("buildTags", easyDeviceMod.getBuildTags());
        dev.putDouble("buildTime", easyDeviceMod.getBuildTime());
        dev.putString("buildUser", easyDeviceMod.getBuildUser());
        dev.putString("buildVersionRelease", easyDeviceMod.getBuildVersionRelease());

        @DeviceType
        int deviceType = easyDeviceMod.getDeviceType(getCurrentActivity());
        switch (deviceType) {
            case DeviceType.WATCH:
                dev.putString("deviceType", "watch");
                break;
            case DeviceType.PHONE:
                dev.putString("deviceType", "phone");
                break;
            case DeviceType.PHABLET:
                dev.putString("deviceType", "phablet");
                break;
            case DeviceType.TABLET:
                dev.putString("deviceType", "tablet");
                break;
            case DeviceType.TV:
                dev.putString("deviceType", "tv");
                break;
        }

        @PhoneType
        int phoneType = easyDeviceMod.getPhoneType();
        switch (phoneType) {
            case PhoneType.CDMA:
                dev.putString("phoneType", "CDMA");
                break;
            case PhoneType.GSM:
                dev.putString("phoneType", "GSM");
                break;
            case PhoneType.NONE:
                dev.putString("phoneType", "NONE");
                break;
            default:
                dev.putString("phoneType", "Uknown");
                break;
        }

        @OrientationType
        int orientationType = easyDeviceMod.getOrientation(getCurrentActivity());
        switch (orientationType) {
            case OrientationType.LANDSCAPE:
                dev.putString("orientation", "landscape");
                break;
            case OrientationType.PORTRAIT:
                dev.putString("orientation", "portrait");
                break;
            case OrientationType.UNKNOWN:
            default:
                dev.putString("orientation", "uknown");
                break;
        }

        p.resolve(dev);
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

        WritableMap display = Arguments.createMap();

        display.putBoolean("isRunningOnEmulator", easyConfigMod.isRunningOnEmulator());
        display.putDouble("time", easyConfigMod.getTime());
        display.putDouble("uptime", easyConfigMod.getUpTime());
        display.putString("formattedTime", easyConfigMod.getFormattedTime());
        display.putDouble("currentDate", easyConfigMod.getCurrentDate().getTime());
        display.putString("formattedDate", easyConfigMod.getFormattedDate());
        display.putBoolean("hasSdCard", easyConfigMod.hasSdCard());

        @RingerMode
        int ringermode = easyConfigMod.getDeviceRingerMode();
        switch (ringermode) {
            case RingerMode.NORMAL:
                display.putString("ringerMode", "Normal");
                break;
            case RingerMode.VIBRATE:
                display.putString("ringerMode", "Vibrate");
                break;
            case RingerMode.SILENT:
                display.putString("ringerMode", "Silent");
                break;
            default:
                display.putString("ringerMode", "-");
                break;
        }

        p.resolve(display);
    }

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


}