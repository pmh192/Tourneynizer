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
    private int numCourts;
    
    private TournamentDef(String name, String description, Place address, Time startTime, int maxTeams, int teamSize, TournamentType tournamentType, Bitmap logo, int numCourts) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.maxTeams = maxTeams;
        this.teamSize = teamSize;
        this.tournamentType = tournamentType;
        this.logo = logo;
        this.numCourts = numCourts;
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

    public int getNumCourts() {
        return numCourts;
    }

    public void setNumCourts(int numCourts) {
        this.numCourts = numCourts;
    }

    public static class Builder {

        private String name;
        private String description;
        private Place address;
        private Time startTime;
        private int maxTeams;
        private int teamSize;
        private TournamentType tournamentType;
        private Bitmap logo;
        private int numCourts;

        public Builder() {}

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAddress(Place place) {
            address = place;
            return  this;
        }

        public Builder setStartTime(Time t) {
            startTime = t;
            return this;
        }

        public Builder setMaxTeams(int maxTeams) {
            this.maxTeams = maxTeams;
            return this;
        }

        public Builder setTeamSize(int teamSize) {
            this.teamSize = teamSize;
            return this;
        }

        public Builder setTournamentType(TournamentType tournamentType) {
            this.tournamentType = tournamentType;
            return this;
        }

        public Builder setLogo(Bitmap logo) {
            this.logo = logo;
            return this;
        }

        public Builder setNumCourts(int numCourts) {
            this.numCourts = numCourts;
            return this;
        }

        public TournamentDef build() {
            return new TournamentDef(name, description, address, startTime, maxTeams, teamSize, tournamentType, logo, numCourts);
        }
    }
}
