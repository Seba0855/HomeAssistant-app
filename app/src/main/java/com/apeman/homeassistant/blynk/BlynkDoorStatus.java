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

    public boolean getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(boolean doorStatus) {
        this.doorStatus = doorStatus;
    }
}
