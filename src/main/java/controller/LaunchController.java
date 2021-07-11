package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LaunchController {

    @FXML
    private TextField playerName1TextField;

    private BooleanProperty isPlayerOneWhite = new SimpleBooleanProperty(true);

    @FXML
    private TextField playerName2TextField;
    @FXML
    private Label errorLabel;
    @FXML
    private RadioButton PlayerOneWhite;
    @FXML
    private RadioButton PlayerOneBlack;
    @FXML
    private Label MinutesPerSide;
    @FXML
    private Label IncrementInSeconds;
    @FXML
    private Slider MinutesPerSideSlider;
    @FXML
    private Slider IncrementInSecondsSlider;


    @FXML
    public void initialize() {
        MinutesPerSide.textProperty().bind(Bindings.format(
                "%.0f",
                MinutesPerSideSlider.valueProperty()
        ));
        IncrementInSeconds.textProperty().bind(Bindings.format(
                "%.0f",
                IncrementInSecondsSlider.valueProperty()
        ));
        isPlayerOneWhite.bind(PlayerOneWhite.selectedProperty());

        ToggleGroup  tg = new ToggleGroup();
        PlayerOneBlack.setToggleGroup(tg);
        PlayerOneWhite.setToggleGroup(tg);
    }



    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerName1TextField.getText().isEmpty() ||playerName2TextField.getText().isEmpty() ) {
            errorLabel.setText(" enter all the names , please");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Start.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().initdata(IncrementInSeconds.getText(),MinutesPerSide.getText(),isPlayerOneWhite.get(),playerName1TextField.getText(),playerName2TextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("Player are set to {} and {} ,loading game scene.", playerName1TextField.getText(),playerName2TextField.getText());
        }

    }
}
