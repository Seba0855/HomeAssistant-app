package com.apeman.homeassistant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlynkData {

    @SerializedName("V0")
    @Expose
    private String temperature;

    @SerializedName("V1")
    @Expose
    private String humidity;

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
