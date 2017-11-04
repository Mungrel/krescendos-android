package com.krescendos.player;


import android.content.Context;
import android.util.Log;

import com.krescendos.model.PartyState;
import com.krescendos.model.PlaybackState;
import com.krescendos.model.Track;
import com.krescendos.web.Requester;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Spotify player wrapper to make things easier

public class TrackPlayer {

    private Track currentlyPlaying;
    private SpotifyPlayer spotifyPlayer;
    private boolean isPlaying;
    private Requester requester;
    private String partyId;
    private boolean isDragging;

    public TrackPlayer(final SpotifyPlayer spotifyPlayer, Context context, final String partyId) {
        this.spotifyPlayer = spotifyPlayer;
        this.isPlaying = false;
        this.requester = Requester.getInstance(context);
        this.partyId = partyId;
        this.isDragging = false;
        this.currentlyPlaying = null;

        this.spotifyPlayer.addNotificationCallback(new AutoNextListener(context, partyId));
    }

    public void setCurrentlyPlaying(Track currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
        playTrack(currentlyPlaying);
    }

    private void playTrack(final Track track) {
        Log.d("PLAYING", track.getName());
        spotifyPlayer.playUri(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                isPlaying = true;
                currentlyPlaying = track;
                requester.updatePlayState(partyId, getState());
            }

            @Override
            public void onError(Error error) {
                Log.d("SPOTIFY_ERROR", "Failed to play track:" +error.toString());
            }
        }, track.getTrackURI(), 0, 0);
    }

    private boolean trackLoaded() {
        return currentlyPlaying != null;
    }

    void seekTo(int newPos) {
        if (!trackLoaded()) {
            return;
        }
        spotifyPlayer.seekToPosition(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                requester.updatePlayState(partyId, getState());
            }

            @Override
            public void onError(Error error) {
            }
        }, newPos);

    }

    public void pause() {
        spotifyPlayer.pause(null);
        isPlaying = false;
        requester.updatePlayState(partyId, getState());
    }

    private void resume() {
        spotifyPlayer.resume(null);
        isPlaying = true;
        requester.updatePlayState(partyId, getState());
    }

    public PartyState getState() {
        PlaybackState state = isPlaying() ? PlaybackState.PLAY : PlaybackState.PAUSE;
        long trackPos = getCurrentTrackTime();
        return new PartyState(state, trackPos);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        if (trackLoaded() && spotifyPlayer.getPlaybackState().positionMs != 0) {
            resume();
        } else {
            playTrack(currentlyPlaying);
        }
        requester.updatePlayState(partyId, getState());
    }

    public long getCurrentTrackTime() {
        if (trackLoaded()) {
            return spotifyPlayer.getPlaybackState().positionMs;
        } else {
            return 0;
        }
    }

    public long getCurrentTrackLength() {
        return (currentlyPlaying == null) ? 0 : currentlyPlaying.getDuration_ms();
    }

    public boolean isDragging() {
        return isDragging;
    }

    void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
