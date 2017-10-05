package com.krescendos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Party implements Parcelable{

    private static int PLAYHEAD_START_POSITION = 0;

    private int playheadIndex;
    private HashMap<String, Track> playlist;
    private String name;
    private String partyId;
    private String welcomeMessage;

    public static final Creator<Party> CREATOR = new Creator<Party>() {

        @Override
        public Party createFromParcel(Parcel parcel) {
            return new Party(parcel);
        }

        @Override
        public Party[] newArray(int size) {
            return new Party[size];
        }
    };

    public Party(String name, String partyId) {
        this.name = name;
        this.playheadIndex = PLAYHEAD_START_POSITION;
        this.playlist = new HashMap<String, Track>();
        this.partyId = partyId;
    }



    public int getPlayheadIndex() {
        return playheadIndex;
    }

    public List<Track> getPlaylist() {
        if (playlist == null) {
            return new ArrayList<Track>();
        }
        return new ArrayList<Track>(playlist.values());
    }

    public String getName() {
        return name;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    protected Party(Parcel in) {
        this.playheadIndex = in.readInt();
        this.name = in.readString();
        this.partyId = in.readString();
        this.welcomeMessage = in.readString();
        this.playlist = (HashMap<String, Track>) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(playheadIndex);
        parcel.writeString(name);
        parcel.writeString(partyId);
        parcel.writeString(welcomeMessage);
        parcel.writeSerializable(playlist);
    }
}
