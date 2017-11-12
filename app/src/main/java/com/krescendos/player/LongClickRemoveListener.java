package com.krescendos.player;

import android.content.Context;
import android.view.View;

import com.krescendos.dialog.OnYesListener;
import com.krescendos.dialog.RemoveTrackDialog;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.firebase.FirebaseRefs;

public class LongClickRemoveListener implements View.OnLongClickListener {

    private Context context;
    private String partyID;
    private String itemID;

    public LongClickRemoveListener(Context context, String partyID, String itemID) {
        this.context = context;
        this.partyID = partyID;
        this.itemID = itemID;
    }

    @Override
    public boolean onLongClick(View view) {
        RemoveTrackDialog dialog = new RemoveTrackDialog(context, new OnYesListener() {
            @Override
            public void onYes() {
                FirebaseManager.removeItem(partyID, itemID);
            }
        });

        dialog.show();

        return true;
    }
}
