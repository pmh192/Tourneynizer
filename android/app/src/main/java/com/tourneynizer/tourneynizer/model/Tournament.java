package com.tourneynizer.tourneynizer.model;

import android.graphics.Bitmap;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanl on 2/3/2018.
 */

public class Tournament implements Parcelable {

    public static final Parcelable.Creator<Tournament> CREATOR
            = new Parcelable.Creator<Tournament>() {
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }

        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    private long id;
    private String name;
    private String description;
    private Address address;
    private Time startTime;
    private Time endTime;
    private int maxTeams;
    private int currentTeams;
    private Time timeCreated;
    private TournamentType tournamentType;
    private Bitmap logo;
    private long creatorUserID;
    private boolean cancelled;
    public String status;

    public Tournament(Tournament t) {
        this(t.id, t.name, t.description, t.address, t.startTime, t.endTime, t.maxTeams, t.currentTeams, t.timeCreated, t.tournamentType, t.logo, t.creatorUserID, t.cancelled);
    }

    public Tournament(long id, @NonNull String name, @NonNull Address address, @NonNull Time startTime, int maxTeams, int currentTeams, @NonNull Time timeCreated, @NonNull TournamentType tournamentType, long creatorUserID) {
        this(id, name, null, address, startTime, null, maxTeams, currentTeams, timeCreated, tournamentType, null, creatorUserID, false);
    }

    public Tournament(long id, @NonNull String name, String description, @NonNull Address address, @NonNull Time startTime, Time endTime, int maxTeams, int currentTeams, @NonNull Time timeCreated, @NonNull TournamentType tournamentType, Bitmap logo, long creatorUserID, boolean cancelled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxTeams = maxTeams;
        this.currentTeams = currentTeams;
        this.timeCreated = timeCreated;
        this.tournamentType = tournamentType;
        this.logo = logo;
        this.creatorUserID = creatorUserID;
        this.cancelled = cancelled;
    }

    private Tournament(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        startTime = (Time) in.readSerializable();
        endTime = (Time) in.readSerializable();
        maxTeams = in.readInt();
        timeCreated = (Time) in.readSerializable();
        tournamentType = TournamentType.values()[in.readInt()];
        logo = in.readParcelable(Bitmap.class.getClassLoader());
        creatorUserID = in.readLong();
        cancelled = in.readByte() == 1;
        status = in.readString();
    }

    public long getID() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public @NonNull Address getAddress() {
        return address;
    }

    public @NonNull Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public int getCurrentTeams() {
        return currentTeams;
    }

    public @NonNull Time getTimeCreated() {
        return timeCreated;
    }

    public @NonNull TournamentType getTournamentType() {
        return tournamentType;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public long getCreatorUserID() {
        return creatorUserID;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean hasStarted() {
        return !status.equals("CREATED");
    }

    public boolean hasFinished() {
        return status.equals("FINISHED");
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(name);
        out.writeString(description);
        out.writeParcelable(address, flags);
        out.writeSerializable(startTime);
        out.writeSerializable(endTime);
        out.writeInt(maxTeams);
        out.writeSerializable(timeCreated);
        out.writeInt(tournamentType.ordinal());
        out.writeParcelable(logo, flags);
        out.writeLong(creatorUserID);
        out.writeByte((byte) (cancelled ? 1 : 0));
        out.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
