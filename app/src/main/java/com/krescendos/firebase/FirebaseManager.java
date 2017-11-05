package com.krescendos.firebase;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.firebase.transactions.PollQueueTransaction;
import com.krescendos.firebase.transactions.UpdateStateTransaction;
import com.krescendos.model.PartyState;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueueTransaction(currentlyPlayingRef));
    }

    public static void updatePlayState(String partyId, PartyState state) {
        DatabaseReference playStateRef = FirebaseRefs.getPartyStateRef(partyId);
        playStateRef.runTransaction(new UpdateStateTransaction(state));
    }
}
