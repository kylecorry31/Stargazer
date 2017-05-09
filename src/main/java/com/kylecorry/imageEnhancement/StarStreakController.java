package com.kylecorry.imageEnhancement;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarStreakController implements Initializable {


    @FXML
    ImageView hdrImage;

    @FXML
    AnchorPane window;

    @FXML
    javafx.scene.control.Label helpText;

    StarPair firstStar, secondStar;

    javafx.scene.image.Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstStar = new StarPair(null, null);
        secondStar = new StarPair(null, null);
        image = SwingFXUtils.toFXImage(HomepageController.hdrImage, null);
        hdrImage.setFitWidth(image.getWidth());
        hdrImage.setFitHeight(image.getHeight());
        hdrImage.setImage(image);
        centerImage(hdrImage);

        hdrImage.setOnMouseClicked(event -> setStarPoint(new Point((int) event.getX(), (int) event.getY())));
    }


    private void setStarPoint(Point pointClicked) {
        System.out.println(pointClicked);
        if (firstStar.first == null) {
            firstStar.first = pointClicked;
            HomepageController.startStar1 = pointClicked;
            helpText.setText("Click on the end of the same star trail");
        } else if (firstStar.second == null) {
            firstStar.second = pointClicked;
            HomepageController.endStar1 = pointClicked;
            helpText.setText("Click on the start of another star trail");
        } else if (secondStar.first == null) {
            secondStar.first = pointClicked;
            HomepageController.startStar2 = pointClicked;
            helpText.setText("Click on the end of the same star trail");
        } else if (secondStar.second == null) {
            secondStar.second = pointClicked;
            HomepageController.endStar2 = pointClicked;
            Stage stage = (Stage) window.getScene().getWindow();
            stage.close();
        }
    }

    private static class StarPair {
        Point first, second;

        StarPair(Point first, Point second) {
            this.first = first;
            this.second = second;
        }
    }


    public static void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null && img.getWidth() != 0 && img.getHeight() != 0) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        } else {
//            imageView.setImage(new Image("images/noImage.png"));
        }
    }

}
