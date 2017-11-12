package com.krescendos.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.firebase.transactions.PollQueue;
import com.krescendos.firebase.transactions.RemoveItem;
import com.krescendos.firebase.transactions.UpdateState;
import com.krescendos.firebase.transactions.Vote;
import com.krescendos.model.PartyState;
import com.krescendos.model.Track;
import com.krescendos.model.VoteDirection;
import com.krescendos.model.VoteItem;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueue(currentlyPlayingRef));
    }

    public static void updatePlayState(String partyId, PartyState state) {
        DatabaseReference playStateRef = FirebaseRefs.getPartyStateRef(partyId);
        playStateRef.runTransaction(new UpdateState(state));
    }

    public static void addTrack(final String partyId, Track track) {
        DatabaseReference playlistRef = FirebaseRefs.getPlaylistRef(partyId);
        DatabaseReference newItemRef = playlistRef.push();
        newItemRef.setValue(new VoteItem<Track>(newItemRef.getKey(), track));

        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        currentlyPlayingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("REF", "" + dataSnapshot.toString());
                if (dataSnapshot.getValue() == null) {
                    FirebaseManager.advancePlayhead(partyId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void vote(String partyId, String itemId, VoteDirection direction, boolean alternateWasActive) {
        DatabaseReference voteCountRef = FirebaseRefs.getVoteCountRef(partyId, itemId);
        voteCountRef.runTransaction(new Vote(direction, alternateWasActive));
    }

    public static void removeItem(String partyId, String itemId) {
        DatabaseReference playlistRef = FirebaseRefs.getPlaylistRef(partyId);
        playlistRef.runTransaction(new RemoveItem(itemId));
    }
}
