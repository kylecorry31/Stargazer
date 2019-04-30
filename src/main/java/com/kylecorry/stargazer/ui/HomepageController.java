package com.kylecorry.stargazer.ui;

import com.jfoenix.controls.*;
import com.kylecorry.stargazer.imageProcessing.blendModes.Darken;
import com.kylecorry.stargazer.imageProcessing.blendModes.Lighten;
import com.kylecorry.stargazer.imageProcessing.stars.filters.SparseLuminosityReductionFilter;
import com.kylecorry.stargazer.stars.*;
import com.kylecorry.stargazer.stars.starcombinestrategies.*;
import com.kylecorry.stargazer.storage.*;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by Kylec on 5/8/2017.
 */
public class HomepageController implements Initializable {

    private List<String> darkFiles, lightFiles;

    @FXML
    JFXButton framesBtn;

    @FXML
    JFXButton blackFramesBtn;

    @FXML
    AnchorPane window;

    @FXML
    Label frames;

    @FXML
    Label blackFrames;

    @FXML
    JFXButton enhanceBtn;

    @FXML
    Label progressText;

    @FXML
    ProgressBar progressBar;

    @FXML
    JFXComboBox<String> filter;

    @FXML
    ImageView filterSettings;

    private Mat darkImage;

    private final FileManager fileManager;
    private final FileSelector fileSelector;
    private final FileNameGenerator fileNameGenerator;
    private Map<String, StarCombineStrategy> starCombineStrategies;


    public HomepageController() {
        darkFiles = new LinkedList<>();
        lightFiles = new LinkedList<>();
        fileManager = new FileManager();
        fileSelector = new DialogFileSelector();
        fileNameGenerator = new TimeFileNameGenerator();
        starCombineStrategies = new HashMap<>();
        starCombineStrategies.put("Streak stars", StarCombineFactory.streakStars());
        starCombineStrategies.put("Remove stars", StarCombineFactory.removeStars());
        starCombineStrategies.put("Reduce noise", StarCombineFactory.reduceNoise());
        starCombineStrategies.put("Align using streaks", StarCombineFactory.alignStreaks());
        starCombineStrategies.put("Align using stars", StarCombineFactory.alignStars());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enhanceBtn.setDisable(true);
        filterSettings.setVisible(false);
        filter.getItems().addAll(starCombineStrategies.keySet().stream().sorted().collect(toList()));
        filter.setValue(filter.getItems().get(0));

    }

    public void createEnhancedImage() {
        enhanceBtn.setDisable(true);
        StarCombiner combiner = new StarCombiner(starCombineStrategies.get(filter.getValue()));
        List<Image> images = lightFiles.stream().map(Image::fromFilenameReducedMemory).collect(toList());
        Service<Image> service = new StarCombinerService(images, combiner);
        bindUIToService(service);
        service.setOnSucceeded(event -> {
            Image image = service.getValue();
            saveImage(image);
            unbindUIFromServices();
            resetUI();
        });
        service.start();
    }

    private void resetUI() {
        unbindUIFromServices();
        progressBar.setProgress(0);
        progressText.setText("");
        frames.setText("No folder selected");
        blackFrames.setText("No folder selected");
        if (darkImage != null)
            darkImage.release();
        darkImage = null;
        lightFiles = new LinkedList<>();
        darkFiles = new LinkedList<>();
        enhanceBtn.setDisable(true);
    }

    private void unbindUIFromServices() {
        if (progressText.textProperty().isBound())
            progressText.textProperty().unbind();
        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
    }

    private void bindUIToService(Service service) {
        unbindUIFromServices();
        progressBar.progressProperty().bind(service.progressProperty());
        progressText.textProperty().bind(service.messageProperty());
    }

    private void saveImage(Image image) {
        SaveImageController saveImageController = new SaveImageController(fileManager, fileSelector, fileNameGenerator);
        saveImageController.saveImage(image.getMat());
    }


    public void selectBlackFrames() {
        DirectorySelectionController controller = new DirectorySelectionController(fileSelector);
        File directory = controller.selectDirectory();
        if (directory != null) {
            darkFiles = fileManager.getAllFileNamesInDirectory(directory);
            blackFrames.setText(directory.getAbsolutePath());
        } else {
            darkFiles.clear();
            blackFrames.setText("No folder selected");
        }
    }

    public void selectFrames() {
        DirectorySelectionController controller = new DirectorySelectionController(fileSelector);
        File directory = controller.selectDirectory();
        if (directory != null) {
            lightFiles = fileManager.getAllFileNamesInDirectory(directory);
            frames.setText(directory.getAbsolutePath());
            enhanceBtn.setDisable(lightFiles.isEmpty());
        } else {
            lightFiles.clear();
            enhanceBtn.setDisable(true);
            frames.setText("No folder selected");
        }
    }

    public void blackFrameHelp() {
        displayPopup("/fxml/BlackFrameHelp.fxml", "Subtracting Black Frames");
    }

    public void frameHelp() {
        displayPopup("/fxml/FrameHelp.fxml", "Averaging Frames");
    }

    public void about() {
        displayPopup("/fxml/About.fxml", "About Stargazer");
    }

    public void modifyFilterSettings() {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FilterSettings.fxml"));
            AnchorPane root = loader.load();
            FilterSettingsController controller = loader.getController();
//            controller.setFilter(filter.getValue());
            if (lightFiles != null && !lightFiles.isEmpty()) {
                controller.setImage(lightFiles.get(0));
            }
            controller.setStage(stage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/styles.css");
            stage.setScene(scene);
//            stage.setTitle("Adjust " + filter.getValue().getName() + " Settings");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayPopup(String fxml, String title) {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/styles.css");
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
