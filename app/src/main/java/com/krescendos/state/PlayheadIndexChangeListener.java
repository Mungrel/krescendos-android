package com.krescendos.state;

import android.widget.SeekBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.PlaylistAdapter;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private PlaylistAdapter playlistAdapter;
    private Integer previousPos;


    public PlayheadIndexChangeListener(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
        this.previousPos = null;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Integer newPos = dataSnapshot.getValue(Integer.class);

        if (previousPos == null) {
            previousPos = newPos;
            return;
        }

        playlistAdapter.poll();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
