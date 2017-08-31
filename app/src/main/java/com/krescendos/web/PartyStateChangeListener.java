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
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Log.d("STATE-CHILD-KEY", "" + child.getKey());
            Log.d("STATE-CHILD-VALUE", "" + child.getValue());
        }
        PartyState partyState = dataSnapshot.getValue(PartyState.class);

        if (partyState == null) {
            Log.d("STATE", "" + partyState);
            partyState = new PartyState(PlaybackState.PAUSE, 0);
        }
        Log.d("STATE", partyState.getPlaybackState().toString());
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
