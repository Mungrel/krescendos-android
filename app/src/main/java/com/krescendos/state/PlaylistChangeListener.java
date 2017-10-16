package com.krescendos.state;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.reflect.TypeToken;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.player.PlaylistAdapter;
import com.krescendos.player.TrackPlayer;

import java.lang.reflect.Type;
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
        GenericTypeIndicator<VoteItem<Track>> type = new GenericTypeIndicator<VoteItem<Track>>() {};
        VoteItem<Track> item = dataSnapshot.getValue(type);
        item.setDbKey(dataSnapshot.getKey());

        Track addedTrack = item.getItem();
        Log.d("CHILD_ADDED:", "" + addedTrack.getName());

        playlistAdapter.append(item);
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
