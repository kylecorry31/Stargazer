package com.kylecorry.imageEnhancement.ui;

import com.kylecorry.imageEnhancement.imageProcessing.*;
import com.kylecorry.imageEnhancement.imageProcessing.stars.AutoAlign;
import com.kylecorry.imageEnhancement.imageProcessing.stars.ManualAlign;
import com.kylecorry.imageEnhancement.imageProcessing.stars.StarStreak;
import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

/**
 * Created by Kylec on 5/8/2017.
 */
public class HomepageController implements Initializable {

    private List<String> darkFiles, lightFiles;

    public static Point startStar1, endStar1, startStar2, endStar2;
    public static Mat hdrImage;

    @FXML
    Button framesBtn;

    @FXML
    Button blackFramesBtn;

    @FXML
    AnchorPane window;

    @FXML
    Label frames;

    @FXML
    Label blackFrames;

    @FXML
    Button enhanceBtn;

    @FXML
    Label progressText;

    @FXML
    ProgressBar progressBar;

    @FXML
    CheckBox alignStars;

    @FXML
    CheckBox autoMergeStars;

    @FXML
    RadioButton autoAlign;

    @FXML
    RadioButton manualAlign;

    @FXML
    Label techniqueLbl;

    Mat darkImage;

    private ImageProcessor imageProcessor;

    private FileManager fileManager;
    private Service<Mat> blackImageService, hdrService, subtractionService, starAlignmentService;


    public HomepageController() {
        darkFiles = new LinkedList<>();
        lightFiles = new LinkedList<>();
        fileManager = new FileManager();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enhanceBtn.setDisable(true);
        autoMergeStars.setDisable(true);
        alignStars.selectedProperty().addListener((observable, oldValue, newValue) -> {
            autoMergeStars.setDisable(!newValue);
            autoAlign.setDisable(!newValue);
            manualAlign.setDisable(!newValue);
            techniqueLbl.setDisable(!newValue);
        });
    }

    public void selectFrames() {
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(null);
        if (directory != null) {
            lightFiles = getAllFileNames(directory.getAbsolutePath());
            frames.setText(directory.getAbsolutePath());
            if (!lightFiles.isEmpty()) {
                enhanceBtn.setDisable(false);
            }
        } else {
            System.out.println("No Selection ");
        }
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
        frames.setText("");
        blackFrames.setText("");
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
        String outputFileName = System.currentTimeMillis() + ".jpg";
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File imageFile = fileChooser.showSaveDialog(null);
        if (imageFile != null) {
            outputFileName = imageFile.getAbsolutePath();
        } else {
            System.out.println("No Selection ");
        }

        fileManager.saveImage(image, outputFileName);
    }

    private void locateStars(Mat blackImage, Mat hdrImage) {
        if (autoAlign.isSelected()) {
            starAlignmentService = new StarAlignmentService(imageProcessor, new AutoAlign(fileManager, lightFiles, blackImage), lightFiles.size());
        } else {
            HomepageController.hdrImage = hdrImage;
            displayPopup("/fxml/StarStreak.fxml", "Star Streak Identifier");
            starAlignmentService = new StarAlignmentService(imageProcessor, new ManualAlign(fileManager, lightFiles, new StarStreak(startStar1, startStar2), new StarStreak(endStar1, endStar2)), lightFiles.size());
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
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(null);
        if (directory != null) {
            darkFiles = getAllFileNames(directory.getAbsolutePath());
            blackFrames.setText(directory.getAbsolutePath());
            if (!lightFiles.isEmpty()) {
                enhanceBtn.setDisable(false);
            }
        } else {
            System.out.println("No Selection ");
        }
    }

    private List<String> getAllFileNames(String directory) {
        List<String> files = new LinkedList<>();
        File folder = new File(directory);

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            if (listOfFiles[i].isFile()) {
                files.add(directory + "/" + listOfFiles[i].getName());
            }
        }
        return files;
    }


    public void blackFrameHelp() {
        displayPopup("/fxml/BlackFrameHelp.fxml", "Subtracting Black Frames");
    }

    public void frameHelp() {
        displayPopup("/fxml/FrameHelp.fxml", "Averaging Frames");
    }

    public void displayPopup(String fxml, String title) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.showAndWait();
            stage = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openResearchBlog() {
        final String article = "https://research.googleblog.com/2017/04/experimental-nighttime-photography-with.html";
        try {
            Desktop.getDesktop().browse(URI.create(article));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
