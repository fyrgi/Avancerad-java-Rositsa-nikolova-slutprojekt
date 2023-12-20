package com.example.avanceradjavarositsanikolovaslutprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelloController {
    @FXML
    private Label fromCurrent, fromMax, fromMin, fromFeelsLike, fromDestination, fromSummaryText;

    @FXML
    private ImageView fromSummaryImage, toSummaryImage;

    @FXML
    private Label toCurrent, toMax, toMin, toFeelsLike, toDestination, toSummaryText;
    @FXML
    private TextField travelFrom, travelTo;

    @FXML
    protected void onHelloButtonClick() {
        ArrayList<String> points = new ArrayList<>();
        String from = travelFrom.getText();
        String to = travelTo.getText();

        if(from.isEmpty() && to.isEmpty()){
            travelFrom.setText("Barkarby");
            from = travelFrom.getText();
            travelTo.setText("Växjö");
            to = travelTo.getText();
        } else if (from.isEmpty()) {
            travelFrom.setText("Barkarby");
            from = travelFrom.getText();
        } else if (to.isEmpty()) {
            travelTo.setText("Växjö");
            to = travelTo.getText();
        }
        //autocomplete();
        points.add(from);
        points.add(to);
        ArrayList<String> resultGpsPoints = TrafficAPI.readTraffik(points);
        WeatherAPI.getWeather(resultGpsPoints);
        getTheResult();
        points.clear();

    }
    @FXML
    public  void getTheResult(){
        HashMap<String, HashMap<String, String>> allData = new HashMap<String, HashMap<String, String>>();
        allData = WeatherAPI.getResult();
        for(String points: allData.keySet()){
            HashMap<String, String> cityData = allData.get(points);
            for(String key: cityData.keySet()){
                if(points.equals("from")){
                    fromDestination.setText(cityData.get("location"));
                    fromMin.setText(cityData.get("min"));
                    fromMax.setText(cityData.get("max"));
                    fromCurrent.setText(cityData.get("current"));
                    fromFeelsLike.setText(cityData.get("feelsLike"));
                    fromSummaryText.setText(cityData.get("summary"));
                    fromSummaryImage.setImage(new Image("https://openweathermap.org/img/wn/"+cityData.get("iconID")+"@2x.png"));
                } else {
                    toDestination.setText(cityData.get("location"));
                    toMin.setText(cityData.get("min"));
                    toMax.setText(cityData.get("max"));
                    toCurrent.setText(cityData.get("current"));
                    toFeelsLike.setText(cityData.get("feelsLike"));
                    toSummaryText.setText(cityData.get("summary"));
                    toSummaryImage.setImage(new Image("https://openweathermap.org/img/wn/"+cityData.get("iconID")+"@2x.png"));
                }
            }
        }
    }
}