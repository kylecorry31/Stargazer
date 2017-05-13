package com.kylecorry.imageEnhancement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 * Created by Kylec on 5/8/2017.
 */
public class Main extends Application {

    public static void main(String args[]) {
        String opencvpath = System.getProperty("user.dir") + "\\libs\\";
        System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Image Enhancement");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}

