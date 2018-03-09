package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class Tournament {
    private Long id;
    private String name, address;
    private Timestamp timeCreated, startTime;
    private int teamSize, maxTeams;
    private TournamentType type;
    private long creatorId;


    public Tournament(String name, String address, Timestamp startTime, int teamSize, int maxTeams, TournamentType type,
                      long creatorId) {


        setName(name);
        setAddress(address);
        setStartTime(startTime);
        setTeamSize(teamSize);
        setMaxTeams(maxTeams);
        setTournamentType(type);
        setCreatorId(creatorId);
    }

    public Tournament(Long id, String name, String address, Timestamp timeCreated, Timestamp startTime, int teamSize,
                      int maxTeams, TournamentType type, long creatorId) {

        this(name, address, startTime, teamSize, maxTeams, type, creatorId);
        persist(id, timeCreated);
    }

    public void persist(Long id, Timestamp timeCreated) {
        if (timeCreated == null || id == null) {
            throw new IllegalArgumentException("Both id and timeCreated must be non null");
        }
        this.id = id;
        this.timeCreated = timeCreated;
    }

    public void setName(String name) {
        if (name == null) { throw new IllegalArgumentException("Name is required"); }
        if (name.trim().isEmpty()) { throw new IllegalArgumentException("Name cannot be empty"); }
        if (name.length() >= 256) { throw new IllegalArgumentException("That name is too long"); }
        this.name = name;
    }

    public void setAddress(String address) {
        if (address == null) { throw new IllegalArgumentException("Address is required"); }
        if (address.trim().isEmpty()) { throw new IllegalArgumentException("Address cannot be empty"); }
        if (address.length() >= 256) { throw new IllegalArgumentException("That address is too long"); }
        this.address = address;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public void setTournamentType(TournamentType type) {
        if (type == null) { throw new IllegalArgumentException("Tournament type cannot be null"); }
        this.type = type;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public TournamentType getType() {
        return type;
    }

    public long getCreatorId() {
        return creatorId;
    }

    @JsonIgnore
    public boolean isPersisted() {
        return id != null;
    }

    private boolean equalsHelper(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null) return false;
        return o1.equals(o2);
    }

    @Override
    public boolean equals(Object o) {
        if (!getClass().isInstance(o)) { return false; }
        Tournament other = (Tournament) o;

        return equalsHelper(other.id, this.id) &&
                equalsHelper(other.name, this.name) &&
                equalsHelper(other.address, this.address) &&
                equalsHelper(other.timeCreated, this.timeCreated) &&
                equalsHelper(other.startTime, this.startTime) &&
                equalsHelper(other.maxTeams, this.maxTeams) &&
                equalsHelper(other.teamSize, this.teamSize) &&
                equalsHelper(other.type, this.type) &&
                equalsHelper(other.creatorId, this.creatorId);
    }
}
