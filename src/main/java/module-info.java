module com.example.avanceradjavarositsanikolovaslutprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.avanceradjavarositsanikolovaslutprojekt to javafx.fxml;
    exports com.example.avanceradjavarositsanikolovaslutprojekt;
}