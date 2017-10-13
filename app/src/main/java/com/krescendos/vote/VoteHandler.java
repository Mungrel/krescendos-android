package com.krescendos.vote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class VoteHandler implements Transaction.Handler {

    private VoteDirection direction;

    public VoteHandler(VoteDirection direction) {
        this.direction = direction;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        Long voteCount = mutableData.getValue(Long.class);
        if (voteCount == null) {
            return Transaction.success(mutableData);
        }

        if (direction == VoteDirection.UP) {
            voteCount += 1;
        } else if (direction == VoteDirection.DOWN){
            voteCount -= 1;
        }

        mutableData.setValue(voteCount);
        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
