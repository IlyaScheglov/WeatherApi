package org.example.WeatherApi.components;

import lombok.RequiredArgsConstructor;
import org.example.WeatherApi.HibernateUtil;
import org.example.WeatherApi.config.WaysConfig;
import org.example.WeatherApi.service.DatabaseQueries;
import org.example.WeatherApi.service.HibernateQueries;
import org.example.WeatherApi.wetherobject.WeatherMidTempObject;
import org.example.WeatherApi.wetherobject.WeatherObject;
import org.example.WeatherApi.wetherobject.WeatherSafeEntity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class WeatherGetter {

    private final HibernateQueries hibernateQueries;

    public WeatherGetter() {
        this.hibernateQueries = new HibernateQueries(new HibernateUtil());
    }

    private String firstPartOfUrl = WaysConfig.getFirstPartOfUrl();
    private String secondPartOfUrl = WaysConfig.getSecondPartOfUrl();

    public List<WeatherObject> getWetherInCity(String cityName){
        String output = getUrlContent(firstPartOfUrl + cityName + secondPartOfUrl);
        if(output != null){
            return getWeatherObjFromJSON(output);
        }
        return null;
    }

    public String weatherChangesAnalytic(String cityName,
                                                List<WeatherObject> nowTemperatures){
        AtomicInteger nowTemperaturesSum = new AtomicInteger(0);
        nowTemperatures.forEach(nt -> nowTemperaturesSum
                .set(nowTemperaturesSum.get() + nt.getTemperature()));
        int midTemperatureNow = nowTemperaturesSum.get() / nowTemperatures.size();
        String nowDate = nowTemperatures.get(0).getDate();

        if(!DatabaseQueries.isThisCityWasSearchedBefore(cityName)){
            hibernateQueries.addNewCityWithItsMidTemp(cityName, nowDate, midTemperatureNow);
            return "Погода в этом городе была проверена впервые";
        }

        WeatherSafeEntity weatherEntityLast = hibernateQueries.getMidTempByCityName(cityName);
        int lastTemperature = weatherEntityLast.getMidTemperature();
        String lastDate = weatherEntityLast.getDateOfSave();

        hibernateQueries.updateWeatherSafe(cityName, nowDate, midTemperatureNow);
        return getStringAnalytic(cityName, nowDate, lastDate,
                midTemperatureNow, lastTemperature);
    }

    private String getStringAnalytic(String cityName, String nowDate, String lastDate,
                                     int midTemperatureNow, int lastTemperature){
        StringBuilder result = new StringBuilder();
        result.append("Нынешняя средняя температура " + nowDate + ": " + midTemperatureNow);
        result.append(biggerOrLessInStr(midTemperatureNow, lastTemperature));
        result.append("по сравнению с прошлым запросом средней температуры в городе " +
                cityName + " " + lastDate + ": " + lastTemperature);
        return result.toString();
    }

    private String biggerOrLessInStr(int now, int last){
        if(now > last){
            return " выше ";
        }
        else if(now < last){
            return " ниже ";
        }
        else{
            return " примерно равна ";
        }
    }

    private String getUrlContent(String urlAddress){
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
            return null;
        }
        return stringBuffer.toString();
    }

    private List<WeatherObject> getWeatherObjFromJSON(String jsonWeather){
        JSONObject jsonObject = new JSONObject(jsonWeather);
        List<WeatherObject> result = new ArrayList<>();

        for(int i = 0; i <= 32; i += 8){
            makeNewWeatherObject(i, result, jsonObject);
        }

        return result;
    }

    private void makeNewWeatherObject(int index, List<WeatherObject> result,
                                      JSONObject jsonObject){
        JSONObject litteleJSONObj = jsonObject.getJSONArray("list")
                .getJSONObject(index);

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

    private int convertToRealTemperature(double temp){
        return (int) Math.round(temp - 273.15);
    }

    private int convertToRealPressure(double pressure){
        return (int) Math.round((pressure * 100) / 133.3);
    }

    private String convertToDate(String date){
        return date.substring(0, 10);
    }

}
