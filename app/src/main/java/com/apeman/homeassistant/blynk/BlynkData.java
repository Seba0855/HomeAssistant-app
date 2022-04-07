package com.apeman.homeassistant.blynk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlynkData {

    @SerializedName("V0")
    @Expose
    private final String temperature;

    @SerializedName("V1")
    @Expose
    private final String humidity;

    public BlynkData(String temperature, String humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}
