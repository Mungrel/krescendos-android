package com.krescendos.state;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.player.UpNextAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpNextChangeListener implements ChildEventListener {

    private List<String> keys;

    private UpNextAdapter upNextAdapter;

    public UpNextChangeListener(UpNextAdapter upNextAdapter) {
        this.upNextAdapter = upNextAdapter;
        this.keys = new ArrayList<>();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        GenericTypeIndicator<VoteItem<Track>> type = new GenericTypeIndicator<VoteItem<Track>>() {
        };

        VoteItem<Track> item = dataSnapshot.getValue(type);
        Log.d("CHILD_ADDED", "Key: " + dataSnapshot.getKey());

        item.setItemId(dataSnapshot.getKey());

        int insertionIndex = (previousChildName == null) ? 0 : keys.indexOf(previousChildName) + 1;
        keys.add(insertionIndex, dataSnapshot.getKey());

        upNextAdapter.insertItem(insertionIndex, item);
        upNextAdapter.setItemVoteCount(insertionIndex, item.getVoteCount() * -1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("CHILD_CHANGED", "Key:" + dataSnapshot.getKey());
        int index = keys.indexOf(dataSnapshot.getKey());

        GenericTypeIndicator<VoteItem<Track>> type = new GenericTypeIndicator<VoteItem<Track>>() {
        };

        VoteItem<Track> item = dataSnapshot.getValue(type);

        upNextAdapter.setItemVoteCount(index, item.getVoteCount() * -1);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        int index = keys.indexOf(dataSnapshot.getKey());
        upNextAdapter.removeItem(index);
        keys.remove(index);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        String movedTrackKey = dataSnapshot.getKey();
        Log.d("CHILD_MOVED", "Key: " + movedTrackKey);

        int oldIndex = keys.indexOf(movedTrackKey);
        keys.remove(oldIndex);
        int newIndex = (previousChildName == null) ? 0 : keys.indexOf(previousChildName) + 1;

        keys.add(newIndex, movedTrackKey);

        upNextAdapter.moveItem(oldIndex, newIndex);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", "" + databaseError.getMessage() + ": " + databaseError.getDetails());
    }
}
