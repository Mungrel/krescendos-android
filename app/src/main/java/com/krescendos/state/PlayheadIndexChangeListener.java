package com.krescendos.state;

import android.widget.SeekBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.PlaylistAdapter;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private PlaylistAdapter playlistAdapter;
    private SeekBar seekBar;

    public PlayheadIndexChangeListener(PlaylistAdapter playlistAdapter, SeekBar seekBar) {
        this.playlistAdapter = playlistAdapter;
        this.seekBar = seekBar;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        playlistAdapter.poll();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
