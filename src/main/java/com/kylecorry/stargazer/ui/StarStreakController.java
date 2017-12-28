package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.imageProcessing.ImageUtils;
import com.kylecorry.stargazer.imageProcessing.stars.StarStreak;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

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

    private StarStreak firstStar, secondStar;

    private Mat drawImage;


    private double initX;
    private double initY;
    private int counter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstStar = null;
        secondStar = null;
        Image image = ImageUtils.toImage(HomepageController.hdrImage);
        drawImage = HomepageController.hdrImage.clone();
        hdrImage.setFitWidth(image.getWidth());
        hdrImage.setFitHeight(image.getHeight());
        hdrImage.setImage(image);
        centerImage(hdrImage);
        counter = 0;

        final double maxX = image.getWidth();
        final double maxY = image.getHeight();


        helpText.setText("Drag across a star trail");
        hdrImage.setOnMousePressed(me -> {
            drawImage.release();
            drawImage = HomepageController.hdrImage.clone();
            hdrImage.setImage(ImageUtils.toImage(drawImage));
            initX = me.getX();
            initY = me.getY();
            System.out.println(initX + " " + initY);
            me.consume();
        });

        hdrImage.setOnMouseReleased(me -> {
            if (me.getX() < maxX && me.getY() < maxY) {

                Imgproc.line(drawImage, new org.opencv.core.Point(initX, initY), new org.opencv.core.Point(me.getX(), me.getY()), new Scalar(0, 255, 0));

                hdrImage.setImage(ImageUtils.toImage(drawImage));
                switch (counter) {
                    case 0:
                        firstStar = new StarStreak(new Point(initX, initY), new Point(me.getX(), me.getY()));
                        break;
                    case 1:
                        secondStar = new StarStreak(new Point(initX, initY), new Point(me.getX(), me.getY()));
                        break;
                }
            }

            initX = me.getX() > maxX ? maxX : me.getX();
            initY = me.getY() > maxY ? maxY : me.getY();
        });

    }

    public void doneBtnClicked() {
        switch (counter) {
            case 0:
                if (firstStar != null) {
                    HomepageController.startStar1 = firstStar.getStart();
                    HomepageController.endStar1 = firstStar.getEnd();
                    counter++;
                    helpText.setText("Drag across another star trail");
                }
                break;
            case 1:
                if (secondStar != null) {
                    HomepageController.startStar2 = secondStar.getStart();
                    HomepageController.endStar2 = secondStar.getEnd();
                    counter++;
                    Stage stage = (Stage) window.getScene().getWindow();
                    stage.close();
                }
                break;
            default:
                drawImage.release();
                hdrImage.setImage(null);
                Stage stage = (Stage) window.getScene().getWindow();
                stage.close();
        }
    }


    private static void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null && img.getWidth() != 0 && img.getHeight() != 0) {
            double w;
            double h;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

}
