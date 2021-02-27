package tjc.rug.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class Loading implements Initializable {

//    @FXML
//    private Pane gasStacks;

    @FXML
    private Pane circle0;

    @FXML
    private Pane circle1;

    @FXML
    private Pane circle2;

//    @FXML
//    private Pane loadingBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Animate.setRotate(circle0, 9, false);
        Animate.setRotate(circle1, 7.5f, false);
        Animate.setRotate(circle2, 6, false);
    }
}
