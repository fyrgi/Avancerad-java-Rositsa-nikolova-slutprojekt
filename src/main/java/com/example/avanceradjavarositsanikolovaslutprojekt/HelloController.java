package com.example.avanceradjavarositsanikolovaslutprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField travelFrom;
    @FXML
    private TextField travelTo;

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

        points.add(from);
        points.add(to);
        TrafficAPI.readTraffik(points);
        points.clear();
    }
}