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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistChangeListener implements ChildEventListener {

    private List<String> keys;
    private Map<String, VoteItem<Track>> keyToItem;

    private PlaylistAdapter playlistAdapter;
    private TrackPlayer trackPlayer;

    public PlaylistChangeListener(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
        this.keys = new ArrayList<>();
        this.keyToItem = new HashMap<>();
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

        keyToItem.put(dataSnapshot.getKey(), item);

        int insertionIndex = 0;
        for (int i = keys.size()-1; i >= 0; i--) {
            VoteItem<Track> sortItem = keyToItem.get(keys.get(i));
            if (sortItem.getVoteCount() >= item.getVoteCount()) {
                insertionIndex = i+1;
                break;
            }
        }

        keys.add(insertionIndex, dataSnapshot.getKey());
        Track addedTrack = item.getItem();
        Log.d("CHILD_ADDED:", "" + addedTrack.getName());

        playlistAdapter.insert(insertionIndex, item);
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

        if (oldIndex == newIndex) {
            return;
        } else if (oldIndex < newIndex) {
            newIndex--;
        }

        keys.remove(oldIndex);
        keys.add(newIndex, movedTrackKey);

        playlistAdapter.moveItem(oldIndex, newIndex);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", "" + databaseError.getMessage() + ": " + databaseError.getDetails());
    }
}
