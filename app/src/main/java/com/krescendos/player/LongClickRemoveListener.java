package com.krescendos.player;

import android.view.View;

import com.krescendos.firebase.FirebaseManager;
import com.krescendos.firebase.FirebaseRefs;

public class LongClickRemoveListener implements View.OnLongClickListener {

    private String partyID;
    private String itemID;

    public LongClickRemoveListener(String partyID, String itemID) {
        this.partyID = partyID;
        this.itemID = itemID;
    }

    @Override
    public boolean onLongClick(View view) {

        FirebaseManager.removeItem(partyID, itemID);
        return true;
    }
}
