package com.example.avanceradjavarositsanikolovaslutprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    private TextField travelFrom, travelTo;

    @FXML
    private GridPane resultGrid;

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

        if(TrafficAPI.getError().isEmpty()){
            WeatherAPI.getWeather(resultGpsPoints);
            getTheResult();
            resultGrid.setOpacity(100);
            errorResult.setOpacity(0);

        } else {
            errorResult.setOpacity(100);
            errorResult.setText(TrafficAPI.getError());
            resultGrid.setOpacity(0);
        }

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
    }
}