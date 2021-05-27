module tjc.rug {
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.controls;
//    requires org.apache.commons.io;

    opens tjc.rug;
    opens tjc.rug.controller;
    opens tjc.rug.model;
}