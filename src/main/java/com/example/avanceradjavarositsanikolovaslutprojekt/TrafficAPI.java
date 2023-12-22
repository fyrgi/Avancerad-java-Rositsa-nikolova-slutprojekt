package com.example.avanceradjavarositsanikolovaslutprojekt;
import java.io.IOException;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eclipsesource.json.*;

import static java.net.HttpURLConnection.HTTP_OK;

public class TrafficAPI {
    private static ArrayList<String> gpsPoints = new ArrayList<String>(), error = new ArrayList<String>();

    // Create a pattern for the coordinates to handle the result of the API.
    private static Pattern pattern = Pattern.compile("[\\d]{2}.[\\d]+\\s[\\d]{2}.[\\d]+");
    private static Matcher matcher;
    // In case we want to be able to add more stations than just From and To but Via.
    public static ArrayList<String> readTraffik(ArrayList<String> userSelection) {
        gpsPoints.clear();
        error.clear();
        int reachedLength = 0;
        // before doing anything check if there is internet connection
        if(!netIsAvailable()){
            error.add("No internet connection. Try again later.");
            return error;
        } else {
            // In case the used did not provide station name:
            do {
                try {
                    // The current functionality does not need more queries. Otherwise, they have to be handled
                    // in another way
                    String trainStation = "<REQUEST>\n" +
                            "  <LOGIN authenticationkey=\"demokey\"/>\n" +
                            "  <QUERY objecttype=\"TrainStation\" namespace=\"rail.infrastructure\" schemaversion=\"1.4\">\n" +
                            "    <FILTER>\n" +
                            //"      <EQ name = \"OfficialLocationName\" value = \"" + userSelection.get(reachedLength) + "\" />\n" +
                            "      <LIKE name = \"OfficialLocationName\" value = \"/" + userSelection.get(reachedLength) + "/\" />\n" +
                            "      <EQ name = \"CountryCode\" value = \"SE\" />\n" +
                            "      <EQ name = \"Advertised\" value = \"true\" />\n" +
                            "    </FILTER>\n" +
                            "  </QUERY>\n" +
                            "</REQUEST>";

                    HttpClient httpClient = HttpClient.newBuilder()
                            .version(HttpClient.Version.HTTP_2)
                            .build();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(String.valueOf("https://api.trafikinfo.trafikverket.se/v2/data.json")))
                            .method("POST", HttpRequest.BodyPublishers.ofString(trainStation))
                            .header("Content-Type", "application/xml")
                            .build();
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    // get the coordinates only if the response code is 200.
                    if (response.statusCode() == HTTP_OK) {
                        JsonValue jsonTo = Json.parse((String) response.body());
                        try {
                            // get the coordinates which will be used for the weather app
                            // Might be good to take the name of the city because of the unsupported city names
                            //in weather API
                            String coordinates = jsonTo.asObject()
                                    .get("RESPONSE").asObject()
                                    .get("RESULT").asArray()
                                    .get(0).asObject()
                                    .get("TrainStation").asArray()
                                    .get(0).asObject().get("Geometry").asObject().get("WGS84").asString();
                            // extract the coordinates for each station and add them to an ArrayList
                            matcher = pattern.matcher(coordinates);
                            while (matcher.find()) {
                                coordinates = matcher.group();
                            }
                            gpsPoints.add(coordinates);
                            if (userSelection.get(reachedLength).trim().isEmpty()) {
                                error.add("Empty value in station field " + (reachedLength + 1));
                            }
                        } catch (Exception e) {
                            // Prepare an error list that will be shown in case the station was not found in the API response.
                            error.add("Train Station " + userSelection.get(reachedLength) + " does not exist!");
                        }
                        reachedLength++;
                    }
                } catch (Exception e) {
                    error.add("An error occurred. Error message " + e);
                    break;
                }
            } while (reachedLength < userSelection.size());
        }
        if (error.isEmpty()) {
            return gpsPoints;
        } else {
            return error;
        }
    }
    // Return the error arraylist to display them instead of the resultGrid
    public static String getError(){
        StringBuilder converted = new StringBuilder();
        for(String err : error){
            converted.append(err).append("\n");
        }
        return converted.toString();
    }

    // A simple check to know if there is ongoing internet connection. Prevents the system from infinite
    // loop in case of no connection
    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
