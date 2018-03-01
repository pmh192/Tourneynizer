package com.tourneynizer.tourneynizer.model;

import android.graphics.Bitmap;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;

import java.sql.Time;
import java.util.Locale;

/**
 * Created by ryanl on 2/3/2018.
 */

public class TournamentDef {

    private String name;
    private String description;
    private Place address;
    private Time startTime;
    private int maxTeams;
    private int teamSize;
    private TournamentType tournamentType;
    private Bitmap logo;

    public TournamentDef() {}

    public TournamentDef(String name, Place address, Time startTime, int maxTeams, int teamSize, TournamentType tournamentType) {
        this(name, null, address, startTime, maxTeams, teamSize, tournamentType, null);
    }

    public TournamentDef(String name, String description, Place address, Time startTime, int maxTeams, int teamSize, TournamentType tournamentType, Bitmap logo) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.maxTeams = maxTeams;
        this.teamSize = teamSize;
        this.tournamentType = tournamentType;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place getAddress() {
        return address;
    }

    public void setAddress(Place address) {
        this.address = address;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public TournamentType getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
}
