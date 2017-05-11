package com.kylecorry.imageEnhancement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kylec on 5/8/2017.
 */
public class Main extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    public static BufferedImage getImage(String filename) {
        try {
            BufferedImage image =  ImageIO.read(new File(filename));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

