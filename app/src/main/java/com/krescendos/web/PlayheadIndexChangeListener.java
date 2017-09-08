package com.krescendos.web;

import android.util.Log;
import android.widget.SeekBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private TrackListAdapter adapter;
    private SeekBar seekBar;

    public PlayheadIndexChangeListener(TrackListAdapter adapter, SeekBar seekBar) {
        this.adapter = adapter;
        this.seekBar = seekBar;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int newIndex = dataSnapshot.getValue(Integer.class);
        if (adapter.getTracks().isEmpty()) {
            return;
        }
        adapter.setCurrentPosition(newIndex);
        Track newCurrentTrack = adapter.getTracks().get(newIndex);

        seekBar.setMax((int) newCurrentTrack.getDuration_ms());
        Log.d("MAX", "MaxSet");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
