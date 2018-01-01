package com.kylecorry.stargazer.imageProcessing.stars.filters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterSettings {

    private Map<String, FilterSetting> settings;


    public FilterSettings() {
        this.settings = new HashMap<>();
    }

    public List<String> possibleSettings(){
        List<String> settingsList = new LinkedList<>();
        settingsList.addAll(settings.keySet());
        settingsList.sort(String::compareToIgnoreCase);
        return settingsList;
    }

    public void put(String key, FilterSetting setting){
        settings.put(key, setting);
    }

    public void put(FilterSetting setting){
        settings.put(setting.getName(), setting);
    }

    public FilterSetting get(String key){
        return settings.get(key);
    }
}
