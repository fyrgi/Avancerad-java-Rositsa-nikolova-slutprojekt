package com.example.avanceradjavarositsanikolovaslutprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherByDestinationController {
    @FXML
    private TextArea errorResult;
    @FXML
    private Label fromCurrent, fromMax, fromMin, fromFeelsLike, fromDestination, fromSummaryText, fromWind;
    @FXML
    private Label toCurrent, toMax, toMin, toFeelsLike, toDestination, toSummaryText, toWind;
    @FXML
    private ImageView fromSummaryImage, toSummaryImage;
    @FXML
    private TextField travelFrom, travelTo, difference;
    @FXML
    private GridPane resultGrid;
    @FXML
    protected void onSearchButtonClick() {
        ArrayList<String> points = new ArrayList<>();
        String from = travelFrom.getText();
        String to = travelTo.getText();
        points.add(from);
        points.add(to);
        ArrayList<String> resultGpsPoints = TrafficAPI.readTraffik(points);
        // if there are no errors in the trafficapi the system shows the result grid, otherwise
        // it hides the result grid and shows the error textare instead.
        if(TrafficAPI.getError().isEmpty()){
            WeatherAPI.getWeather(resultGpsPoints);
            getTheResult();
            resultGrid.setOpacity(100);
            errorResult.setOpacity(0);

        } else {
            errorResult.setOpacity(100);
            errorResult.setText(TrafficAPI.getError());
            resultGrid.setOpacity(0);
            difference.setOpacity(0);
        }

        points.clear();

    }

    // Set the values in the result grid which is displayed if there are no errors.
    @FXML
    public void getTheResult(){
        HashMap<String, HashMap<String, String>> allData = new HashMap<String, HashMap<String, String>>();
        allData = WeatherAPI.getResult();
        for(String points: allData.keySet()){
            HashMap<String, String> cityData = allData.get(points);
            for(String key: cityData.keySet()){
                // another hardcoded part where I make it impossible to have more than 2 stations.
                // Probably it will be better if this data belongs to an object of a class.
                if(points.equals("from")){
                    fromDestination.setText(cityData.get("location"));
                    fromMin.setText(cityData.get("min")+" °C");
                    fromMax.setText(cityData.get("max")+" °C");
                    fromCurrent.setText(cityData.get("current")+" °C");
                    fromFeelsLike.setText(cityData.get("feelsLike")+" °C");
                    fromSummaryText.setText(cityData.get("summary"));
                    fromWind.setText(cityData.get("wind")+" m/s");
                    fromSummaryImage.setImage(new Image("https://openweathermap.org/img/wn/"+cityData.get("iconID")+"@2x.png"));
                } else {
                    toDestination.setText(cityData.get("location"));
                    toMin.setText(cityData.get("min")+" °C");
                    toMax.setText(cityData.get("max")+" °C");
                    toCurrent.setText(cityData.get("current")+" °C");
                    toFeelsLike.setText(cityData.get("feelsLike")+" °C");
                    toSummaryText.setText(cityData.get("summary"));
                    toWind.setText(cityData.get("wind")+" m/s");
                    toSummaryImage.setImage(new Image("https://openweathermap.org/img/wn/"+cityData.get("iconID")+"@2x.png"));
                }
            }
        }
        endSummary();
    }

    // A little summary of the difference in the Feels like temperature.
    public void endSummary(){
        float from = Float.parseFloat(fromFeelsLike.getText().substring(0,4));
        float to = Float.parseFloat(toFeelsLike.getText().substring(0,4));
        difference.setOpacity(100);
        if(to < from && (from - to) > 1){
            // the double makes the result too sensitive and not formatted well. Changed to float.
            difference.setText("Currently it feels colder in "+toDestination.getText()+" by " + (from - to) + " degrees.");
            difference.setStyle("-fx-background-color: #26676e;");
        } else if (from < to && (to - from) > 1){
            difference.setText("Currently feels warmer in "+toDestination.getText()+" by " + (to - from) + " degrees.");
            difference.setStyle("-fx-background-color: #d68f4d;");
        } else {
            difference.setText("Currently it feels the same at both places.");
            difference.setStyle("-fx-background-color: #4dd68b;");
        }
    }
}