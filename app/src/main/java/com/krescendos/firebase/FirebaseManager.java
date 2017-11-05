package com.krescendos.firebase;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.firebase.transactions.PollQueueTransaction;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueueTransaction(currentlyPlayingRef));
    }

}
