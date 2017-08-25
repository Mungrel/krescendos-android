package com.krescendos.domain;

public class PartyState {
    PlaybackState playbackState;
    int playheadPositionAtLastStateChange;

    public PlaybackState getPlaybackState() {
        return playbackState;
    }

    public int getPlayheadPositionAtLastStateChange() {
        return playheadPositionAtLastStateChange;
    }
}
