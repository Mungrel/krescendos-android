package com.krescendos.state;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.krescendos.model.Track;
import com.krescendos.player.PlaylistAdapter;
import com.krescendos.player.TrackPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistChangeListener implements ChildEventListener {

    private List<String> keys;

    private PlaylistAdapter playlistAdapter;
    private TrackPlayer trackPlayer;

    public PlaylistChangeListener(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
        this.keys = new ArrayList<>();
    }

    public PlaylistChangeListener(PlaylistAdapter playlistAdapter, TrackPlayer trackPlayer) {
        this(playlistAdapter);
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
        playlistAdapter.poll();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        String movedTrackKey = dataSnapshot.getKey();

        int oldIndex = keys.indexOf(movedTrackKey);
        int newIndex = (previousChildName == null) ? 0 : keys.indexOf(previousChildName) + 1;

        keys.remove(oldIndex);
        keys.add(newIndex, movedTrackKey);

        playlistAdapter.moveItem(oldIndex, newIndex);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", "" + databaseError.getMessage() + ": " + databaseError.getDetails());
    }
}
