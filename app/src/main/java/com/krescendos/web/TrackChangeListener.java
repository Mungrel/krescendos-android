package com.krescendos.web;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrackChangeListener implements ValueEventListener {
    TrackListAdapter trackListAdapter;

    public TrackChangeListener(TrackListAdapter trackListAdapter){
        this.trackListAdapter = trackListAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Track> tracks = new ArrayList<Track>();
        for (DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
            Track track = trackSnapshot.getValue(Track.class);
            tracks.add(track);
        }
        trackListAdapter.updateTracks(tracks);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("DB_ERROR", "Failed to read value", databaseError.toException());
    }
}
