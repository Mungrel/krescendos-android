package com.krescendos.firebase;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRefs {
    private static final DatabaseReference ROOT = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getVoteCountRef(String partyId, String itemKey) {
        return getPartyRef(partyId).child("playlist").child(itemKey).child("voteCount");
    }

    public static DatabaseReference getCurrentlyPlayingRef(String partyId) {
        return getPartyRef(partyId).child("currentlyPlaying");
    }

    public static DatabaseReference getPartyStateRef(String partyId) {
        return getPartyRef(partyId).child("partyState");
    }

    private static DatabaseReference getPartyRef(String partyId) {
        return ROOT.child("party").child(partyId);
    }

}
