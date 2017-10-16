package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.PlaylistAdapter;

public class CurrentlyPlayingChangeListener implements ValueEventListener {

    private PlaylistAdapter playlistAdapter;

    public CurrentlyPlayingChangeListener(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
