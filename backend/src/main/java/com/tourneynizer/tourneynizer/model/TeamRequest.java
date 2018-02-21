package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;

public class TeamRequest {
    private long id, teamId, userId, requesterId;
    private boolean accepted;
    private Timestamp timeRequested;

    public TeamRequest(long id, long teamId, long userId, long requesterId, boolean accepted, Timestamp timeRequested) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.requesterId = requesterId;
        this.accepted = accepted;
        this.timeRequested = timeRequested;
    }

    public long getId() {
        return id;
    }

    public long getTeamId() {
        return teamId;
    }

    public long getUserId() {
        return userId;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Timestamp getTimeRequested() {
        return timeRequested;
    }
}
