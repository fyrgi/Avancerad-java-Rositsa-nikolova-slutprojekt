package com.example.avanceradjavarositsanikolovaslutprojekt;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WeatherAPI {

//https://maps.google.com/?q=<lat>,<lon>
// https://maps.google.com/?q=59.40544641022305,17.863678328010483

    public static void getWeather(ArrayList<String> points) {
        int stopsCount = 0;

        do{
            try {
                int spaceIndex = points.get(stopsCount).indexOf(" ");
                String lat = points.get(stopsCount).substring(spaceIndex+1);
                String lon = points.get(stopsCount).substring(0, spaceIndex);

                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=5756ae175fada5c93f07cf136851b305&units=metric");

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();
                //  System.out.println("response code:" +responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the InputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Handle the response data
                    System.out.println(response);

                    JsonValue jv = Json.parse(response.toString());
                    JsonObject jo = jv.asObject();
                    JsonArray ja = jo.get("weather").asArray();
                    JsonObject inne = ja.get(0).asObject();
                    String description = inne.getString("description","cant find the stuff!!!");
                    System.out.println(description);


                } else { //404 403 402 etc error koder
                    // Handle the error response
                    System.out.println("Error response code: " + responseCode);
                }
                // Increase the counter to keep finding points
                stopsCount++;
                // Close the connection
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (stopsCount < points.size());
    }
}
