package org.example.WeatherApi.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class WaysConfig {

    private static final String firstPartOfUrl = "https://api.openweathermap.org/data/2.5/forecast?q=";

    private static final String secondPartOfUrl = "&appid=XXX";

    private static final String firstHTMLPagePath = "/templates/index.html";

    private static final String secondHTMLPagePath = "/templates/weather.html";


    public static String getFirstPartOfUrl(){
        return firstPartOfUrl;
    }

    public static String getSecondPartOfUrl(){
        return secondPartOfUrl;
    }

    public static String getFirstHTMLPage() throws FileNotFoundException{
        StringBuilder builder = new StringBuilder();
        String path = Objects.requireNonNull(WaysConfig.class
                .getResource(firstHTMLPagePath)).getPath();
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine() + "\n");
        }
        scanner.close();
        return builder.toString();
    }

    public static String getSecondHTMLPage() throws FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        String path = Objects.requireNonNull(WaysConfig.class
                .getResource(secondHTMLPagePath)).getPath();
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine() + "\n");
        }
        scanner.close();
        return builder.toString();
    }
}
