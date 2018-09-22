package com.kylecorry.stargazer.ui;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class SplashScreenController {
    private static boolean wasSplashScreenLoaded = false;

    public static boolean wasSplashScreenLoaded(){
        return wasSplashScreenLoaded;
    }


    public void init(Pane window){
        try {
            wasSplashScreenLoaded = true;
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/splash.fxml"));
            window.getChildren().setAll(pane);


            FadeTransition fadeOut = new FadeTransition(new Duration(500), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(new Duration(500));

            fadeOut.play();

            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentPane = FXMLLoader.load(getClass().getResource("/fxml/homepage.fxml"));
                    parentPane.setOpacity(0);
                    window.getChildren().setAll(parentPane);
                    FadeTransition fadeIn = new FadeTransition(new Duration(500), parentPane);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.setCycleCount(1);
                    fadeIn.play();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
