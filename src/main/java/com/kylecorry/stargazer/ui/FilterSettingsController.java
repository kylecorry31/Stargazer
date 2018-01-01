package com.kylecorry.stargazer.ui;

import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXSlider;
import com.kylecorry.stargazer.imageProcessing.stars.filters.FilterSetting;
import com.kylecorry.stargazer.imageProcessing.stars.filters.IFilter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FilterSettingsController implements Initializable {

    private boolean initiliazed = false;

    private IFilter filter;

    @FXML
    private Label title;

    @FXML
    private VBox settingsPane;

    @FXML
    private JFXScrollPane pane;

    public void setFilter(IFilter filter) {
        this.filter = filter;
        if (initiliazed) {
            showFilterSettings(filter);
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
    }
}
