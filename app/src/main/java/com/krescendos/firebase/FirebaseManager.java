package com.krescendos.firebase;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.firebase.transactions.AddTrack;
import com.krescendos.firebase.transactions.PollQueue;
import com.krescendos.firebase.transactions.UpdateState;
import com.krescendos.model.PartyState;
import com.krescendos.model.Track;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueue(currentlyPlayingRef));
    }

    public static void updatePlayState(String partyId, PartyState state) {
        DatabaseReference playStateRef = FirebaseRefs.getPartyStateRef(partyId);
        playStateRef.runTransaction(new UpdateState(state));
    }

    public static void addTrack(String partyId, Track track) {
        DatabaseReference playlistRef = FirebaseRefs.getPlaylistRef(partyId);
        playlistRef.runTransaction(new AddTrack(track));
    }
}
