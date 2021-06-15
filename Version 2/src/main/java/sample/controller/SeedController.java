package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.World;

import java.net.URL;
import java.util.ResourceBundle;

public class SeedController implements Initializable {

    @FXML
    private TextField valueBox;

    @FXML
    private Text errorPrompt;

    @FXML
    void cancel(ActionEvent event) {
        System.out.println("::POPUP:: Cancel");
        ((Stage) valueBox.getScene().getWindow()).close();
    }

    @FXML
    void setSeed(ActionEvent event) {
        System.out.println("::POPUP:: Set seed");
        int newSeed;
        try {
            newSeed = Integer.parseInt(valueBox.getText());
        } catch (NumberFormatException e) {
            this.errorPrompt.setVisible(true);
            return;
        }
        World.setSeed(newSeed);
        Controller.updateBottomText();
        cancel(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.errorPrompt.setVisible(false);
    }
}

