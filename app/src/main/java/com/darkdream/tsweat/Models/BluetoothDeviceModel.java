package com.darkdream.tsweat.Models;

public class BluetoothDeviceModel {
    private String deviceName;
    private String deviceAddress;

    public BluetoothDeviceModel(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
}
