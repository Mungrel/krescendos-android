package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.UpNextAdapater;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private UpNextAdapater upNextAdapater;
    private Integer previousPos;

    public PlayheadIndexChangeListener(UpNextAdapater upNextAdapater) {
        this.upNextAdapater = upNextAdapater;
        this.previousPos = null;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Integer newPos = dataSnapshot.getValue(Integer.class);

        if (previousPos == null) {
            previousPos = newPos;
            return;
        }

        upNextAdapater.poll();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
