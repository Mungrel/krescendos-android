package com.krescendos.web;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;

import java.util.List;

public class PlaylistAppendListener implements ChildEventListener {

    private TrackListAdapter trackListAdapter;

    public PlaylistAppendListener(TrackListAdapter trackListAdapter){
        this.trackListAdapter = trackListAdapter;
    }
    
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Track track = dataSnapshot.getValue(Track.class);
        List<Track> trackList = trackListAdapter.getTracks();
        trackList.add(track);
        trackListAdapter.updateTracks(trackList);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
