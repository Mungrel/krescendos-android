package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.UpNextAdapater;

public class CurrentlyPlayingChangeListener implements ValueEventListener {

    private UpNextAdapater upNextAdapater;

    public CurrentlyPlayingChangeListener(UpNextAdapater upNextAdapater) {
        this.upNextAdapater = upNextAdapater;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
