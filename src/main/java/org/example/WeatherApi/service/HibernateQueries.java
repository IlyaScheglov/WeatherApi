package org.example.WeatherApi.service;

import lombok.RequiredArgsConstructor;
import org.example.WeatherApi.HibernateUtil;
import org.example.WeatherApi.wetherobject.CitiesEntity;
import org.example.WeatherApi.wetherobject.WeatherSafeEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class HibernateQueries {

    private final HibernateUtil hibernateUtil;


    public boolean isThisCityWasSearchedBefore(String cityName){
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        CitiesEntity city = getCityByName(session, cityName);
        transaction.commit();
        session.close();
        return city != null;
    }

    public WeatherSafeEntity getMidTempByCityName(String cityName){
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String hqlQuery = "FROM WeatherSafeEntity w WHERE w.citiesEntity.name = :cName";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("cName", cityName);
        List<WeatherSafeEntity> weatherSafeEntitiesList = query.list();
        transaction.commit();
        session.close();
        return weatherSafeEntitiesList.get(0);
    }

    public void updateWeatherSafe(String cityName,
                                  String date, int midTemp){
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        CitiesEntity city = getCityByName(session, cityName);
        WeatherSafeEntity weather = city.getWeatherSafeEntities().get(0);
        weather.setDateOfSave(date);
        weather.setMidTemperature(midTemp);
        session.save(weather);
        transaction.commit();
        session.close();
    }

    public void addNewCityWithItsMidTemp(String cityName, String date, int midTemp){
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        CitiesEntity city = addNewCity(session, cityName);
        WeatherSafeEntity weather = new WeatherSafeEntity();
        weather.setDateOfSave(date);
        weather.setMidTemperature(midTemp);
        weather.setCitiesEntity(city);
        session.save(weather);
        city.getWeatherSafeEntities().add(weather);
        session.save(city);
        transaction.commit();
        session.close();
    }

    private CitiesEntity addNewCity(Session session, String cityName){
        CitiesEntity city = new CitiesEntity();
        city.setName(cityName);
        session.save(city);
        return city;
    }

    private CitiesEntity getCityByName(Session session, String cityName){
        String hqlQuery = "FROM CitiesEntity c WHERE c.name = :cName";
        Query query = session.createQuery(hqlQuery);
        query.setParameter("cName", cityName);
        List<CitiesEntity> citiesEntityList = query.list();
        if(citiesEntityList.isEmpty()){
            return null;
        }
        return citiesEntityList.get(0);
    }
}
