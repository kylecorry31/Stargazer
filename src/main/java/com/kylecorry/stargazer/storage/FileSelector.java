package com.kylecorry.stargazer.storage;

import java.io.File;

public interface FileSelector {

    File openDirectory();

    File openFile();

    File openFile(String fileTypePrompt, String... fileExtensions);

    File saveFile();

    File saveFile(String fileTypePrompt, String... fileExtensions);

}
