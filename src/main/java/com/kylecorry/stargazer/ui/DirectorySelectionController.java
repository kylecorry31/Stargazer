package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.FileManager;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class DirectorySelectionController {

    public File selectDirectory(FileManager fileManager){
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(null);
        if (directory != null) {
            return directory;
        } else {
            return null;
        }
    }

}
