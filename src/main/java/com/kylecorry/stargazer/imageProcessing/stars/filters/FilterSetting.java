package com.kylecorry.stargazer.imageProcessing.stars.filters;

public class FilterSetting {
    private String name;
    private double currentValue;
    private double minValue;
    private double maxValue;
    private String description;

    public FilterSetting(String name, double defaultValue, double minValue, double maxValue, String description) {
        this.name = name;
        currentValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.description = description;
    }

    public FilterSetting(String name, double defaultValue, double minValue, double maxValue) {
        this(name, defaultValue, minValue, maxValue, "");
    }

    public void setValue(double value) {
        currentValue = Math.max(Math.min(value, maxValue), minValue);
    }

    public double getValue() {
        return currentValue;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
