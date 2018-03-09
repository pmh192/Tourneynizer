package com.tourneynizer.tourneynizer.model;

public enum TournamentType {
    VOLLEYBALL_POOLED,
    VOLLEYBALL_BRACKET;

    @Override
    public String toString() {
        String name = super.toString();
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        String result = words[0].substring(0, 1) + words[0].substring(1).toLowerCase();
        for (int i = 1; i < words.length; i++) {
            result += " " + words[i].substring(0, 1) + words[i].substring(1).toLowerCase();
        }
        return result;
    }
}
