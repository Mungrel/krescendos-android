package com.krescendos.firebase.transactions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;

import java.util.Map;

public class RemoveItem implements Transaction.Handler {

    private String itemID;

    public RemoveItem(String itemID) {
        this.itemID = itemID;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        GenericTypeIndicator<Map<String, VoteItem<Track>>> type = new GenericTypeIndicator<Map<String, VoteItem<Track>>>() {
        };
        Map<String, VoteItem<Track>> map = mutableData.getValue(type);

        if (map.containsKey(itemID)) {
            map.remove(itemID);
        }

        mutableData.setValue(map);

        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
