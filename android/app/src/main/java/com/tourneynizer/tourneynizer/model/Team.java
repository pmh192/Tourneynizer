package com.tourneynizer.tourneynizer.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanwiener on 2/28/18.
 */

public class Team implements Parcelable {

    private long id;
    private String name;
    private Time timeCreated;
    private long creatorID;
    private long tournamentID;
    private boolean checkedIn;
    private boolean approved;
    private Bitmap logo;

    public Team(long id, @NonNull String name, @NonNull Time timeCreated, long creatorID, long tournamentID) {
        this(id, name, timeCreated, creatorID, tournamentID, false, false, null);
    }

    public Team(long id, @NonNull String name, @NonNull Time timeCreated, long creatorID, long tournamentID, boolean checkedIn, boolean approved, Bitmap logo) {
        this.id = id;
        this.name = name;
        this.timeCreated = timeCreated;
        this.creatorID = creatorID;
        this.tournamentID = tournamentID;
        this.checkedIn = false;
        this.approved = false;
        this.logo = logo;
    }

    protected Team(Parcel in) {
        id = in.readLong();
        name = in.readString();
        timeCreated = (Time) in.readSerializable();
        creatorID = in.readLong();
        tournamentID = in.readLong();
        checkedIn = in.readByte() != 0;
        approved = in.readByte() != 0;
        logo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public long getID() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull Time getTimeCreated() {
        return timeCreated;
    }

    public long getCreatorID() {
        return creatorID;
    }

    public long getTournamentID() {
        return tournamentID;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public boolean isApproved() {
        return approved;
    }

    public Bitmap getLogo() {
        return logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeSerializable(timeCreated);
        parcel.writeLong(creatorID);
        parcel.writeLong(tournamentID);
        parcel.writeByte((byte) (checkedIn ? 1 : 0));
        parcel.writeByte((byte) (approved ? 1 : 0));
        parcel.writeParcelable(logo, i);
    }
}
