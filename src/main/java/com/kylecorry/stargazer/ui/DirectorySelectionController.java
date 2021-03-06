package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.FileSelector;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class DirectorySelectionController {

    private FileSelector selector;

    public DirectorySelectionController(FileSelector selector){
        this.selector = selector;
    }

    public File selectDirectory(){
        return selector.openDirectory();
    }

}
