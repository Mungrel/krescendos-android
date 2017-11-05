package com.krescendos.state;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.player.TrackPlayer;
import com.krescendos.web.Requester;

public class StateUpdateRequestListener implements ValueEventListener {
    private TrackPlayer mPlayer;
    private String partyCode;

    public StateUpdateRequestListener(String partyCode, TrackPlayer mPlayer) {
        this.mPlayer = mPlayer;
        this.partyCode = partyCode;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FirebaseManager.updatePlayState(partyCode, mPlayer.getState());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
