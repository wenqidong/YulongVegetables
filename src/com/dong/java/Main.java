package com.dong.java;

import com.dong.java.persistence.PersistenceTool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main_view.fxml"));
        primaryStage.setTitle("玉龙蔬菜");
        primaryStage.setScene(new Scene(root, 1400, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        PersistenceTool p = PersistenceTool.getInstance();
        p.getDatafromFile();
        launch(args);
    }
}
