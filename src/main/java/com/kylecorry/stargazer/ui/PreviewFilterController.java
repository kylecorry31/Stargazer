package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.imageProcessing.ImageUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

import java.net.URL;
import java.util.ResourceBundle;

public class PreviewFilterController implements Initializable{

    @FXML
    private ImageView previewImg;

    @FXML
    private ImageView previewFilter;

    private Image image;
    private Image filteredImage;

    private boolean initialized = false;

    public void setImage(Mat image, Mat filtered){
        this.image = ImageUtils.toImage(image);
        this.filteredImage = ImageUtils.toImage(filtered);
        image.release();
        filtered.release();
        if(initialized){
            setPreview(this.image, this.filteredImage);
        }
    }

    private void setPreview(Image image, Image filteredImage){
        previewImg.setImage(image);
        previewFilter.setImage(filteredImage);
    }

    public void showNormalImage(){
        previewFilter.setVisible(false);
        previewImg.setVisible(true);
    }

    public void showFilteredImage(){
        previewImg.setVisible(false);
        previewFilter.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialized = true;
        if(image != null && filteredImage != null){
            setPreview(image, filteredImage);
        }
    }
}
