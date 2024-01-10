package org.example.WeatherApi.service;

import org.example.WeatherApi.wetherobject.WeatherMidTempObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueries {

    //Этот кусок кода я еще не проверял

    private static Statement getStatament(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WeatherDB", "postgres", "admin759");
            Statement statement = connection.createStatement();
            return statement;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isThisCityWasSearchedBefore(String cityName){
        try {
            String query = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            Statement statement = getStatament();
            ResultSet resultSet = statement.executeQuery(query);
            List<Long> listToCheckSize = new ArrayList<>();
            while(resultSet.next()){
                listToCheckSize.add(resultSet.getLong("id"));
            }
            statement.close();
            return listToCheckSize.size() > 0;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static long getIdOfCity(String cityName){
        try {
            Statement statement = getStatament();
            String query = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            ResultSet resultSet = statement.executeQuery(query);
            long result = 0L;
            while (resultSet.next()){
                result = resultSet.getLong("id");
                break;
            }
            statement.close();
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }

    public static WeatherMidTempObject getMidWeatherByCityId(long cityId){
        try{
            Statement statement = getStatament();
            String query = "SELECT * FROM weather_safe WHERE city_id = " + cityId;
            ResultSet resultSet = statement.executeQuery(query);
            WeatherMidTempObject result = null;
            while(resultSet.next()){
                result = new WeatherMidTempObject(resultSet.getInt("mid_temperature"),
                        resultSet.getString("date_of_save"));
                break;
            }
            statement.close();
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static long addCityToDB(String cityName){
        try {
            Statement statement = getStatament();
            String insertionQuery = "INSERT INTO cities(name) VALUES('" + cityName + "')";
            String getCityQuery = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            statement.executeUpdate(insertionQuery);
            ResultSet resultSet = statement.executeQuery(getCityQuery);
            long result = 0L;
            while (resultSet.next()) {
                result = resultSet.getLong("id");
                break;
            }
            statement.close();
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }

    public static void addMidCityWeather(long cityId, WeatherMidTempObject midTemperature){
        try {
            Statement statement = getStatament();
            String query = "INSERT INTO weather_safe(date_of_save, mid_temperature, city_id) VALUES('"
                    + midTemperature.getDateOfSave() + "', " + midTemperature.getMiddleTemperature() +
                    ", " + cityId + ")";
            statement.executeUpdate(query);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateCityWeather(long cityId, WeatherMidTempObject midTemperature){
        try {
            Statement statement = getStatament();
            String query = "UPDATE weather_safe SET date_of_save = '"
                    + midTemperature.getDateOfSave() + "', mid_temperature = "
                    + midTemperature.getMiddleTemperature() + " WHERE city_id = " + cityId;
            statement.executeUpdate(query);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
