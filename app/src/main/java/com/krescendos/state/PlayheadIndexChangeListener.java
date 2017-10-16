package com.krescendos.state;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.player.UpNextAdapter;

public class PlayheadIndexChangeListener implements ValueEventListener {

    private UpNextAdapter upNextAdapter;
    private Integer previousPos;

    public PlayheadIndexChangeListener(UpNextAdapter upNextAdapter) {
        this.upNextAdapter = upNextAdapter;
        this.previousPos = null;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Integer newPos = dataSnapshot.getValue(Integer.class);

        if (previousPos == null) {
            previousPos = newPos;
            return;
        }

        upNextAdapter.poll();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
