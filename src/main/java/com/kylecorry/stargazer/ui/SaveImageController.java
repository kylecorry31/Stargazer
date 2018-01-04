package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.FileNameGenerator;
import com.kylecorry.stargazer.storage.FileSelector;
import com.kylecorry.stargazer.storage.IFileManager;
import org.opencv.core.Mat;

import java.io.File;

public class SaveImageController {

    private FileSelector selector;
    private IFileManager fileManager;
    private FileNameGenerator fileNameGenerator;

    public SaveImageController(IFileManager fileManager, FileSelector selector, FileNameGenerator fileNameGenerator) {
        this.selector = selector;
        this.fileManager = fileManager;
        this.fileNameGenerator = fileNameGenerator;
    }


    public boolean saveImage(Mat image) {
        String outputFileName = selectFileName();
        if(outputFileName == null)
            return false;
        return fileManager.saveImage(image, outputFileName);
    }

    private String selectFileName() {
        File imageFile = selector.saveFile("Image files", "*.jpg", "*.jpeg");
        return imageFile != null ? imageFile.getAbsolutePath() : generateFileName();
    }

    private String generateFileName() {
        String fileName = fileNameGenerator.generateFileName();
        if(fileName == null)
            return null;
        return fileNameGenerator.generateFileName() + ".jpg";
    }


}
