package com.tourneynizer.tourneynizer.model;

import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanwiener on 2/28/18.
 */

public class Team {

    private long id;
    private String name;
    private Time timeCreated;
    private long creatorID;
    private long tournamentID;
    private boolean checkedIn;
    private boolean approved;

    public Team(long id, @NonNull String name, @NonNull Time timeCreated, long creatorID, long tournamentID) {
        this(id, name, timeCreated, creatorID, tournamentID, false, false);
    }

    public Team(long id, @NonNull String name, @NonNull Time timeCreated, long creatorID, long tournamentID, boolean checkedIn, boolean approved) {
        this.id = id;
        this.name = name;
        this.timeCreated = timeCreated;
        this.creatorID = creatorID;
        this.tournamentID = tournamentID;
        this.checkedIn = false;
        this.approved = false;
    }

    public long getId() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull Time getTimeCreated() {
        return timeCreated;
    }

    public long getCreatorID() {
        return creatorID;
    }

    public long getTournamentID() {
        return tournamentID;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public boolean isApproved() {
        return approved;
    }
}
