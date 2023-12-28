package org.example.WeatherApi.components;

import org.example.WeatherApi.config.WaysConfig;
import org.example.WeatherApi.wetherobject.WeatherObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WeatherGetter {

    private static String firstPartOfUrl = WaysConfig.getFirstPartOfUrl();
    private static String secondPartOfUrl = WaysConfig.getSecondPartOfUrl();

    public static String getWetherInCity(String cityName){
        String output = getUrlContent(firstPartOfUrl + cityName + secondPartOfUrl);
        if(output.equals("no city found")){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        List<WeatherObject> weatherObjects = getWeatherObjfromJSON(output);

        weatherObjects.forEach(wo -> builder.append(wo.toDivHTML()));
        return builder.toString();
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

    private static List<WeatherObject> getWeatherObjfromJSON(String jsonWeather){
        JSONObject jsonObject = new JSONObject(jsonWeather);
        List<WeatherObject> result = new ArrayList<>();

        for(int i = 0; i <= 32; i += 8){
            JSONObject litteleJSONObj = jsonObject.getJSONArray("list")
                    .getJSONObject(i);
            String date = convertToDate(litteleJSONObj
                    .getString("dt_txt"));
            int temperature = convertToRealTemperature(litteleJSONObj
                    .getJSONObject("main").getDouble("temp"));
            int temperatureFeels = convertToRealTemperature(litteleJSONObj
                    .getJSONObject("main").getDouble("feels_like"));
            int minTemperature = convertToRealTemperature(litteleJSONObj
                    .getJSONObject("main").getDouble("temp_min"));
            int maxTemperature = convertToRealTemperature(litteleJSONObj
                    .getJSONObject("main").getDouble("temp_max"));
            int pressure = convertToRealPressure(litteleJSONObj
                    .getJSONObject("main").getDouble("pressure"));
            double windSpeed = litteleJSONObj
                    .getJSONObject("wind").getDouble("speed");
            int humidity = litteleJSONObj
                    .getJSONObject("main").getInt("humidity");

            result.add(new WeatherObject(date, temperature, temperatureFeels,
                    maxTemperature, minTemperature, pressure, windSpeed, humidity));
        }

        return result;
    }

    private static int convertToRealTemperature(double temp){
        return (int) Math.round(temp - 273.15);
    }

    private static int convertToRealPressure(double pressure){
        return (int) Math.round((pressure * 100) / 133.3);
    }

    private static String convertToDate(String date){
        return date.substring(0, 10);
    }

}
