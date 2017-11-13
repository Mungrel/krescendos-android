package com.krescendos.state;

import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AllowSuggestionsChangeListener implements ValueEventListener {

    private ImageButton addTrack;

    public AllowSuggestionsChangeListener(ImageButton addTrack) {
        this.addTrack = addTrack;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        boolean allowSuggestions = dataSnapshot.getValue(Boolean.class);

        addTrack.setEnabled(allowSuggestions);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
