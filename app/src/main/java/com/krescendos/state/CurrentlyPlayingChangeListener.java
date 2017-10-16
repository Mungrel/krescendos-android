package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.UpNextAdapter;

public class CurrentlyPlayingChangeListener implements ValueEventListener {

    private UpNextAdapter upNextAdapter;

    public CurrentlyPlayingChangeListener(UpNextAdapter upNextAdapter) {
        this.upNextAdapter = upNextAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
