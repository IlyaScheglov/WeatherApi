package org.example.WeatherApi;


import org.example.WeatherApi.wetherobject.CitiesEntity;
import org.example.WeatherApi.wetherobject.WeatherSafeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    private SessionFactory sessionFactory;

    {
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(WeatherSafeEntity.class);
        cfg.addAnnotatedClass(CitiesEntity.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

}
