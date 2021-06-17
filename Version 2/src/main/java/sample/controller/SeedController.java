package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class SeedController extends AbstractController implements Initializable {

    @FXML
    private TextField valueBox;

    @FXML
    private Text errorPrompt;

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) valueBox.getScene().getWindow()).close();
    }

    @FXML
    void setSeed(ActionEvent event) {
        int newSeed;
        try {
            newSeed = Integer.parseInt(valueBox.getText());
        } catch (NumberFormatException e) {
            this.errorPrompt.setVisible(true);
            return;
        }
        getWorld().updateSeed(newSeed);
        getMainController().updateBottomText();
        cancel(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.errorPrompt.setVisible(false);
    }
}

