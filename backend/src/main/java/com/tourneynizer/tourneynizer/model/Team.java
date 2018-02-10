package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;

public class Team {
    private Long id;
    private String name;
    private Timestamp timeCreated;
    private long creator_id, tournament_id;
    private boolean checkedIn;

    public Team(String name, long creator_id, long tournament_id) {
        setName(name);
        setCreator_id(creator_id);
        setTournament_id(tournament_id);
    }

    public Team(long id, String name, Timestamp timeCreated, long creator_id, long tournament_id, boolean checkedIn) {
        this(name, creator_id, tournament_id);
        persist(id, timeCreated);
        setCheckedIn(checkedIn);
    }

    public void persist(long id, Timestamp timeCreated) {
        if (timeCreated == null) { throw new IllegalArgumentException("timeCreated cannot be null"); }
        this.id = id;
        this.timeCreated = timeCreated;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    private void setTournament_id(long tournament_id) {
        this.tournament_id = tournament_id;
    }

    public boolean isPersisted() {
        return id != null;
    }

    public long getCreatorId() {
        return creator_id;
    }

    public String getName() {
        return name;
    }

    public long getTournamentId() {
        return tournament_id;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (creator_id != team.creator_id) return false;
        if (tournament_id != team.tournament_id) return false;
        if (checkedIn != team.checkedIn) return false;
        if (id != null ? !id.equals(team.id) : team.id != null) return false;
        if (!name.equals(team.name)) return false;
        return timeCreated != null ? timeCreated.equals(team.timeCreated) : team.timeCreated == null;
    }
}
