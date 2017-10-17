package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.player.CurrentlyPlayingAdapter;
import com.krescendos.player.TrackPlayer;

public class CurrentlyPlayingChangeListener implements ValueEventListener {

    private CurrentlyPlayingAdapter currentlyPlayingAdapter;
    private TrackPlayer trackPlayer;

    public CurrentlyPlayingChangeListener(CurrentlyPlayingAdapter currentlyPlayingAdapter) {
        this.currentlyPlayingAdapter = currentlyPlayingAdapter;
    }

    public CurrentlyPlayingChangeListener(CurrentlyPlayingAdapter currentlyPlayingAdapter, TrackPlayer trackPlayer) {
        this.currentlyPlayingAdapter = currentlyPlayingAdapter;
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<VoteItem<Track>> type = new GenericTypeIndicator<VoteItem<Track>>() {};

        VoteItem<Track> newItem = dataSnapshot.getValue(type);
        if (newItem == null) {
            return;
        }
        currentlyPlayingAdapter.setCurrentlyPlaying(newItem);

        if (trackPlayer != null) {
            trackPlayer.setCurrentlyPlaying(newItem.getItem());
            trackPlayer.play();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
