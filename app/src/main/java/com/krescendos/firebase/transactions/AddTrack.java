package com.krescendos.firebase.transactions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;

import java.util.ArrayList;
import java.util.List;

public class AddTrack implements Transaction.Handler {

    private Track track;

    public AddTrack(Track track) {
        this.track = track;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        GenericTypeIndicator<List<VoteItem<Track>>> type = new GenericTypeIndicator<List<VoteItem<Track>>>() {};
        List<VoteItem<Track>> tracks = mutableData.getValue(type);

        if (tracks == null) {
            tracks = new ArrayList<>();
        }


        tracks.add(new VoteItem<Track>(track));

        mutableData.setValue(tracks);
        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
