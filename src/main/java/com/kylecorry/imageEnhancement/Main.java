package com.kylecorry.imageEnhancement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Kylec on 5/8/2017.
 */
public class Main extends Application {

    public static void main(String args[]) {
        try {
            System.load(new File("../lib/opencv_java320.dll").getAbsolutePath());
        } catch (UnsatisfiedLinkError e) {
            try {
                System.load(new File("libs/opencv_java320.dll").getAbsolutePath());
            } catch (UnsatisfiedLinkError e1) {
                System.err.println("Could not load opencv - FATAL");
                System.exit(1);
            }
        }
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

