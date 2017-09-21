package com.krescendos.state;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.TrackPlayer;

public class StateUpdateRequestListener implements ValueEventListener {
    private TrackPlayer mPlayer;
    private Requester requester;
    private String partyCode;

    public StateUpdateRequestListener(Context context, String partyCode, TrackPlayer mPlayer) {
        this.mPlayer = mPlayer;
        this.partyCode = partyCode;
        this.requester = Requester.getInstance(context);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        requester.updatePlayState(partyCode, mPlayer.getState());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
