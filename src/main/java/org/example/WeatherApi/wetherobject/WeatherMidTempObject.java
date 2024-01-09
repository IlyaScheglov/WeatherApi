package org.example.WeatherApi.wetherobject;

import java.io.Serializable;

public class WeatherMidTempObject implements Serializable {

    private int middleTemperature;

    private String dateOfSave;

    public WeatherMidTempObject(int middleTemperature, String dateOfSave) {
        this.middleTemperature = middleTemperature;
        this.dateOfSave = dateOfSave;
    }

    public int getMiddleTemperature() {
        return middleTemperature;
    }

    public String getDateOfSave() {
        return dateOfSave;
    }

    public void setMiddleTemperature(int middleTemperature) {
        this.middleTemperature = middleTemperature;
    }

    public void setDateOfSave(String dateOfSave) {
        this.dateOfSave = dateOfSave;
    }
}
