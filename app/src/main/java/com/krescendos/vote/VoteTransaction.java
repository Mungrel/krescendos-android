package com.krescendos.vote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class VoteTransaction implements Transaction.Handler {

    private VoteDirection direction;
    private boolean alternateWasActive;

    public VoteTransaction(VoteDirection direction, boolean alternateWasActive) {
        this.direction = direction;
        this.alternateWasActive = alternateWasActive;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Long voteCount = mutableData.getValue(Long.class);
        if (voteCount == null) {
            return Transaction.success(mutableData);
        }

        int delta = (alternateWasActive) ? 2 : 1;

        if (direction == VoteDirection.UP) {
            voteCount -= delta;
        } else if (direction == VoteDirection.DOWN) {
            voteCount += delta;
        }

        mutableData.setValue(voteCount);
        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
