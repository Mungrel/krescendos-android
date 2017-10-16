package com.krescendos.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRefs {
    private static final DatabaseReference ROOT = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getVoteCountRef(String partyId, String itemKey) {
        return ROOT.child("party").child(partyId).child("playlist").child(itemKey).child("voteCount");
    }
}
