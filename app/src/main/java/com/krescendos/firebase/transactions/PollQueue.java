package com.krescendos.firebase.transactions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PollQueue implements Transaction.Handler {

    private DatabaseReference currentlyPlayingRef;

    public PollQueue(DatabaseReference currentlyPlayingRef) {
        this.currentlyPlayingRef = currentlyPlayingRef;
    }

    @Override
    public Transaction.Result doTransaction(MutableData mutableData) {

        GenericTypeIndicator<Map<String, VoteItem<Track>>> type = new GenericTypeIndicator<Map<String, VoteItem<Track>>>() {
        };
        Map<String, VoteItem<Track>> map = mutableData.getValue(type);
        List<VoteItem<Track>> tracks = new ArrayList<>(map.values());

        if (tracks.isEmpty()) {
            return Transaction.success(mutableData);
        }

        Collections.sort(tracks, new Comparator<VoteItem<Track>>() {
            @Override
            public int compare(VoteItem<Track> item1, VoteItem<Track> item2) {
                int c = item1.getVoteCount().compareTo(item2.getVoteCount());
                if (c == 0) {
                    c = item1.getItemId().compareTo(item2.getItemId());
                }
                return c;
            }
        });


        VoteItem<Track> topTrack = tracks.get(0);
        currentlyPlayingRef.setValue(topTrack);
        map.remove(topTrack.getItemId());

        mutableData.setValue(map);

        return Transaction.success(mutableData);
    }

    @Override
    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

    }
}
