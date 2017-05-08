package com.kylecorry.imageEnhancement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kylec on 5/8/2017.
 */
public class Main extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    public static BufferedImage getImage(String filename) {
        try {
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static List<BufferedImage> getImages(String directory) {
//        List<BufferedImage> images = new LinkedList<>();
//        List<String> files = getAllFileNames(directory);
//        for (String file : files)
//            images.add(getImage(file));
//        return images;
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Parent root = loader.load();
//            root.getStylesheets().add("/style/material.css");
//            primaryStage.getIcons().add(new Image(("images/logo.png")));
        primaryStage.setTitle("Image Enhancement");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}

