package com.kylecorry.imageEnhancement;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Kylec on 5/8/2017.
 */
public class HomepageController implements Initializable {

    private List<String> darkFiles, lightFiles;

    public static Point startStar1, endStar1, startStar2, endStar2;
    public static BufferedImage hdrImage;

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

    public HomepageController() {
        darkFiles = new LinkedList<>();
        lightFiles = new LinkedList<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enhanceBtn.setDisable(true);
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
        final boolean stars = alignStars.isSelected();
        Service service = new Service<BufferedImage>() {


            @Override
            protected Task<BufferedImage> createTask() {
                return new Task<BufferedImage>() {

                    private double progress;
                    private boolean black = true;

                    private boolean isProcessingBlack() {
                        return black;
                    }

                    private double getInnerProgress() {
                        return progress;
                    }

                    private void addToProgress(double amount) {
                        progress += amount;
                    }

                    @Override
                    protected BufferedImage call() throws Exception {
                        HDR hdr = new HDR();
                        updateProgress(0, 1);

                        final double totalWork = darkFiles.size() + lightFiles.size() + (darkFiles.isEmpty() ? 0 : 1);

                        hdr.imageNumber.addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                if (oldValue.intValue() != newValue.intValue())
                                    addToProgress(1 / totalWork);
                                if (isProcessingBlack()) {
                                    updateMessage("Processing black frame " + newValue.intValue() + " of " + darkFiles.size());
                                } else {
                                    updateMessage("Processing frame " + newValue.intValue() + " of " + lightFiles.size());
                                }
                                updateProgress(getInnerProgress(), 1);
                            });
                        });


                        BufferedImage dark = null;
                        if (!darkFiles.isEmpty()) {
                            updateMessage("Creating black average");
                            dark = hdr.averageImagesLazy(darkFiles);
                        }
                        black = false;
                        updateMessage("Creating average");
                        BufferedImage light = hdr.averageImagesLazy(lightFiles);
                        BufferedImage diff = light;
                        if (!darkFiles.isEmpty()) {
                            updateMessage("Subtracting images");
                            ImageSubtractor subtractor = new ImageSubtractor();
                            diff = subtractor.subtract(light, dark);
                        }
                        updateProgress(1, 1);
                        return diff;
                    }
                };
            }
        };

        Service alignmentService = new Service<BufferedImage>() {


            @Override
            protected Task<BufferedImage> createTask() {
                return new Task<BufferedImage>() {

                    private double progress;
                    private boolean black = true;

                    private boolean isProcessingBlack() {
                        return black;
                    }

                    private double getInnerProgress() {
                        return progress;
                    }

                    private void addToProgress(double amount) {
                        progress += amount;
                    }

                    @Override
                    protected BufferedImage call() throws Exception {
                        HDR hdr = new HDR();
                        StarAligner starAligner = new StarAligner();
                        updateProgress(0, 1);

                        final double totalWork = darkFiles.size() + lightFiles.size() + (darkFiles.isEmpty() ? 0 : 1);

                        hdr.imageNumber.addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                if (oldValue.intValue() != newValue.intValue())
                                    addToProgress(1 / totalWork);
                                updateMessage("Processing black frame " + newValue.intValue() + " of " + darkFiles.size());
                                updateProgress(getInnerProgress(), 1);
                            });
                        });

                        starAligner.imageNumber.addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                if (oldValue.intValue() != newValue.intValue())
                                    addToProgress(1 / totalWork);
                                updateMessage("Aligning stars " + newValue.intValue() + " of " + lightFiles.size());
                                updateProgress(getInnerProgress(), 1);

                            });
                        });

                        BufferedImage dark = null;
                        if (!darkFiles.isEmpty()) {
                            updateMessage("Creating black average");
                            dark = hdr.averageImagesLazy(darkFiles);
                        }
                        black = false;
                        updateMessage("Creating average");
                        BufferedImage light = starAligner.alignStars(lightFiles, startStar1, endStar1, startStar2, endStar2);
                        BufferedImage diff = light;
                        if (!darkFiles.isEmpty()) {
                            updateMessage("Subtracting images");
                            ImageSubtractor subtractor = new ImageSubtractor();
                            diff = subtractor.subtract(light, dark);
                        }
                        updateProgress(1, 1);
                        return diff;
                    }
                };
            }
        };

        service.setOnSucceeded((event) -> {
            progressText.textProperty().unbind();
            progressText.setText("Done");

            String outputFileName = "hdr.jpg";
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            File imageFile = fileChooser.showSaveDialog(null);
            if (imageFile != null) {
                outputFileName = imageFile.getAbsolutePath();
            } else {
                System.out.println("No Selection ");
            }

            try {
                ImageIO.write((BufferedImage) service.getValue(), "JPEG", new File(outputFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            progressText.setText("");

            if (!stars) {
                frames.setText("");
                blackFrames.setText("");
                lightFiles = new LinkedList<>();
                darkFiles = new LinkedList<>();
                enhanceBtn.setDisable(true);
            } else {
                hdrImage = (BufferedImage) service.getValue();
                displayPopup("/fxml/StarStreak.fxml", "Star Streak Identifier");
                progressText.textProperty().bind(alignmentService.messageProperty());
                progressBar.progressProperty().bind(alignmentService.progressProperty());
                alignmentService.start();
            }

        });

        alignmentService.setOnSucceeded(event -> {
            progressText.textProperty().unbind();
            progressText.setText("Done");

            String outputFileName = "hdr-stars.jpg";
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            File imageFile = fileChooser.showSaveDialog(null);
            if (imageFile != null) {
                outputFileName = imageFile.getAbsolutePath();
            } else {
                System.out.println("No Selection ");
            }

            try {
                ImageIO.write((BufferedImage) alignmentService.getValue(), "JPEG", new File(outputFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            progressText.setText("");
            frames.setText("");
            blackFrames.setText("");
            lightFiles = new LinkedList<>();
            darkFiles = new LinkedList<>();
            enhanceBtn.setDisable(true);
        });


        progressText.textProperty().bind(service.messageProperty());
        progressBar.progressProperty().bind(service.progressProperty());
        service.start();

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
