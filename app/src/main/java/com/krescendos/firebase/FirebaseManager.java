package com.krescendos.firebase;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.krescendos.firebase.FirebaseRefs;
import com.krescendos.firebase.transactions.PollQueueTransaction;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FirebaseManager {

    public static void advancePlayhead(String partyId) {
        DatabaseReference currentlyPlayingRef = FirebaseRefs.getCurrentlyPlayingRef(partyId);
        FirebaseRefs.getPlaylistRef(partyId).runTransaction(new PollQueueTransaction(currentlyPlayingRef));
    }

}
