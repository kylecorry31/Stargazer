package com.kylecorry.stargazer.imageProcessing.stars.filters;

import java.util.Arrays;
import java.util.List;

public class FilterFactory {
    public IFilter getFilter(String name) {
        if (name.equalsIgnoreCase("no filter")) {
            return new NoFilter();
        } else if (name.equalsIgnoreCase("background subtraction filter")) {
            return new BackgroundSubtractionFilter();
        } else if (name.equalsIgnoreCase("Binary Sparse Luminosity Reduction Filter")) {
            return new BinarySparseLuminosityReductionFilter();
        } else if(name.equalsIgnoreCase("Brightness Filter")) {
            return new BrightnessFilter();
        } else {
            return new SparseLuminosityReductionFilter();
        }
    }

    public List<IFilter> getAllFilters(){
        return Arrays.asList(new SparseLuminosityReductionFilter(), new BinarySparseLuminosityReductionFilter(), new BackgroundSubtractionFilter(), new BrightnessFilter(), new NoFilter());
    }
}
