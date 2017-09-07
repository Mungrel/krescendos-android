package com.krescendos.web;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.player.TrackPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlaylistChangeListener implements ValueEventListener {
    private TrackListAdapter trackListAdapter;
    private TrackPlayer trackPlayer;
    private DatabaseReference playHeadIndexRef;
    private DatabaseReference partyStateRef;
    private PlayheadIndexChangeListener playheadIndexChangeListener;
    private PartyStateChangeListener partyStateChangeListener;
    private boolean listenersSet;
    private Context context;
    private String partyCode;

    public PlaylistChangeListener(Context context, String partyCode, TrackListAdapter trackListAdapter, DatabaseReference playHeadIndexRef,
                                  DatabaseReference partyStateRef,
                                  PlayheadIndexChangeListener playheadIndexChangeListener,
                                  PartyStateChangeListener partyStateChangeListener) {
        this.context = context;
        this.partyCode = partyCode;
        this.trackListAdapter = trackListAdapter;
        this.playHeadIndexRef = playHeadIndexRef;
        this.partyStateRef = partyStateRef;
        this.playheadIndexChangeListener = playheadIndexChangeListener;
        this.partyStateChangeListener = partyStateChangeListener;
        this.listenersSet = false;
    }

    public PlaylistChangeListener(TrackListAdapter trackListAdapter, TrackPlayer trackPlayer) {
        this.trackListAdapter = trackListAdapter;
        this.trackPlayer = trackPlayer;
        this.listenersSet = true;
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

        if (!listenersSet) {
            playHeadIndexRef.addValueEventListener(playheadIndexChangeListener);
            partyStateRef.addValueEventListener(partyStateChangeListener);
            new Requester(context).requestPartyStateUpdate(partyCode);
            listenersSet = true;
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
        if (oldList.isEmpty()){
            return newList;
        }
        if (newList.isEmpty()){
            return oldList;
        }
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
