package com.kylecorry.stargazer.ui;

import com.jfoenix.controls.*;
import com.kylecorry.stargazer.imageProcessing.*;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.AutoAlign;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ManualAlign;
import com.kylecorry.stargazer.imageProcessing.stars.StarStreak;
import com.kylecorry.stargazer.imageProcessing.stars.filters.*;
import com.kylecorry.stargazer.storage.*;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.opencv.core.*;
import org.opencv.core.Point;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kylec on 5/8/2017.
 */
public class HomepageController implements Initializable {

    private List<String> darkFiles, lightFiles;

    public static Point startStar1, endStar1, startStar2, endStar2;
    public static Mat hdrImage;

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
    JFXProgressBar progressBar;

    @FXML
    JFXToggleButton alignStars;

    @FXML
    CheckBox autoMergeStars;

    @FXML
    JFXRadioButton autoAlign;

    @FXML
    JFXRadioButton manualAlign;

    @FXML
    Label techniqueLbl;

    @FXML
    JFXComboBox<IFilter> filter;

    @FXML
    ImageView filterSettings;

    private Mat darkImage;

    private ImageProcessor imageProcessor;

    private final FileManager fileManager;
    private final FileSelector fileSelector;
    private final FileNameGenerator fileNameGenerator;
    private Service<Mat> blackImageService, hdrService, subtractionService, starAlignmentService;


    public HomepageController() {
        darkFiles = new LinkedList<>();
        lightFiles = new LinkedList<>();
        fileManager = new FileManager();
        fileSelector = new DialogFileSelector();
        fileNameGenerator = new TimeFileNameGenerator();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!SplashScreenController.wasSplashScreenLoaded) {
            loadSplashScreen();
        }
        enhanceBtn.setDisable(true);
        autoMergeStars.setDisable(true);
        filter.setDisable(true);
        filterSettings.setVisible(false);
        alignStars.selectedProperty().addListener((observable, oldValue, newValue) -> {
            autoMergeStars.setDisable(!newValue);
            autoAlign.setDisable(!newValue);
            manualAlign.setDisable(!newValue);
            techniqueLbl.setDisable(!newValue);
            if(autoAlign.isSelected()){
                filter.setDisable(!newValue);
                filterSettings.setVisible(newValue);
            }
        });
        autoAlign.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filter.setDisable(!newValue);
            filterSettings.setVisible(newValue);
        });

        FilterFactory filterFactory = new FilterFactory();

        List<IFilter> filters = filterFactory.getAllFilters();
        filter.getItems().addAll(filters);
        filter.setValue(filters.get(0));

        filter.setConverter(new StringConverter<IFilter>() {
            @Override
            public String toString(IFilter iFilter) {
                return iFilter.getName();
            }

            @Override
            public IFilter fromString(String s) {
                return filterFactory.getFilter(s);
            }
        });

    }

    private void loadSplashScreen(){
        SplashScreenController splashScreenController = new SplashScreenController();
        splashScreenController.init(window);
    }

    public void createEnhancedImage() {
        enhanceBtn.setDisable(true);
        imageProcessor = new ImageProcessor(fileManager);

        blackImageService = null;
        hdrService = new HDRService(imageProcessor, lightFiles);
        hdrService.setOnSucceeded(event -> {
            unbindUIFromServices();
            if (blackImageService != null) {
                subtractionService = new SubtractionService(imageProcessor, blackImageService.getValue(), hdrService.getValue());
                bindUIToService(subtractionService);
                subtractionService.start();
                subtractionService.setOnSucceeded(event1 -> {
                    unbindUIFromServices();
                    saveImage(subtractionService.getValue());
                    if (alignStars.isSelected()) {
                        Mat black = new Mat();
                        blackImageService.getValue().convertTo(black, CvType.CV_8U);
                        locateStars(black, hdrService.getValue());
                    } else
                        resetUI();
                });
            } else {
                saveImage(hdrService.getValue());
                if (alignStars.isSelected()) {
                    Mat hdr = hdrService.getValue();
                    locateStars(Mat.zeros(hdr.size(), CvType.CV_8U), hdr);
                } else
                    resetUI();
            }
        });

        // Process black frames and/or start hdr service
        if (!darkFiles.isEmpty()) {
            blackImageService = new BlackImageService(new ImageProcessor(fileManager), darkFiles);
            bindUIToService(blackImageService);
            blackImageService.start();
            blackImageService.setOnSucceeded(event -> {
                bindUIToService(hdrService);
                hdrService.start();
            });
        } else {
            bindUIToService(hdrService);
            hdrService.start();
        }

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
        if (hdrImage != null)
            hdrImage.release();
        alignStars.setSelected(false);
        autoMergeStars.setSelected(false);
        System.gc();
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

    private void saveImage(Mat image) {
        SaveImageController saveImageController = new SaveImageController(fileManager, fileSelector, fileNameGenerator);
        saveImageController.saveImage(image);
    }

    private void locateStars(Mat blackImage, Mat hdrImage) {
        if (autoAlign.isSelected()) {
            starAlignmentService = new StarAlignmentService(imageProcessor, new AutoAlign(fileManager, lightFiles, blackImage, new StarFilter(filter.getValue())), lightFiles.size());
        } else {
            HomepageController.hdrImage = hdrImage;
            displayPopup("/fxml/StarStreak.fxml", "Star Streak Identifier");
            starAlignmentService = new StarAlignmentService(imageProcessor, new ManualAlign(fileManager, lightFiles, new StarStreak(startStar1, endStar1), new StarStreak(startStar2, endStar2)), lightFiles.size());
        }

        bindUIToService(starAlignmentService);
        starAlignmentService.setOnSucceeded(event -> {
            unbindUIFromServices();
            if (blackImageService != null) {
                subtractionService = new SubtractionService(imageProcessor, blackImageService.getValue(), starAlignmentService.getValue());
                bindUIToService(subtractionService);
                subtractionService.setOnSucceeded(event1 -> {
                    unbindUIFromServices();
                    saveImage(subtractionService.getValue());
                    if (autoMergeStars.isSelected()) {
                        // TODO: auto merge
                    } else {
                        resetUI();
                    }
                });
                subtractionService.start();
            } else {
                saveImage(starAlignmentService.getValue());
                if (autoMergeStars.isSelected()) {
                    // TODO: auto merge`
                } else {
                    resetUI();
                }
            }
        });
        starAlignmentService.start();

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
            controller.setFilter(filter.getValue());
            if(lightFiles != null && !lightFiles.isEmpty()){
                controller.setImage(lightFiles.get(0));
            }
            controller.setStage(stage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/styles.css");
            stage.setScene(scene);
            stage.setTitle("Adjust " + filter.getValue().getName() + " Settings");
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
