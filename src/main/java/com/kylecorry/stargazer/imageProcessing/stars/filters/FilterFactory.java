package com.kylecorry.stargazer.imageProcessing.stars.filters;

import java.util.Arrays;
import java.util.List;

public class FilterFactory {
    public IFilter getFilter(String name) {
        if (name.equalsIgnoreCase("no filter")) {
            return new NoFilter();
        } else if (name.equalsIgnoreCase("background subtraction filter")) {
            return new BackgroundSubtractionFilter();
        } else {
            return new SparseLuminosityReductionFilter();
        }
    }

    public List<IFilter> getAllFilters(){
        return Arrays.asList(new SparseLuminosityReductionFilter(), new BackgroundSubtractionFilter(), new NoFilter());
    }
}
