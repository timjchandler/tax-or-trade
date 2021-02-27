module tjc.rug {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;

    opens tjc.rug;
    opens tjc.rug.controller;
    opens tjc.rug.model;
}