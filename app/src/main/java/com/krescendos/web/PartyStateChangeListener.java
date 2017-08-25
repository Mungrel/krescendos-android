package com.krescendos.web;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.PartyState;
import com.krescendos.player.TrackListAdapter;

public class PartyStateChangeListener implements ValueEventListener {

    private SeekBar seekBar;
    private TrackListAdapter listAdapter;

    public PartyStateChangeListener(SeekBar seekBar, TrackListAdapter listAdapter){
        this.seekBar = seekBar;
        this.listAdapter = listAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);
        long currentTrackLength = listAdapter.getTracks().get(listAdapter.getCurrentPosition()).getDuration_ms();
        seekBar.setMax((int)currentTrackLength);
        seekBar.setProgress((int) partyState.getPlayheadPositionAtLastStateChange());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DBERROR", databaseError.getMessage());
    }
}
