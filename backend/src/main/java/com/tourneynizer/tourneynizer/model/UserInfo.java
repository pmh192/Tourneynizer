package com.tourneynizer.tourneynizer.model;

public class UserInfo {

    public int wins, losses, tournaments, matches;

    public UserInfo(int wins, int losses, int tournaments) {
        this.wins = wins;
        this.losses = losses;
        this.tournaments = tournaments;
        this.matches = wins + losses;
    }

    public UserInfo() {
        this(0, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (wins != userInfo.wins) return false;
        if (losses != userInfo.losses) return false;
        if (tournaments != userInfo.tournaments) return false;
        return matches == userInfo.matches;
    }
}
