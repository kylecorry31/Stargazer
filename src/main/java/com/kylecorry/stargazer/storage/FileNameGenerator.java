package com.kylecorry.stargazer.storage;

public interface FileNameGenerator {

    /**
     * Generate a file name (without an extension)
     * @return The file name.
     */
    String generateFileName();
}
