package com.krescendos.state;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.dialog.Toaster;

public class AllowSuggestionsChangeListener implements ValueEventListener {

    private Context context;
    private ImageButton addTrack;
    private View.OnClickListener baseListener;

    public AllowSuggestionsChangeListener(Context context, ImageButton addTrack, View.OnClickListener baseListener) {
        this.context = context;
        this.addTrack = addTrack;
        this.baseListener = baseListener;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        boolean allowSuggestions = dataSnapshot.getValue(Boolean.class);

        if (allowSuggestions) {
            addTrack.setOnClickListener(baseListener);
        } else {
            addTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = "This party is not currently accepting suggestions";
                    Toaster.showToast(context, text);
                }
            });
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
