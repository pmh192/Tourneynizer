package com.tourneynizer.tourneynizer.model;

import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanl on 2/28/2018.
 */

public class TeamRequest {

    private long id;
    private long teamID;
    private long userID;
    private long requesterID;
    private Boolean accepted;
    private Time timeRequested;

    public TeamRequest(long id, long teamID, long userID, long requesterID, Boolean accepted, @NonNull Time timeRequested) {
        this.id = id;
        this.teamID = teamID;
        this.userID = userID;
        this.requesterID = requesterID;
        this.accepted = accepted;
        this.timeRequested = timeRequested;
    }

    public long getID() {
        return id;
    }

    public long getTeamID() {
        return teamID;
    }

    public long getUserID() {
        return userID;
    }

    public long getRequesterID() {
        return requesterID;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public @NonNull Time getTimeRequested() {
        return timeRequested;
    }
}
