package com.krescendos.player;

import android.content.Context;
import android.util.Log;

import com.krescendos.web.Requester;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;

public class AutoNextListener implements Player.NotificationCallback {

    private Requester requester;
    private TrackPlayer trackPlayer;
    private String partyCode;

    public AutoNextListener(Requester requester, TrackPlayer trackPlayer, String partyCode) {
        this.requester = requester;
        this.trackPlayer = trackPlayer;
        this.partyCode = partyCode;
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackDelivered) {
            requester.advancePlayhead(partyCode);
            requester.updatePlayState(partyCode, trackPlayer.getState());
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("PLAYBACK_ERROR", ""+error.toString());
    }
}
