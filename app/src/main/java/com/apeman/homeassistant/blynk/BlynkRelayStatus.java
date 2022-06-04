package com.apeman.homeassistant.blynk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlynkRelayStatus {

    @SerializedName("V4")
    @Expose
    private int firstDevice;

    @SerializedName("V5")
    @Expose
    private int secondDevice;

    public BlynkRelayStatus(int firstDevice, int secondDevice) {
        this.firstDevice = firstDevice;
        this.secondDevice = secondDevice;
    }

    public int getFirstDeviceStatus() {
        return firstDevice;
    }

    public int getSecondDeviceStatus() {
        return secondDevice;
    }

    public void setFirstDeviceStatus(int firstDevice) {
        this.firstDevice = firstDevice;
    }

    public void setSecondDeviceStatus(int secondDevice) {
        this.secondDevice = secondDevice;
    }

}
