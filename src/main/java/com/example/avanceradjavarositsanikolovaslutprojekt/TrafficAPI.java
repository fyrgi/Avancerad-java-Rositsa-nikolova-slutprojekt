package com.example.avanceradjavarositsanikolovaslutprojekt;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eclipsesource.json.*;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.Gson;
public class TrafficAPI {

    private static ArrayList<String> gpsPoints = new ArrayList<String>();
    private static Pattern pattern = Pattern.compile("[\\d]{2}.[\\d]+\\s[\\d]{2}.[\\d]+");
    private static Matcher matcher;
    public static void readTraffik(ArrayList<String> userSelection){

        int reachedLength = 0;
        do {
            try {
                String trainStation = "<REQUEST>\n" +
                        "  <LOGIN authenticationkey=\"demokey\"/>\n" +
                        "  <QUERY objecttype=\"TrainStation\" namespace=\"rail.infrastructure\" schemaversion=\"1.4\">\n" +
                        "    <FILTER>\n" +
                        "      <EQ name = \"OfficialLocationName\" value = \"" + userSelection.get(reachedLength) + "\" />\n" +
                        "      <EQ name = \"CountryCode\" value = \"SE\" />\n" +
                        "      <EQ name = \"Advertised\" value = \"true\" />\n" +
                        "    </FILTER>\n" +
                        "  </QUERY>\n" +
                        "</REQUEST>";
                // kör nysaste protocoll https
                HttpClient httpClient = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build();
                HttpRequest requestTo = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.trafikinfo.trafikverket.se/v2/data.json"))
                        .method("POST", HttpRequest.BodyPublishers.ofString(trainStation))
                        .header("Content-Type", "application/xml")
                        .build();

                HttpResponse responseTo = httpClient.send(requestTo, HttpResponse.BodyHandlers.ofString());
                JsonValue jsonTo = Json.parse((String) responseTo.body());
                String temp = jsonTo.asObject()
                        .get("RESPONSE").asObject()
                        .get("RESULT").asArray()
                        .get(0).asObject()
                        .get("TrainStation").asArray()
                        .get(0).asObject().get("Geometry").asObject().get("WGS84").asString();
                System.out.println("temp "+ temp);

                matcher = pattern.matcher(temp);
                while(matcher.find()) {
                    temp = matcher.group();
                }

                gpsPoints.add(temp);


                reachedLength++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while (reachedLength < userSelection.size());

        for(String gpsPoint: gpsPoints){
            System.out.println("The GPS points of the stops "+ gpsPoint);
        }
    }
}
