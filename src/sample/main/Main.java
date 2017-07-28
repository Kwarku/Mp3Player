package sample.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final String appName = "MP3 Player v1.0";
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainPane.fxml"));
        primaryStage.setTitle(appName);
        primaryStage.setScene(new Scene(root,400,300));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
