package com.krescendos.firebase;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.firebase.transactions.AddTrackTransaction;
import com.krescendos.firebase.transactions.PollQueueTransaction;
import com.krescendos.firebase.transactions.UpdateStateTransaction;
import com.krescendos.model.PartyState;
import com.krescendos.model.Track;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueueTransaction(currentlyPlayingRef));
    }

    public static void updatePlayState(String partyId, PartyState state) {
        DatabaseReference playStateRef = FirebaseRefs.getPartyStateRef(partyId);
        playStateRef.runTransaction(new UpdateStateTransaction(state));
    }

    public static void addTrack(String partyId, Track track) {
        DatabaseReference playlistRef = FirebaseRefs.getPlaylistRef(partyId);
        playlistRef.runTransaction(new AddTrackTransaction(track));
    }
}
