package com.kylecorry.stargazer;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommonsIOTest {

    @Test
    public void testFileExtension() throws Exception {
        assertEquals("jpg", FilenameUtils.getExtension("test.jpg"));
        assertEquals("jpeg", FilenameUtils.getExtension("test.jpeg"));
        assertEquals("txt", FilenameUtils.getExtension("random/folder/test.txt"));
        assertEquals("", FilenameUtils.getExtension("test"));
        assertEquals("txt", FilenameUtils.getExtension("\\windows\\folder\\test.txt"));
        assertEquals("jpg", FilenameUtils.getExtension("test.d/test.jpg"));
    }

}
