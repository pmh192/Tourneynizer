package com.tourneynizer.tourneynizer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanl on 2/28/2018.
 */

public class TeamRequest implements Parcelable {

    private long id;
    private long teamID;
    private long userID;
    private long requesterID;
    private Boolean accepted;
    private Time timeRequested;

    public TeamRequest(long id, long teamID, long userID, long requesterID, Boolean accepted, @NonNull Time timeRequested) {
        this.id = id;
        this.teamID = teamID;
        this.userID = userID;
        this.requesterID = requesterID;
        this.accepted = accepted;
        this.timeRequested = timeRequested;
    }

    private TeamRequest(Parcel in) {
        id = in.readLong();
        teamID = in.readLong();
        userID = in.readLong();
        requesterID = in.readLong();
        byte tmpAccepted = in.readByte();
        accepted = tmpAccepted == 0 ? null : tmpAccepted == 1;
        timeRequested = (Time) in.readSerializable();
    }

    public static final Creator<TeamRequest> CREATOR = new Creator<TeamRequest>() {
        @Override
        public TeamRequest createFromParcel(Parcel in) {
            return new TeamRequest(in);
        }

        @Override
        public TeamRequest[] newArray(int size) {
            return new TeamRequest[size];
        }
    };

    public long getID() {
        return id;
    }

    public long getTeamID() {
        return teamID;
    }

    public long getUserID() {
        return userID;
    }

    public long getRequesterID() {
        return requesterID;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public @NonNull Time getTimeRequested() {
        return timeRequested;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(teamID);
        parcel.writeLong(userID);
        parcel.writeLong(requesterID);
        parcel.writeByte((byte) (accepted == null ? 0 : accepted ? 1 : 2));
        parcel.writeSerializable(timeRequested);
    }
}
