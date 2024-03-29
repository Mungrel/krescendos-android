package com.krescendos.player;


import android.util.Log;

import com.krescendos.firebase.FirebaseManager;
import com.krescendos.model.PartyState;
import com.krescendos.model.PlaybackState;
import com.krescendos.model.Track;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

// Spotify player wrapper to make things easier

public class TrackPlayer {

    private Track currentlyPlaying;
    private SpotifyPlayer spotifyPlayer;
    private boolean isPlaying;
    private String partyId;
    private boolean isDragging;

    public TrackPlayer(final SpotifyPlayer spotifyPlayer, final String partyId) {
        this.spotifyPlayer = spotifyPlayer;
        this.isPlaying = false;
        this.partyId = partyId;
        this.isDragging = false;
        this.currentlyPlaying = null;

        addNotificationListener();
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
                FirebaseManager.updatePlayState(partyId, getState());
            }

            @Override
            public void onError(Error error) {
                Log.d("SPOTIFY_ERROR", "Failed to play track:" + error.toString());
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
                FirebaseManager.updatePlayState(partyId, getState());
            }

            @Override
            public void onError(Error error) {
            }
        }, newPos);

    }

    public void pause() {
        spotifyPlayer.pause(null);
        isPlaying = false;
        FirebaseManager.updatePlayState(partyId, getState());
    }

    private void resume() {
        spotifyPlayer.resume(null);
        isPlaying = true;
        FirebaseManager.updatePlayState(partyId, getState());
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
        FirebaseManager.updatePlayState(partyId, getState());
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

    private void addNotificationListener() {
        this.spotifyPlayer.addNotificationCallback(new Player.NotificationCallback() {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent) {
                if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackDelivered) {
                    FirebaseManager.advancePlayhead(partyId);
                    FirebaseManager.updatePlayState(partyId, getState());
                }
            }

            @Override
            public void onPlaybackError(Error error) {
                Log.d("PLAYBACK_ERROR", "" + error.toString());
            }
        });
    }
}
