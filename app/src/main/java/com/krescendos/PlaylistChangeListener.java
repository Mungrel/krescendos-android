package com.krescendos;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistChangeListener implements ValueEventListener {
    private TrackListAdapter trackListAdapter;

    public PlaylistChangeListener(TrackListAdapter trackListAdapter){
        this.trackListAdapter = trackListAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<Map<String, Track>> t = new GenericTypeIndicator<Map<String, Track>>() {};
        Map<String, Track> playlist = dataSnapshot.getValue(t);
        List<Track> list = new ArrayList<Track>();
        if (playlist != null){
            list.addAll(playlist.values());
        }
        trackListAdapter.updateTracks(list);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
