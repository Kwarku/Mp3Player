package sample.controller;



import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.mp3.Mp3Parser;
import sample.mp3.Mp3Song;
import sample.mp3.Mp3Player;

public class MainController implements Initializable {

    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private ContentPaneController contentPaneController;
    @FXML
    private MenuPaneController menuPaneController;

    //stworzenie obiektu mp3player
    private Mp3Player mp3Player;
    private Mp3Parser mp3Parser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //wywoalnie wszystkich metod
        mp3Player = new Mp3Player();
        mp3Parser = new Mp3Parser();
       configureControlPaneAction();
       configureVolume();
       configureTable();

       configureMenu();

    }

    //dodanie sterowania menu opcji co umozliwia wczytywanie plikow
    private void configureMenu() {
        MenuItem openFile = menuPaneController.getFileMenuItem();
        MenuItem openDir = menuPaneController.getDirMenuItem();

        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                            //wbudowania opcja jaby czyli filechooser wczytuje tylko pliki z rozszerzeniem mp3
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
                    //otwiera nowa scene z nowym oknem do komunikacji z uzytkkownikiem
                File file = fc.showOpenDialog(new Stage());
                mp3Player.getMp3Collection().clear();
                mp3Player.getMp3Collection().addSong(mp3Parser.createMp3Song(file));
            }
        });

        openDir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                File dir = dc.showDialog(new Stage());
                mp3Player.getMp3Collection().clear();
                mp3Player.getMp3Collection().addSongs(mp3Parser.createMp3Songs(dir));
            }
        });
    }


    //wycztanie pliku muzycznego z tabeli
    private void configureTable(){
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        contentTable.setItems(mp3Player.getMp3Collection().getSongList());
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 ){
                    mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
                    configureProgressBar();
                    controlPaneController.getPlayButton().setSelected(true);
                }
            }
        });
    }

    //sterowanie glosnoscia
    private void configureVolume(){
        Slider volSlider = controlPaneController.getVolumeSlider();
        final double minVolume = 0;
        final double maxVolume = 1;
        volSlider.setMin(minVolume);
        volSlider.setMax(maxVolume);
        volSlider.setValue(maxVolume);
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mp3Player.setVolume(newValue.doubleValue());
            }
        });
    }

    //strowanie przyciskami
    private void configureControlPaneAction(){
        //przywoalnie przyciskow
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button prevButton = controlPaneController.getPrevButton();
        Button nextButton = controlPaneController.getNextButton();

        // play / stop
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playButton.isSelected()){
                    mp3Player.play();
                }else {
                    mp3Player.stop();
                }
            }
        });

        // nastepna piosenka
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() +1);
                mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
            }
        });



        //poprzednia piosenka
        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1);
                mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
            }
        });
    }

    //sterowanie sliderem piosenki
    private void configureProgressBar(){
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        Slider songSlider = controlPaneController.getSongSlider();
        mp3Player.getMediaPlayer().setOnReady(new Runnable() {
            @Override
            public void run() {
                    //ustawienie maksymalnej dlugosci slidera z piosenka na maksymalna dlugosc piosenki
                songSlider.setMax(mp3Player.getLoadedSongLength());
            }
        });
        mp3Player.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                songSlider.setValue(newValue.toSeconds());      // ustawienie obecnego polozenia slidera na obecna dlugosc piosenki
                //z tego co mi sie wydaje to bedzie zmianielo piosenke gdy konczy sie



                if (songSlider.getMax() == songSlider.getValue()){

                    contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
                    mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
        songSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    // przesuniecie slidera piosenki i jednoczesne sprawdzenie czy wartosci sie zmienia, to powoduje przycinanie piosenki xD
                if (songSlider.isValueChanging())
                mp3Player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));

            }
        });
    }


}