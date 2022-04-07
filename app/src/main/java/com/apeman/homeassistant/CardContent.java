package com.apeman.homeassistant;

public class CardContent {
    private String roomIndicator;
    private String value;
    private String description;
    private int lineColor;
    private int valueTextSize;

    public CardContent(String roomIndicator, String value, String description) {
        this.roomIndicator = roomIndicator;
        this.value = value;
        this.description = description;

        valueTextSize = 34; // Default text size of value indicator set to 34sp
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public String getRoomIndicator() {
        return roomIndicator;
    }

    public void setRoomIndicator(String roomIndicator) {
        this.roomIndicator = roomIndicator;
    }

    public int getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
    }

}
