package com.krescendos.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Party {

    private static int PLAYHEAD_START_POSITION = 0;

    private int playheadIndex;
    private Map<String, Track> playlist;
    private String name;
    private String partyId;
    private String welcomeMessage;

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
}
