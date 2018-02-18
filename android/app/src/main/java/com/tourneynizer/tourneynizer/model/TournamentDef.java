package com.tourneynizer.tourneynizer.model;

import android.graphics.Bitmap;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.Locale;

/**
 * Created by ryanl on 2/3/2018.
 */

public class TournamentDef implements Parcelable {

    public static final Creator<TournamentDef> CREATOR
            = new Creator<TournamentDef>() {
        public TournamentDef createFromParcel(Parcel in) {
            return new TournamentDef(in);
        }

        public TournamentDef[] newArray(int size) {
            return new TournamentDef[size];
        }
    };

    private String name;
    private String description;
    private Address address;
    private Time startTime;
    private int maxTeams;
    private TournamentType tournamentType;
    private Bitmap logo;
    private int numCourts;

    public TournamentDef(TournamentDef t) {
        this(t.name, t.description, t.address, t.startTime, t.maxTeams, t.tournamentType, t.logo, t.numCourts);
    }

    public TournamentDef() {
        name = "";
        description = "";
        address = new Address(Locale.getDefault());
        startTime = new Time(0);
        maxTeams = 10;
        tournamentType = TournamentType.VOLLEYBALL_POOLED;
        logo = null;
        numCourts = 1;
    }

    public TournamentDef(@NonNull String name, @NonNull Address address, @NonNull Time startTime, int maxTeams, @NonNull TournamentType tournamentType, int numCourts) {
        this(name, null, address, startTime, maxTeams, tournamentType, null, numCourts);
    }

    public TournamentDef(@NonNull String name, String description, @NonNull Address address, @NonNull Time startTime, int maxTeams, @NonNull TournamentType tournamentType, Bitmap logo, int numCourts) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.maxTeams = maxTeams;
        this.tournamentType = tournamentType;
        this.logo = logo;
        this.numCourts = numCourts;
    }

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

    public @NonNull String getName() {
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

    public @NonNull Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public @NonNull Time getStartTime() {
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

    public @NonNull TournamentType getTournamentType() {
        return tournamentType;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public int getNumCourts() {
        return numCourts;
    }

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
}
