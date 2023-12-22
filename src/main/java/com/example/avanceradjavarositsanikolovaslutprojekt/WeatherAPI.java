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
import java.util.HashMap;

public class WeatherAPI {

//https://maps.google.com/?q=<lat>,<lon>
// https://maps.google.com/?q=59.40544641022305,17.863678328010483

    public WeatherAPI() {
    }

    private static HashMap<String,HashMap<String, String>> result = new HashMap<>();
    static double min = 99, max = 99, current = 99, feelsLike = 99, wind = 99;
    private static String summary = "Cannot Find information!";

    public static void getWeather(ArrayList<String> points) {
        int stopsCount = 0;
        // other name
        boolean hasFrom = false;
        do{
            try {
                // get the coordinates of the stations
                int spaceIndex = points.get(stopsCount).indexOf(" ");
                String lat = points.get(stopsCount).substring(spaceIndex+1);
                String lon = points.get(stopsCount).substring(0, spaceIndex);
                // get the weather by coordinates
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
                    // The hashmap could be an actual Class object instead and the extacted
                    // values could be assigned to the class's attributes.
                    HashMap<String, String> values = new HashMap<>();
                    JsonValue jv = Json.parse(response.toString());
                    JsonObject jo = jv.asObject();
                    String locationName = jo.getString("name", "No name");
                    values.put("location", locationName);
                    JsonArray ja1 = jo.get("weather").asArray();
                    JsonObject summaryRecord = ja1.get(0).asObject();
                    JsonObject summaryWind = jo.get("wind").asObject();
                    wind = summaryWind.getDouble("speed", 99);
                    values.put("wind", String.valueOf(wind));
                    JsonObject summaryTemp = jo.get("main").asObject();
                    min = summaryTemp.getDouble("temp_min", 99);
                    values.put("min", String.valueOf(min));
                    max = summaryTemp.getDouble("temp_max", 99);
                    values.put("max", String.valueOf(max));
                    current = summaryTemp.getDouble("temp", 99);
                    values.put("current", String.valueOf(current));
                    feelsLike = summaryTemp.getDouble("feels_like", 99);
                    values.put("feelsLike", String.valueOf(feelsLike));
                    String icon = summaryRecord.getString("icon", "No icon id");
                    values.put("iconID", icon);
                    summary = summaryRecord.getString("description", "Cannot Find information!");
                    values.put("summary", summary);
                    // create the result with both points. Despite wanting to have more than 2 points
                    // this code makes it impossible.
                    if(!hasFrom){
                        result.put("from", values);
                        hasFrom = true;
                    } else {
                        result.put("to", values);
                    }
                } else {
                    // Handle the error response. For now it is impossible since I did not manage to
                    // get another http status code.
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

    public static HashMap<String, HashMap<String, String>> getResult() {
        return result;
    }
}
