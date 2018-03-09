package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class Tournament {
    private Long id;
    private String name;
    private double lat, lng;
    private Timestamp timeCreated, startTime;
    private int teamSize, maxTeams, numCourts;
    private TournamentType type;
    private long creatorId;


    public Tournament(String name, double lat, double lng, Timestamp startTime, int teamSize, int maxTeams,
                      TournamentType type, int numCourts, long creatorId) {


        setName(name);
        setLat(lat);
        setLng(lng);
        setStartTime(startTime);
        setTeamSize(teamSize);
        setMaxTeams(maxTeams);
        setTournamentType(type);
        setNumCourts(numCourts);
        setCreatorId(creatorId);
    }

    public Tournament(Long id, String name, double lat, double lng, Timestamp timeCreated, Timestamp startTime, int teamSize,
                      int maxTeams, TournamentType type, int numCourts, long creatorId) {

        this(name, lat, lng, startTime, teamSize, maxTeams, type, numCourts, creatorId);
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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    public void setNumCourts(int numCourts) {
        this.numCourts = numCourts;
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


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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

    public int getNumCourts() {
        return numCourts;
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
                equalsHelper(other.lat, this.lat) &&
                equalsHelper(other.lng, this.lng) &&
                equalsHelper(other.timeCreated, this.timeCreated) &&
                equalsHelper(other.startTime, this.startTime) &&
                equalsHelper(other.maxTeams, this.maxTeams) &&
                equalsHelper(other.teamSize, this.teamSize) &&
                equalsHelper(other.numCourts, this.numCourts) &&
                equalsHelper(other.type, this.type) &&
                equalsHelper(other.creatorId, this.creatorId);
    }
}
