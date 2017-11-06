package com.krescendos.firebase.transactions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.krescendos.model.PartyState;

public class UpdateState implements Transaction.Handler {

    private PartyState partyState;

    public UpdateState(PartyState partyState) {
        this.partyState = partyState;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        mutableData.setValue(partyState);
        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
