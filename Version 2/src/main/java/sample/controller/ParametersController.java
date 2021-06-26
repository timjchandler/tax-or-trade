package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.tick.Tax;

import java.net.URL;
import java.util.ResourceBundle;

public class ParametersController extends AbstractController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField field0;

    @FXML
    private TextField field1;

    @FXML
    private TextField field2;

    @FXML
    private TextField field3;

    @FXML
    private Text label0;

    @FXML
    private Text label1;

    @FXML
    private Text label2;

    @FXML
    private Text label3;

    @FXML
    private Button taxOrTrade;

    @FXML
    private Text textToT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDependents();
        label2.setText("Required Electricity");
        field2.setPromptText(getWorld().getRequiredElectricity() + "");
        label3.setText("Total ticks (weeks)");
        field3.setPromptText(getWorld().getTotalTicks() + "");
    }

    private void setDependents() {
        if (getWorld().isTaxNotTrade()) {
            label0.setText("Tax Rate");
            field0.setPromptText(Tax.getTaxRate() + "");
            label1.setText("Yearly tax increase");
            field1.setPromptText(getWorld().getTaxIncrement() + "");
            textToT.setText("Current: Tax");
            taxOrTrade.setText("Switch to Trade");
        } else {
            label0.setText("Carbon Cap");
            field0.setPromptText(getWorld().getCap() + "");
            label1.setText("Yearly change in cap");
            field1.setPromptText(getWorld().getCapIncrement() + "");
            textToT.setText("Current: Cap and Trade");
            taxOrTrade.setText("Switch to Tax");
        }
    }

    @FXML
    void toggle(ActionEvent event) {
        getWorld().setTaxOrTrade(!getWorld().isTaxNotTrade());
        setDependents();
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    @FXML
    void accept(ActionEvent event) {
        boolean allGood = true;

        float val0 = canParse(field0.getText(), label0);
        float val1 = canParse(field1.getText(), label1);
        float val2 = canParse(field2.getText(), label2);
        float val3 = canParse(field3.getText(), label3);

        if (val0 != -1) {
            if (getWorld().isTaxNotTrade()) getWorld().setTaxRate(val0);
            else getWorld().setCap(val0);
        } else allGood = false;

        if (val1 != -1) {
            if (getWorld().isTaxNotTrade()) getWorld().setTaxIncrement(val1);
            else getWorld().setCapIncrement(val1);
        } else allGood = false;

        if (val2 != -1) getWorld().setRequiredElectricity(val2);
        else allGood = false;

        if (val3 != -1) getWorld().setTotalTicks((int) val3);
        else allGood = false;

        if (allGood) ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private float canParse(String text, Text label) {
        float value;
        try {
            value = Float.parseFloat(text);
        } catch (NumberFormatException e) {
            value = -1;
        }
        if (value > 0) {
            label.setFill(Color.BLACK);
            return value;
        } else {
            label.setFill(Color.RED);
            return -1;
        }
    }
}
