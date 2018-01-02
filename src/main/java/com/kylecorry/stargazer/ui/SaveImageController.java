package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.FileManager;
import javafx.stage.FileChooser;
import org.opencv.core.Mat;

import java.io.File;

public class SaveImageController {

    private String generateFileName(){
        return System.currentTimeMillis() + ".jpg";
    }

    private String selectFileName(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File imageFile = fileChooser.showSaveDialog(null);
        return imageFile != null ? imageFile.getAbsolutePath() : generateFileName();
    }

    public void saveImage(FileManager fileManager, Mat image){
        String outputFileName = selectFileName();
        fileManager.saveImage(image, outputFileName);
    }

}
