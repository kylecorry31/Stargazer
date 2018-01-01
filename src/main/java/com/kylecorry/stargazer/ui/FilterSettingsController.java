package com.kylecorry.stargazer.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXSlider;
import com.kylecorry.stargazer.imageProcessing.stars.filters.FilterSetting;
import com.kylecorry.stargazer.imageProcessing.stars.filters.IFilter;
import com.kylecorry.stargazer.imageProcessing.stars.filters.StarFilter;
import com.kylecorry.stargazer.storage.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FilterSettingsController implements Initializable {

    private boolean initiliazed = false;

    private IFilter filter;

    private String image;

    @FXML
    private Label title;

    @FXML
    private VBox settingsPane;

    @FXML
    private JFXScrollPane pane;

    @FXML
    private AnchorPane window;

    @FXML
    private JFXButton previewBtn;

    public void setFilter(IFilter filter) {
        this.filter = filter;
        if (initiliazed) {
            showFilterSettings(filter);
        }
    }

    public void setImage(String image){
        this.image = image;
        if(initiliazed) {
            previewBtn.setDisable(false);
        }
    }

    private void showFilterSettings(IFilter filter) {
        JFXScrollPane.smoothScrolling((ScrollPane) pane.getChildren().get(0));
        title.setText(filter.getName());
        List<String> settings = filter.getSettings().possibleSettings();
        for (String setting : settings) {
            FilterSetting currentSetting = filter.getSettings().get(setting);
            Label sliderLabel = new Label();
            sliderLabel.setText(currentSetting.getName());
            sliderLabel.getStyleClass().add("header");
            settingsPane.getChildren().add(sliderLabel);
            Label sliderLabelDesc = new Label();
            sliderLabelDesc.setText(currentSetting.getDescription());
            settingsPane.getChildren().add(sliderLabelDesc);
            JFXSlider slider = new JFXSlider();
            slider.setMin(currentSetting.getMinValue());
            slider.setMax(currentSetting.getMaxValue());
            slider.setValue(currentSetting.getValue());
            slider.setMaxWidth(608);
            slider.getStyleClass().add("custom-jfx-slider");
            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                currentSetting.setValue(newValue.doubleValue());
            });
            settingsPane.getChildren().add(slider);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initiliazed = true;
        if (filter != null) {
            showFilterSettings(filter);
        }
        if(image == null){
            previewBtn.setDisable(true);
        } else {
            previewBtn.setDisable(false);
        }
    }

    public void previewFilter(){
        if(image != null){
            FileManager manager = new FileManager();
            Mat img = manager.loadImage(image);
            Mat filtered = filter.filterStars(img, new Mat());
            displayPreview(img, filtered);
        }
    }

    private void displayPreview(Mat image, Mat filtered) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PreviewFilter.fxml"));
            AnchorPane root = loader.load();
            PreviewFilterController controller = loader.getController();
            controller.setImage(image, filtered);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/styles.css");
            stage.setScene(scene);
            stage.setTitle("Previewing " + filter.getName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
