package com.apeman.homeassistant.blynk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlynkDoorStatus {
    @SerializedName("V2")
    @Expose
    private boolean doorStatus;

    public BlynkDoorStatus(boolean doorStatus) {
        this.doorStatus = doorStatus;
    }

    public String getDoorStatus() {
        if (doorStatus) {
            return "Open";
        }
        return "Closed";
    }

    public void setDoorStatus(boolean doorStatus) {
        this.doorStatus = doorStatus;
    }
}
