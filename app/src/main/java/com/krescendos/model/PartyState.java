package com.krescendos.model;

public class PartyState {
    private PlaybackState playbackState;
    private long playheadPositionAtLastStateChange;

    public PartyState() {
    }

    public PartyState(PlaybackState playbackState, long playheadPositionAtLastStateChange) {
        this.playbackState = playbackState;
        this.playheadPositionAtLastStateChange = playheadPositionAtLastStateChange;
    }

    public PlaybackState getPlaybackState() {
        return playbackState;
    }

    public long getPlayheadPositionAtLastStateChange() {
        return playheadPositionAtLastStateChange;
    }
}
