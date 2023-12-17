package com.example.avanceradjavarositsanikolovaslutprojekt;

import java.net.MalformedURLException;
import java.net.URL;

public class WeatherAPI {

    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=66fe6923319ff73ef10de2fc1af0a10e");

    public WeatherAPI() throws MalformedURLException {
    }
}
