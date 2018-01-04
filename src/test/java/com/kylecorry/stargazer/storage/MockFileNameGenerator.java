package com.kylecorry.stargazer.storage;

public class MockFileNameGenerator implements FileNameGenerator {
    @Override
    public String generateFileName() {
        return "test";
    }
}
