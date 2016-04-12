package com.example.My_Home_Controller.rest;

/**
 * Configuration File to hold informations shared by all activities of the application.
 *
 * The urls used to access the RESTful Web Services can be found here.
 *
 * Created by Axel on 12-04-16.
 */
public class Config {
    private static Config ourInstance = new Config();

    private final String baseUrl;
    private final String insideTemperatureUrl;

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        baseUrl = "http://192.168.1.15:8080/";
        //baseUrl = "http://localhost:8080/";
        insideTemperatureUrl = baseUrl + "rest/insideTemperature";
    }


    public String getBaseUrl(){
        return baseUrl;
    }

    public String getInsideTemperatureUrl(){
        return insideTemperatureUrl;
    }
}
