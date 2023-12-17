module com.example.avanceradjavarositsanikolovaslutprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires minimal.json;
    requires gson;


    opens com.example.avanceradjavarositsanikolovaslutprojekt to javafx.fxml;
    exports com.example.avanceradjavarositsanikolovaslutprojekt;
}