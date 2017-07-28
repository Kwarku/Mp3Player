package sample.controller;

/**
 * Created by Pawel on 24.07.2017.
 */
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class ControlPaneController implements Initializable{

    @FXML
    private Button nextButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider songSlider;

    @FXML
    private Button prevButton;

    @FXML
    private ToggleButton playButton;

    public Button getNextButton() {
        return nextButton;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public Slider getSongSlider() {
        return songSlider;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public ToggleButton getPlayButton() {
        return playButton;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    

}
