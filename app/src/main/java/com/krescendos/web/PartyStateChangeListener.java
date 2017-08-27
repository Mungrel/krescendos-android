package com.krescendos.web;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.PartyState;
import com.krescendos.domain.PlaybackState;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.timer.UpdateTimer;

public class PartyStateChangeListener implements ValueEventListener {

    private SeekBar seekBar;
    private TrackListAdapter listAdapter;
    private UpdateTimer updateTimer;

    public PartyStateChangeListener(SeekBar seekBar, TrackListAdapter listAdapter, UpdateTimer updateTimer){
        this.seekBar = seekBar;
        this.listAdapter = listAdapter;
        this.updateTimer = updateTimer;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);
        if (listAdapter.getTracks().isEmpty()){
            return;
        }
        long currentTrackLength = listAdapter.getTracks().get(listAdapter.getCurrentPosition()).getDuration_ms();
        seekBar.setMax((int)currentTrackLength);
        seekBar.setProgress((int) partyState.getPlayheadPositionAtLastStateChange());
        updateTimer.setTime(partyState.getPlayheadPositionAtLastStateChange());
        if (partyState.getPlaybackState().equals(PlaybackState.PAUSE)){
            updateTimer.pause();
        } else {
            updateTimer.start();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("DBERROR", databaseError.getMessage());
    }
}
