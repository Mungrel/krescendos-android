package com.krescendos.firebase.transactions;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PollQueue implements Transaction.Handler{

    private DatabaseReference currentlyPlayingRef;

    public PollQueue(DatabaseReference currentlyPlayingRef) {
        this.currentlyPlayingRef = currentlyPlayingRef;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {
        GenericTypeIndicator<List<VoteItem<Track>>> type = new GenericTypeIndicator<List<VoteItem<Track>>>() {};
        List<VoteItem<Track>> tracks = mutableData.getValue(type);

        if (tracks.isEmpty()) {
            return Transaction.success(mutableData);
        }

        Collections.sort(tracks, new Comparator<VoteItem<Track>>() {
            @Override
            public int compare(VoteItem<Track> item1, VoteItem<Track> item2) {
                return item1.getVoteCount().compareTo(item2.getVoteCount());
            }
        });


        VoteItem<Track> topTrack = tracks.get(0);
        currentlyPlayingRef.setValue(topTrack);
        tracks.remove(0);

        mutableData.setValue(tracks);

        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
