package com.kylecorry.imageEnhancement.ui;

import com.kylecorry.imageEnhancement.imageProcessing.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    javafx.scene.control.ScrollPane scrollPane;

    @FXML
    javafx.scene.control.Label helpText;

    StarPair firstStar, secondStar;

    javafx.scene.image.Image image;

    BufferedImage drawImage;


    double initX;
    double initY;
    int counter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstStar = null;
        secondStar = null;
        image = SwingFXUtils.toFXImage(HomepageController.hdrImage, null);
        drawImage = ImageUtils.copyImage(HomepageController.hdrImage, HomepageController.hdrImage.getType());
        hdrImage.setFitWidth(image.getWidth());
        hdrImage.setFitHeight(image.getHeight());
        hdrImage.setImage(image);
        centerImage(hdrImage);
        counter = 0;

        final double maxX = image.getWidth();
        final double maxY = image.getHeight();

//        hdrImage.setOnMousePressed(me -> {
//            initX = me.getX();
//            initY = me.getY();
//            me.consume();
//        });


        helpText.setText("Drag across a star streak, then press done");
        hdrImage.setOnMousePressed(me -> {
            drawImage = ImageUtils.copyImage(HomepageController.hdrImage, HomepageController.hdrImage.getType());
            hdrImage.setImage(SwingFXUtils.toFXImage(drawImage, null));
            initX = me.getX();
            initY = me.getY();
            System.out.println(initX + " " + initY);
            me.consume();
        });

        hdrImage.setOnMouseReleased(me -> {
            if (me.getX() < maxX && me.getY() < maxY) {
                Graphics2D graphics2D = drawImage.createGraphics();
                graphics2D.setColor(java.awt.Color.GREEN);
                graphics2D.drawLine((int) initX, (int) initY, (int) me.getX(), (int) me.getY());
                graphics2D.dispose();
                hdrImage.setImage(SwingFXUtils.toFXImage(drawImage, null));
                switch (counter) {
                    case 0:
                        firstStar = new StarPair(new Point((int) initX, (int) initY), new Point((int) me.getX(), (int) me.getY()));
                        break;
                    case 1:
                        secondStar = new StarPair(new Point((int) initX, (int) initY), new Point((int) me.getX(), (int) me.getY()));
                        break;
                }
            }

            initX = me.getX() > maxX ? maxX : me.getX();
            initY = me.getY() > maxY ? maxY : me.getY();
        });

//        hdrImage.setOnMouseClicked(event -> setStarPoint(new Point((int) event.getX(), (int) event.getY())));
    }

    public void doneBtnClicked() {
        switch (counter) {
            case 0:
                if (firstStar != null) {
                    HomepageController.startStar1 = firstStar.first;
                    HomepageController.endStar1 = firstStar.second;
                    counter++;
                    helpText.setText("Drag across a second star streak, then press done");
                }
                break;
            case 1:
                if (secondStar != null) {
                    HomepageController.startStar2 = secondStar.first;
                    HomepageController.endStar2 = secondStar.second;
                    counter++;
                    Stage stage = (Stage) window.getScene().getWindow();
                    stage.close();
                }
                break;
            default:
                Stage stage = (Stage) window.getScene().getWindow();
                stage.close();
        }
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
