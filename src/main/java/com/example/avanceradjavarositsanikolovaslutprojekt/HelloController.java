package com.example.avanceradjavarositsanikolovaslutprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        ArrayList<String> points = new ArrayList<>();
        points.add("Barkarby");
        points.add("Malmö");
        TrafficAPI.readTraffik(points);
    }
}