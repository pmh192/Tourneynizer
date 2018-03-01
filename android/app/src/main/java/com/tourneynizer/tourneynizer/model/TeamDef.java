package com.tourneynizer.tourneynizer.model;

import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanwiener on 2/28/18.
 */

public class TeamDef {

    private String name;
    private long tournamentID;

    public TeamDef() {}

    public TeamDef(String name, long tournamentID) {
        this.name = name;
        this.tournamentID = tournamentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(long tournamentID) {
        this.tournamentID = tournamentID;
    }
}
