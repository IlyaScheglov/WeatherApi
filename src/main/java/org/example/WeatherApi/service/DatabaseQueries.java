package org.example.WeatherApi.service;

import org.example.WeatherApi.wetherobject.WeatherMidTempObject;

import java.sql.*;

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
            Connection connection = DriverManager.getConnection("jdbc:posgresql://localhost:5432/WeatherDB", "postgres", "admin759");
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
            String query = "SELECT * FROM cities c WHERE c.name = " + cityName;
            Statement statement = getStatament();
            ResultSet resultSet = statement.executeQuery(query);
            statement.close();
            return resultSet != null;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static long getIdOfCity(String cityName){
        try {
            Statement statement = getStatament();
            String query = "SELECT * FROM cities c WHERE c.name = " + cityName;
            ResultSet resultSet = statement.executeQuery(query);
            statement.close();
            while (resultSet.next()){
                return resultSet.getLong("id");
            }
            return 0L;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0L;
        }
    }

    public static WeatherMidTempObject getMidWeatherByCityId(long cityId){
        try{
            Statement statement = getStatament();
            String query = "SELECT * FROM weather_safe ws WHERE ws.city_id = " + cityId;
            ResultSet resultSet = statement.executeQuery(query);
            statement.close();
            while(resultSet.next()){
                return new WeatherMidTempObject(resultSet.getInt("mid_temperature"),
                        resultSet.getDate("date_of_save").toString());
            }
            return null;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
