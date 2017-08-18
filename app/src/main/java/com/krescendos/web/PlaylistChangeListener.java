package com.krescendos.web;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.player.TrackPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistChangeListener implements ValueEventListener {
    private TrackListAdapter trackListAdapter;
    private TrackPlayer trackPlayer;

    public PlaylistChangeListener(TrackListAdapter trackListAdapter) {
        this.trackListAdapter = trackListAdapter;
    }

    public PlaylistChangeListener(TrackListAdapter trackListAdapter, TrackPlayer trackPlayer) {
        this(trackListAdapter);
        this.trackPlayer = trackPlayer;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Track> newList = new ArrayList<Track>();

        for (DataSnapshot track : dataSnapshot.getChildren()) {
            newList.add(track.getValue(Track.class));
        }

        List<Track> oldList = trackListAdapter.getTracks();
        logList("OLD:", oldList);
        newList = trackListDiff(oldList, newList);
        logList("DIFF:", newList);

        for (Track track : newList) {
            Log.d("TRACK", "Added: " + track.getName());
            trackListAdapter.addTrack(track);
            if (trackPlayer != null) {
                Log.d("QUEUE", "Track queued: " + track.getName());
                trackPlayer.queue(track);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", "" + databaseError.getMessage() + ": " + databaseError.getDetails());
    }

    private void logList(String tag, List<Track> tracks) {
        for (Track track : tracks) {
            Log.d(tag, track.getName());
        }
    }

    private List<Track> trackListDiff(List<Track> oldList, List<Track> newList) {
        List<Track> found = new ArrayList<Track>();
        for (Track newTrack : newList) {
            for (Track oldTrack : oldList) {
                if (newTrack.getId().equals(oldTrack.getId())) {
                    found.add(newTrack);
                }
            }
        }
        newList.removeAll(found);
        return newList;
    }
}
