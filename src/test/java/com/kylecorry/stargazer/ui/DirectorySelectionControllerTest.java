package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.MockFileSelector;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DirectorySelectionControllerTest {

    private DirectorySelectionController controller;

    @Before
    public void setup(){
        controller = new DirectorySelectionController(new MockFileSelector());
    }

    @Test
    public void selectDirectory() {
        File directory = controller.selectDirectory();
        assertEquals("test", directory.getName());
    }
}