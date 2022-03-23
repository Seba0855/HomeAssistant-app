package com.apeman.homeassistant;

public class CardContent {
    private String roomIndicator;
    private String value;
    private String description;
    private int lineColor;

    public CardContent(String roomIndicator, String value, int lineColor, String description) {
        this.roomIndicator = roomIndicator;
        this.value = value;
        this.lineColor = lineColor;
        this.description = description;
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
}
