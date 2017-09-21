package com.krescendos.state;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.model.PartyState;
import com.krescendos.model.PlaybackState;
import com.krescendos.timer.UpdateTimer;

public class PartyStateChangeListener implements ValueEventListener {

    private UpdateTimer updateTimer;

    public PartyStateChangeListener(UpdateTimer updateTimer) {
        this.updateTimer = updateTimer;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);

        if (partyState == null) {
            partyState = new PartyState(PlaybackState.PAUSE, 0);
        }
        Log.d("STATE", partyState.getPlaybackState().toString());
        if (partyState.getPlaybackState().equals(PlaybackState.PAUSE)) {
            updateTimer.pause();
            updateTimer.setTime(partyState.getPlayheadPositionAtLastStateChange());
        } else {
            updateTimer.setTime(partyState.getPlayheadPositionAtLastStateChange());
            updateTimer.start();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DB_ERROR", databaseError.getMessage());
    }
}
