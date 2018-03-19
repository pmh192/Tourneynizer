package com.tourneynizer.tourneynizer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanl on 3/11/2018.
 */

public class Match implements Parcelable {

    private long id;
    private long tournamentID;
    private int order;
    private Long refereeID;
    private Long team1ID;
    private Long team2ID;
    private Long score1;
    private Long score2;
    private Long child1ID;
    private Long child2ID;
    private String scoreType;
    private Time startTime;
    private Time endTime;
    private String status;

    public Match(long id, long tournamentID, int order, Long refereeID, Long team1ID, Long team2ID, Long score1, Long score2, Long child1ID, Long child2ID, String scoreType, Time startTime, Time endTime, String status) {
        this.id = id;
        this.tournamentID = tournamentID;
        this.order = order;
        this.refereeID = refereeID;
        this.team1ID = team1ID;
        this.team2ID = team2ID;
        this.score1 = score1;
        this.score2 = score2;
        this.child1ID = child1ID;
        this.child2ID = child2ID;
        this.scoreType = scoreType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    private Match(Parcel in) {
        id = in.readLong();
        tournamentID = in.readLong();
        order = in.readInt();
        if (in.readByte() == 0) {
            refereeID = null;
        } else {
            refereeID = in.readLong();
        }
        if (in.readByte() == 0) {
            team1ID = null;
        } else {
            team1ID = in.readLong();
        }
        if (in.readByte() == 0) {
            team2ID = null;
        } else {
            team2ID = in.readLong();
        }
        if (in.readByte() == 0) {
            score1 = null;
        } else {
            score1 = in.readLong();
        }
        if (in.readByte() == 0) {
            score2 = null;
        } else {
            score2 = in.readLong();
        }
        if (in.readByte() == 0) {
            child1ID = null;
        } else {
            child1ID = in.readLong();
        }
        if (in.readByte() == 0) {
            child2ID = null;
        } else {
            child2ID = in.readLong();
        }
        scoreType = in.readString();
        startTime = (Time) in.readSerializable();
        endTime = (Time) in.readSerializable();
        status = in.readString();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public long getID() {
        return id;
    }

    public long getTournamentID() {
        return tournamentID;
    }

    public int getOrder() {
        return order;
    }

    public Long getRefereeID() {
        return refereeID;
    }

    public Long getTeam1ID() {
        return team1ID;
    }

    public Long getTeam2ID() {
        return team2ID;
    }

    public long getScore1() {
        if (score1 == null) {
            return 0;
        }
        return score1;
    }

    public void setScore1(Long score1) {
        this.score1 = score1;
    }

    public void setScore2(Long score2) {
        this.score2 = score2;
    }

    public long getScore2() {
        if (score2 == null) {
            return 0;
        }
        return score2;
    }

    public Long getChild1ID() {
        return child1ID;
    }

    public Long getChild2ID() {
        return child2ID;
    }

    public String getScoreType() {
        return scoreType;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public boolean isReady() {
        return team1ID != null && team2ID != null && startTime != null;
    }

    public boolean hasStarted() {
        return !status.equals("CREATED");
    }

    public boolean hasFinished() {
        return status.equals("COMPLETED");
    }

    public void start() {
        status = "STARTED";
    }

    public void end() {
        status = "COMPLETED";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(tournamentID);
        parcel.writeInt(order);
        if (refereeID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(refereeID);
        }
        if (team1ID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(team1ID);
        }
        if (team2ID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(team2ID);
        }
        if (score1 == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(score1);
        }
        if (score2 == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(score2);
        }
        if (child1ID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(child1ID);
        }
        if (child2ID == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(child2ID);
        }
        parcel.writeString(scoreType);
        parcel.writeSerializable(startTime);
        parcel.writeSerializable(endTime);
        parcel.writeString(status);
    }
}
