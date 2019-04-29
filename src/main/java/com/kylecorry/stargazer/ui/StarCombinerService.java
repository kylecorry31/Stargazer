package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.stars.Image;
import com.kylecorry.stargazer.stars.StarCombiner;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class StarCombinerService extends Service<Image> {


    private List<Image> images;
    private StarCombiner combiner;

    public StarCombinerService(List<Image> images, StarCombiner combiner) {
        this.images = images;
        this.combiner = combiner;
    }

    @Override
    protected Task<Image> createTask() {
        return new Task<Image>() {
            @Override
            protected Image call() {
                return combiner.combine(images, progress -> Platform.runLater(() -> {
                    updateProgress(progress, 1.0);
                    updateMessage(String.format("Processing images: %.0f%%", progress * 100));
                }));
            }
        };
    }
}
