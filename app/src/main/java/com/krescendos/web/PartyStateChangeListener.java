package com.krescendos.web;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.PartyState;

public class PartyStateChangeListener implements ValueEventListener {

    private SeekBar seekBar;

    public PartyStateChangeListener(SeekBar seekBar){
        this.seekBar = seekBar;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);
        seekBar.setProgress((int) partyState.getPlayheadPositionAtLastStateChange());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DBERROR", databaseError.getMessage());
    }
}
