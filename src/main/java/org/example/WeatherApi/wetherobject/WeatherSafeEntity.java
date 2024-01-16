package org.example.WeatherApi.wetherobject;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Data
@Table(name = "weather_safe")
public class WeatherSafeEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date_of_save")
    private String dateOfSave;

    @Column(name = "mid_temperature")
    private int midTemperature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CitiesEntity citiesEntity = new CitiesEntity();
}


