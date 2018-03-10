package com.tourneynizer.tourneynizer.model;

public class UserInfo {

    public int wins, losses, tournaments, matches;

    public UserInfo(int wins, int losses, int tournaments, int matches) {
        this.wins = wins;
        this.losses = losses;
        this.tournaments = tournaments;
        this.matches = matches;
    }
}
