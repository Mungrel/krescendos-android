package com.krescendos.web;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.PartyState;
import com.krescendos.domain.PlaybackState;
import com.krescendos.timer.UpdateTimer;

public class PartyStateChangeListener implements ValueEventListener {

    private UpdateTimer updateTimer;

    public PartyStateChangeListener(UpdateTimer updateTimer) {
        this.updateTimer = updateTimer;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);
        Log.d("STATE", partyState.getPlaybackState().toString());
        if (partyState == null) {
            partyState = new PartyState(PlaybackState.PAUSE, 0);
        }
        if (partyState.getPlaybackState().equals(PlaybackState.PAUSE)) {
            Log.d("STATE", "" + partyState.getPlayheadPositionAtLastStateChange());
            updateTimer.pause();
            updateTimer.setTime(partyState.getPlayheadPositionAtLastStateChange());
        } else {
            Log.d("STATE", "" + partyState.getPlayheadPositionAtLastStateChange());
            updateTimer.setTime(partyState.getPlayheadPositionAtLastStateChange());
            updateTimer.start();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DBERROR", databaseError.getMessage());
    }
}
