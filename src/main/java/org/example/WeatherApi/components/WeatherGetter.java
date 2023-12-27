package org.example.WeatherApi.components;

import org.example.WeatherApi.config.WaysConfig;
import org.example.WeatherApi.wetherobject.WeatherObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherGetter {

    private static String firstPartOfUrl = WaysConfig.getFirstPartOfUrl();
    private static String secondPartOfUrl = WaysConfig.getSecondPartOfUrl();

    public static String getWetherInCity(String cityName){
        String output = getUrlContent(firstPartOfUrl + cityName + secondPartOfUrl);
        if(output.equals("no city found")){
            System.out.println("Город не найден");
        }
        return getWeatherObjfromJSON(output, cityName).toDivHTML();
    }

    private static String getUrlContent(String urlAddress){
        StringBuffer stringBuffer = new StringBuffer();

        try{
            String line;
            URL url = new URL(urlAddress);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch (Exception e){
            return "no city found";
        }
        return stringBuffer.toString();
    }

    private static WeatherObject getWeatherObjfromJSON(String jsonWeather, String cityName){
        JSONObject jsonObject = new JSONObject(jsonWeather);

        int temperature = convertToRealTemperature(jsonObject.getJSONArray("list")
                .getJSONObject(0).getJSONObject("main").getDouble("temp"));
        int temperatureFeels = convertToRealTemperature(jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("main").getDouble("feels_like"));
        int minTemperature = convertToRealTemperature(jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("main").getDouble("temp_min"));
        int maxTemperature = convertToRealTemperature(jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("main").getDouble("temp_max"));
        int pressure = convertToRealPressure(jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("main").getDouble("pressure"));
        double windSpeed = jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("wind").getDouble("speed");
        int humidity = (int) jsonObject.getJSONArray("list").getJSONObject(0)
                .getJSONObject("main").getDouble("humidity");

        WeatherObject weatherObject = new WeatherObject(cityName, temperature, temperatureFeels,
                maxTemperature, minTemperature, pressure, windSpeed, humidity);
        return weatherObject;
    }

    private static int convertToRealTemperature(double temp){
        return (int) Math.round(temp - 273.15);
    }

    private static int convertToRealPressure(double pressure){
        return (int) Math.round((pressure * 100) / 133.3);
    }

}
