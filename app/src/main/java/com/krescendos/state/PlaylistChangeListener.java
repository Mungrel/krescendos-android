package com.krescendos.state;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.krescendos.model.Track;
import com.krescendos.player.PlaylistAdapter;
import com.krescendos.player.TrackPlayer;

public class PlaylistChangeListener implements ChildEventListener {
    private PlaylistAdapter playlistAdapter;
    private TrackPlayer trackPlayer;

    public PlaylistChangeListener(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
    }

    public PlaylistChangeListener(PlaylistAdapter playlistAdapter, TrackPlayer trackPlayer) {
        this.playlistAdapter = playlistAdapter;
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Track addedTrack = dataSnapshot.getValue(Track.class);
        Log.d("CHILD_ADDED:", "" + addedTrack.getName());

        playlistAdapter.appendTrack(addedTrack);
        if (trackPlayer != null) {
            trackPlayer.queue(addedTrack);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", "" + databaseError.getMessage() + ": " + databaseError.getDetails());
    }
}
