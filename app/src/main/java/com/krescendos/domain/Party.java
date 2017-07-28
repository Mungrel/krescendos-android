package com.krescendos.domain;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private static int PLAYHEAD_START_POSITION = 0;

    private int playheadIndex;
    private List<Track> playlist;
    private String name;
    private String partyId;

    public Party(String name, String partyId) {
        this.name = name;
        this.playheadIndex = PLAYHEAD_START_POSITION;
        this.playlist = new ArrayList<Track>();
        this.partyId = partyId;
    }

    public int getPlayheadIndex() {
        return playheadIndex;
    }

    public List<Track> getPlaylist() {
        return playlist;
    }

    public String getName() {
        return name;
    }

    public String getPartyId() {
        return partyId;
    }
}
