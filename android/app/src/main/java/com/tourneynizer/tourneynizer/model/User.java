package com.tourneynizer.tourneynizer.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;

/**
 * Created by ryanwiener on 2/13/18.
 */

public class User implements Parcelable {

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private long id;
    private String email;
    private String name;
    private Time timeCreated;
    private Bitmap profilePicture;
    private int wins;
    private int losses;
    private int tournamentsParticipated;

    private User(Parcel in) {
        id = in.readLong();
        email = in.readString();
        name = in.readString();
        timeCreated = (Time) in.readSerializable();
        profilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        wins = in.readInt();
        losses = in.readInt();
        tournamentsParticipated = in.readInt();
    }

    public User(User u) {
        this(u.id, u.email, u.name, u.timeCreated, u.profilePicture, u.wins, u.losses, u.tournamentsParticipated);
    }

    public User(long id, @NonNull String email, @NonNull String name, @NonNull Time timeCreated, int wins, int losses, int tournamentsParticipated) {
        this(id, email, name, timeCreated, null, wins, losses, tournamentsParticipated);
    }

    public User(long id, @NonNull String email, @NonNull String name, @NonNull Time timeCreated, Bitmap profilePicture, int wins, int losses, int tournamentsParticipated) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.timeCreated = timeCreated;
        this.profilePicture = profilePicture;
        this.wins = wins;
        this.losses = losses;
        this.tournamentsParticipated = tournamentsParticipated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeSerializable(timeCreated);
        parcel.writeParcelable(profilePicture, i);
        parcel.writeInt(wins);
        parcel.writeInt(losses);
        parcel.writeInt(tournamentsParticipated);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Time getTimeCreated() {
        return timeCreated;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTournamentsParticipated() {
        return tournamentsParticipated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return id == ((User) obj).id;
        }
        return false;
    }
}
