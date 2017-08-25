package com.krescendos.web;

import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.domain.PartyState;

public class PartyStateChangeListener implements ValueEventListener {

    private LinearLayout layout;

    public PartyStateChangeListener(LinearLayout layout){
        this.layout = layout;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PartyState partyState = dataSnapshot.getValue(PartyState.class);


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
