package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;

public class TournamentRequest {
    private long id, teamId, tournamentId, requesterId;
    private boolean accepted;
    private Timestamp timeRequested;

    public TournamentRequest(long id, long teamId, long tournamentId, long requesterId, boolean accepted, Timestamp timeRequested) {
        this.id = id;
        this.teamId = teamId;
        this.tournamentId = tournamentId;
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

    public long getTournamentId() {
        return tournamentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TournamentRequest that = (TournamentRequest) o;

        if (id != that.id) return false;
        if (teamId != that.teamId) return false;
        if (tournamentId != that.tournamentId) return false;
        if (requesterId != that.requesterId) return false;
        if (accepted != that.accepted) return false;
        return timeRequested.equals(that.timeRequested);
    }
}
