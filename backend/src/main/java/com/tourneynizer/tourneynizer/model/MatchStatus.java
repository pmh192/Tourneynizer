package com.tourneynizer.tourneynizer.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum MatchStatus {
    CREATED, STARTED, COMPLETED;

    private static Map<MatchStatus, String> createMap() {
        Map<MatchStatus, String> myMap = new HashMap<>();
        myMap.put(CREATED, "Not Started");
        myMap.put(STARTED, "In Progress");
        myMap.put(COMPLETED, "Finished");
        return myMap;
    }
    private static final Map<MatchStatus, String> map = Collections.unmodifiableMap(createMap());
    public static Map<MatchStatus, String> getStringMap() {
        return map;
    }
}
