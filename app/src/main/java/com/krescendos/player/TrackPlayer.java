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

    private Queue<Track> trackList;
    private Track currentlyPlaying;
    private int pos;
    private SpotifyPlayer spotifyPlayer;
    private boolean isPlaying;
    private Requester requester;
    private String partyId;
    private boolean isDragging;

    public TrackPlayer(final SpotifyPlayer spotifyPlayer, Context context, final String partyId) {
        this.spotifyPlayer = spotifyPlayer;
        this.trackList = new LinkedList<>();
        this.pos = 0;
        this.isPlaying = false;
        this.requester = Requester.getInstance(context);
        this.partyId = partyId;
        this.isDragging = false;

        this.spotifyPlayer.addNotificationCallback(new com.spotify.sdk.android.player.Player.NotificationCallback() {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent) {
                if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackDelivered) {
                    next();
                } else if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged) {
                    Log.d("TRACK_CHANGE", "Changed, new Pos: " + pos);
                    requester.advancePlayhead(partyId, pos);
                }
            }

            @Override
            public void onPlaybackError(Error error) {
            }
        });
    }

    private void playTrack(Track track) {
        Log.d("PLAYING", track.getName());
        spotifyPlayer.playUri(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                isPlaying = true;
                requester.updatePlayState(partyId, getState());
            }

            @Override
            public void onError(Error error) {

            }
        }, track.getTrackURI(), 0, 0);
    }

    private boolean trackLoaded() {
        return (currentlyPlaying != null);
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

    public void queue(Track track) {
        trackList.add(track);
        if (trackList.size() == 1 && !trackLoaded()) {
            // First track added
            currentlyPlaying = trackList.poll();
            playTrack(currentlyPlaying);
        }
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

    public void next() {
        if (pos + 1 < trackList.size()) {
            // Not last track in list
            Log.d("LAST", "last track state");
            currentlyPlaying = trackList.poll();
            playTrack(currentlyPlaying);
            requester.advancePlayhead(partyId);
            requester.updatePlayState(partyId, getState());
        } else {
            // Last track in list
            spotifyPlayer.skipToNext(new Player.OperationCallback() {
                @Override
                public void onSuccess() {
                    isPlaying = false;
                    requester.updatePlayState(partyId, getState());
                }

                @Override
                public void onError(Error error) {
                }
            });
        }

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        if (trackLoaded() && spotifyPlayer.getPlaybackState().positionMs != 0) {
            resume();
        } else {
            if (!trackList.isEmpty()) {
                playTrack(currentlyPlaying);
            }
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
        if (trackLoaded()) {
            return currentlyPlaying.getDuration_ms();
        } else {
            return 0;
        }
    }

    public boolean isDragging() {
        return isDragging;
    }

    void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
