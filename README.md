# react-native-android-device-info

A React Native library for Android to get device information.

This library uses [EasyDeviceInfo](https://github.com/nisrulz/easydeviceinfo).

## Install

```shell
yarn add react-native-android-device-info
```

## Link the library

```shell
react-native link react-native-android-device-info
```

## Permissions

Add required permissions to:

 `<your project>/android/app/src/main/AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> <!-- Network Info -->
<uses-permission android:name="android.permission.INTERNET" /> <!-- Network Info -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> <!-- WiFI Info -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" /> <!-- SIM Info / Phone # -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> <!-- Location Info -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> <!-- Location Info -->
<uses-permission android:name="android.permission.USE_FINGERPRINT" /> <!-- Fingerprint Info -->
```

## How To Use

```js
import { NativeModules } from 'react-native'

const device = NativeModules.RNEasyDeviceInfo

device.getBatteryInfo().then((battery) => {
  console.log(battery)
})
```


## Demo App

Please check [Android Device Info](https://github.com/hush2/android-device-info) for a working demo.


## Credits

[Nishant Srivastava](https://github.com/nisrulz/) for EasyDeviceInfo.

## License

Apache 2.0
