package org.example.WeatherApi;

import org.example.WeatherApi.wetherobject.WeatherObject;

import java.util.Scanner;

import static org.example.WeatherApi.components.WeatherGetter.getWetherInCity;

public class Main {

    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.print("введите название города: ");
        String cityName = scanner.next();
        scanner.close();
        getWetherInCity(cityName);
    }
}
