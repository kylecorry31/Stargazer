package com.kylecorry.stargazer.storage;

import java.io.File;
import java.io.IOException;

public class MockFileSelector implements FileSelector{
    @Override
    public File openDirectory() {
        return new File("test");
    }

    @Override
    public File openFile() {
        return new File("test.jpg");
    }

    @Override
    public File openFile(String fileTypePrompt, String... fileExtensions) {
        return new File("test." + fileExtensions[0]);
    }

    @Override
    public File saveFile() {
        File f = new File("test.jpg");
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    @Override
    public File saveFile(String fileTypePrompt, String... fileExtensions) {
        File f = new File("test.jpg");
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
}
