package com.krescendos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Party implements Parcelable {

    public static final Creator<Party> CREATOR = new PartyCreator();
    private HashMap<String, Track> playlist;
    private String name;
    private String partyId;
    private String welcomeMessage;
    private boolean allowSuggestions;

    public Party(String name, String partyId) {
        this.name = name;
        this.playlist = new HashMap<>();
        this.partyId = partyId;
        this.allowSuggestions = true;
    }

    protected Party(Parcel in) {
        this.name = in.readString();
        this.partyId = in.readString();
        this.welcomeMessage = in.readString();
        this.allowSuggestions = in.readByte() != 0;
        this.playlist = (HashMap<String, Track>) in.readSerializable();
    }

    public List<Track> getPlaylist() {
        if (playlist == null) {
            return new ArrayList<Track>();
        }
        return new ArrayList<Track>(playlist.values());
    }

    public void setPlaylist(HashMap<String, Track> playlist) {
        this.playlist = playlist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(partyId);
        parcel.writeString(welcomeMessage);
        parcel.writeByte((byte) (allowSuggestions ? 1 : 0));
        parcel.writeSerializable(playlist);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public boolean getAllowSuggestions() {
        return allowSuggestions;
    }

    public void setAllowSuggestions(boolean allowSuggestions) {
        this.allowSuggestions = allowSuggestions;
    }
}
