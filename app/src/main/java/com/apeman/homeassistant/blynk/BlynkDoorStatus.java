package com.apeman.homeassistant.blynk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlynkDoorStatus {

    @SerializedName("V2")
    @Expose
    private int doorStatus;

    @SerializedName("V3")
    @Expose
    private int windowStatus;

    public BlynkDoorStatus(int doorStatus, int windowStatus) {
        this.doorStatus = doorStatus;
        this.windowStatus = windowStatus;
    }

    public int getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(int doorStatus) {
        this.doorStatus = doorStatus;
    }

    public int getWindowStatus() {
        return windowStatus;
    }

    public void setWindowStatus(int windowStatus) {
        this.windowStatus = windowStatus;
    }

    public String getStatus(int objectStatus) {
        if (objectStatus == 1) {
            return "Open";
        }
        return "Closed";
    }
}
