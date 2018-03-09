package com.tourneynizer.tourneynizer.model;

public enum TournamentType {
    VOLLEYBALL_POOLED,
    VOLLEYBALL_BRACKET;

    @Override
    public String toString() {
        String s = super.toString();
        return makeUserFriendly(s);
    }

    private static String makeUserFriendly(String s) {
        s = s.replace("_", " ");
        String[] words = s.split(" ");
        String result = words[0].substring(0, 1) + words[0].substring(1).toLowerCase();
        for (int i = 1; i < words.length; i++) {
            result += " " + words[i].substring(0, 1) + words[i].substring(1).toLowerCase();
        }
        return result;
    }

    private static String revertUserFriendly(String s) {
        s = s.replace(" ", "_");
        s.toUpperCase();
        return s;
    }

    public static TournamentType userFriendlyValueOf(String name) {
        String realName = revertUserFriendly(name);
        return valueOf(TournamentType.class, realName);
    }
}
