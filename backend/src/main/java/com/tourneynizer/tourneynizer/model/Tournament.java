package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;

public class Tournament {
    private Long id;
    private String name, address;
    private Timestamp timeCreated, startTime;
    private int teamSize, maxTeams, numCourts;
    private TournamentType type;
    private long creatorId;


    public Tournament(String name, String address, Timestamp startTime, int teamSize, int maxTeams, TournamentType type,
                      int numCourts, long creatorId) {


        setName(name);
        setAddress(address);
        setStartTime(startTime);
        setTeamSize(teamSize);
        setMaxTeams(maxTeams);
        setTournamentType(type);
        setNumCourts(numCourts);
        setCreatorId(creatorId);
    }

    public void setName(String name) {
        if (name == null) { throw new IllegalArgumentException("Name is required"); }
        if (name.trim().isEmpty()) { throw new IllegalArgumentException("Name cannot be empty"); }
        this.name = name;
    }

    public void setAddress(String address) {
        if (address == null) { throw new IllegalArgumentException("Address is required"); }
        if (address.trim().isEmpty()) { throw new IllegalArgumentException("Address cannot be empty"); }
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

    public void setNumCourts(int numCourts) {
        this.numCourts = numCourts;
    }

    public void setTournamentType(TournamentType type) {
        this.type = type;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
