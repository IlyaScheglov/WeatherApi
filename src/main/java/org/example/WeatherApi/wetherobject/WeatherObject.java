package org.example.WeatherApi.wetherobject;

import java.io.Serializable;

public class WeatherObject implements Serializable{

    private String cityName;

    private int temperature;
    private int temperatureFeels;

    private int maxTemperature;

    private int minTemperature;

    private int pressure;

    private double windSpeed;

    private int humidity;

    public WeatherObject(String cityName, int temperature,
                         int temperatureFeels, int maxTemperature,
                         int minTemperature, int pressure,
                         double windSpeed, int humidity) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.temperatureFeels = temperatureFeels;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getTemperatureFeels() {
        return temperatureFeels;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setTemperatureFeels(int temperatureFeels) {
        this.temperatureFeels = temperatureFeels;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Погода в городе: " + cityName + "\n");
        builder.append("Температура: " + temperature + "°C\n");
        builder.append("Ощущается как: " + temperatureFeels + "°C\n");
        builder.append("Максимальная температура: " + maxTemperature + "°C\n");
        builder.append("Минимальная температура: " + minTemperature + "°C\n");
        builder.append("Давление: " + pressure + "мм.рт.ст.\n");
        builder.append("Скорость ветра: " + windSpeed + "м/с\n");
        builder.append("Влажность: " + humidity + "%\n");
        return builder.toString();
    }
}
