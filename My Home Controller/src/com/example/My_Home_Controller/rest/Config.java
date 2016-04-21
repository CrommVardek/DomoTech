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
    private final String insideLuminosityUrl;
    private final String spicesUrl;
    private final String spotsUrl;


    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        baseUrl = "http://192.168.1.13:8080/";
        //baseUrl = "http://localhost:8080/";
        insideTemperatureUrl = baseUrl + "rest/insideTemperature";
        insideLuminosityUrl = baseUrl + "rest/insideLuminosity";
        spicesUrl = baseUrl + "rest/spices";
        spotsUrl = baseUrl + "rest/spots";
    }

    public String getBaseUrl(){return baseUrl;}
    public String getInsideTemperatureUrl(){
        return insideTemperatureUrl;
    }
    public String getInsideLuminosityUrl(){return insideLuminosityUrl;}
    public String getSpicesUrl(){return spicesUrl;}
    public String getSpotsUrl(){return spotsUrl;}
}
