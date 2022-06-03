package com.apeman.homeassistant;

public class CardContent {
    public enum CardType {
        SENSOR, RELAY;
    }

    // Shared parameters
    private int icon;
    private CardType cardType;

    // Sensor parameters
    private String roomIndicator;
    private String value;
    private String description;
    private int lineColor;
    private int valueTextSize;

    // Relay parameters
    private String relayIndicator;
    private String powerFirst;
    private String powerSecond;
    private int firstIndicatorColor;
    private int secondIndicatorColor;

    public CardContent(int icon, CardType cardType) {
        // Setting the type of card
        this.cardType = cardType;
        this.icon = icon;

        if (cardType == CardType.SENSOR) {
            valueTextSize = 34; // Default text size of value indicator set to 34sp
        }
    }

    public void setSensorContent(String roomIndicator, String value, String description, int valueTextSize) {
        if (getCardType() == CardType.SENSOR) {
            setRoomIndicator(roomIndicator);
            setValue(value);
            setDescription(description);

            setValueTextSize(valueTextSize); // Default text size of value indicator set to 34sp
        }
    }

    public void setRelayContent(String relayIndicator, String powerFirst, String powerSecond) {
        if (getCardType() == CardType.RELAY) {
            setRelayIndicator(relayIndicator);
            setPowerFirst(powerFirst);
            setPowerSecond(powerSecond);
        }
    }

    // Shared getter & setters

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }


    // Sensor getter & setters

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

    // Relay getter & setters

    public String getRelayIndicator() {
        return relayIndicator;
    }

    public void setRelayIndicator(String relayIndicator) {
        this.relayIndicator = relayIndicator;
    }

    public String getPowerFirst() {
        return powerFirst;
    }

    public void setPowerFirst(String powerFirst) {
        this.powerFirst = powerFirst;
    }

    public String getPowerSecond() {
        return powerSecond;
    }

    public void setPowerSecond(String powerSecond) {
        this.powerSecond = powerSecond;
    }

    public int getFirstIndicatorColor() {
        return firstIndicatorColor;
    }

    public void setFirstIndicatorColor(int firstIndicatorColor) {
        this.firstIndicatorColor = firstIndicatorColor;
    }

    public int getSecondIndicatorColor() {
        return secondIndicatorColor;
    }

    public void setSecondIndicatorColor(int secondIndicatorColor) {
        this.secondIndicatorColor = secondIndicatorColor;
    }
}
