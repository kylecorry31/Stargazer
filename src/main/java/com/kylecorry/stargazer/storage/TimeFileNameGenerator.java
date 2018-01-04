package com.kylecorry.stargazer.storage;

public class TimeFileNameGenerator implements FileNameGenerator{
    @Override
    public String generateFileName() {
        return String.valueOf(System.currentTimeMillis());
    }
}
