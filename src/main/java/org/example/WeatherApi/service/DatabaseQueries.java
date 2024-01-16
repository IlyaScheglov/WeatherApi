package org.example.WeatherApi.service;

import org.example.WeatherApi.wetherobject.WeatherMidTempObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueries {

    //Этот кусок кода я еще не проверял

    private static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WeatherDB", "postgres", "admin759");
            return connection;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static WeatherMidTempObject getMidTempByCityName(String cityName){
        try{
            Connection connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            long cityId = getIdOfCity(statement, cityName);
            WeatherMidTempObject result = getMidWeatherByCityId(statement, cityId);
            connection.commit();
            statement.close();
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void updateWeatherSafe(String cityName,
                                         WeatherMidTempObject midWeather){
        try{
            Connection connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            long cityId = getIdOfCity(statement, cityName);
            updateCityWeather(statement, cityId, midWeather);
            connection.commit();
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addNewCityWithItsMidTemp(String cityName,
                                                WeatherMidTempObject midWeather){
        try{
            Connection connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            long cityId = addCityToDB(statement, cityName);
            addMidCityWeather(statement, cityId, midWeather);
            connection.commit();
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isThisCityWasSearchedBefore(String cityName){
        try {
            String query = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Long> listToCheckSize = new ArrayList<>();
            while(resultSet.next()){
                listToCheckSize.add(resultSet.getLong("id"));
            }
            statement.close();
            return !listToCheckSize.isEmpty();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private static long getIdOfCity(Statement statement, String cityName){
        try {
            String query = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            ResultSet resultSet = statement.executeQuery(query);
            long result = 0L;
            while (resultSet.next()){
                result = resultSet.getLong("id");
                break;
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }

    private static WeatherMidTempObject getMidWeatherByCityId(Statement statement, long cityId){
        try{
            String query = "SELECT * FROM weather_safe WHERE city_id = " + cityId;
            ResultSet resultSet = statement.executeQuery(query);
            WeatherMidTempObject result = null;
            while(resultSet.next()){
                result = new WeatherMidTempObject(resultSet.getInt("mid_temperature"),
                        resultSet.getString("date_of_save"));
                break;
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private static long addCityToDB(Statement statement, String cityName){
        try {
            String insertionQuery = "INSERT INTO cities(name) VALUES('" + cityName + "')";
            String getCityQuery = "SELECT * FROM cities WHERE name = '" + cityName + "'";
            statement.executeUpdate(insertionQuery);
            ResultSet resultSet = statement.executeQuery(getCityQuery);
            long result = 0L;
            while (resultSet.next()) {
                result = resultSet.getLong("id");
                break;
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }

    private static void addMidCityWeather(Statement statement, long cityId, WeatherMidTempObject midTemperature){
        try {
            String query = "INSERT INTO weather_safe(date_of_save, mid_temperature, city_id) VALUES('"
                    + midTemperature.getDateOfSave() + "', " + midTemperature.getMiddleTemperature() +
                    ", " + cityId + ")";
            statement.executeUpdate(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void updateCityWeather(Statement statement, long cityId, WeatherMidTempObject midTemperature){
        try {
            String query = "UPDATE weather_safe SET date_of_save = '"
                    + midTemperature.getDateOfSave() + "', mid_temperature = "
                    + midTemperature.getMiddleTemperature() + " WHERE city_id = " + cityId;
            statement.executeUpdate(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
