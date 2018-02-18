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

public class TournamentDef {//implements Parcelable {
/*
    public static final Creator<TournamentDef> CREATOR
            = new Creator<TournamentDef>() {
        public TournamentDef createFromParcel(Parcel in) {
            return new TournamentDef(in);
        }

        public TournamentDef[] newArray(int size) {
            return new TournamentDef[size];
        }
    };
*/
    private String name;
    private String description;
    private Place address;
    private Time startTime;
    private int maxTeams;
    private int teamSize;
    private TournamentType tournamentType;
    private Bitmap logo;
    private int numCourts;

    public TournamentDef(TournamentDef t) {
        this(t.name, t.description, t.address, t.startTime, t.maxTeams, t.teamSize, t.tournamentType, t.logo, t.numCourts);
    }

    public TournamentDef() {}

    public TournamentDef(String name, Place address, Time startTime, int maxTeams, int teamSize, TournamentType tournamentType, int numCourts) {
        this(name, null, address, startTime, maxTeams, teamSize, tournamentType, null, numCourts);
    }

    public TournamentDef(String name, String description, Place address, Time startTime, int maxTeams, int teamSize, TournamentType tournamentType, Bitmap logo, int numCourts) {
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
/*
    private TournamentDef(Parcel in) {
        name = in.readString();
        description = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        startTime = (Time) in.readSerializable();
        maxTeams = in.readInt();
        tournamentType = TournamentType.values()[in.readInt()];
        logo = in.readParcelable(Bitmap.class.getClassLoader());
        numCourts = in.readInt();
    }
*/
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
/*
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeParcelable(address, flags);
        out.writeSerializable(startTime);
        out.writeInt(maxTeams);
        out.writeInt(tournamentType.ordinal());
        out.writeParcelable(logo, flags);
        out.writeInt(numCourts);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    */
}
