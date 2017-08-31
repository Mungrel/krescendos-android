package com.krescendos.player;


import android.content.Context;
import android.util.Log;

import com.krescendos.domain.PartyState;
import com.krescendos.domain.PlaybackState;
import com.krescendos.domain.Track;
import com.krescendos.web.Requester;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;

// Spotify player wrapper to make things easier

public class TrackPlayer {

    private List<Track> trackList;
    private int pos;
    private SpotifyPlayer spotifyPlayer;
    private boolean isPlaying;
    private OnTrackChangeListener onTrackChangeListener;
    private Requester requester;
    private String partyId;
    private boolean isDragging;

    public TrackPlayer(final SpotifyPlayer spotifyPlayer, Context context, final String partyId) {
        this.spotifyPlayer = spotifyPlayer;
        this.trackList = new ArrayList<Track>();
        this.pos = 0;
        this.isPlaying = false;
        this.requester = Requester.getInstance(context);
        this.partyId = partyId;
        this.isDragging = false;

        this.spotifyPlayer.addNotificationCallback(new com.spotify.sdk.android.player.Player.NotificationCallback() {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent) {
                if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackDelivered) {
                    pos++;
                    if (pos == trackList.size()) {
                        pos = 0;
                    }
                    playTrack(trackList.get(pos));
                    requester.advancePlayhead(partyId);
                } else if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged) {
                    Log.d("TRACK_CHANGE", "Changed, new Pos: "+pos);
                    onTrackChangeListener.onTrackChange(trackList.get(pos));
                    requester.advancePlayhead(partyId, pos);
                }
            }

            @Override
            public void onPlaybackError(Error error) {
            }
        });
    }

    public void setOnTrackChangeListener(OnTrackChangeListener onTrackChangeListener) {
        this.onTrackChangeListener = onTrackChangeListener;
    }

    private void playTrack(Track track) {
        Log.d("PLAYING", track.getName());
        spotifyPlayer.playUri(null, track.getTrackURI(), 0, 0);
        isPlaying = true;
        requester.updatePlayState(partyId, getState());
    }

    private boolean trackLoaded() {
        return ((spotifyPlayer != null) && (spotifyPlayer.getMetadata() != null) &&
                (spotifyPlayer.getPlaybackState() != null) &&
                (spotifyPlayer.getMetadata().currentTrack != null) && (trackList.size() > 0));
    }

    public void seekTo(int newPos) {
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
        if (trackList.size() == 1 && !trackLoaded()){
            // First track added
            playTrack(trackList.get(getCurrentPos()));
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

    public void previous() {
        pos--;
        if (pos < 0) {
            pos = trackList.size() - 1;
        }
        if (isPlaying) {
            playTrack(trackList.get(pos));
        }
        requester.updatePlayState(partyId, getState());
    }

    public void next() {
        pos++;
        if (pos == trackList.size()) {
            pos = 0;
        }
        if (isPlaying) {
            playTrack(trackList.get(pos));
        }
        requester.updatePlayState(partyId, getState());
    }

    public void skipTo(int newPos) {
        Log.d("SKIPTO: ", "" + newPos);
        if (newPos == pos) {
            return;
        }
        if (newPos >= 0 && newPos < trackList.size()) {
            pos = newPos;
        } else {
            Log.d("ERROR:", "Invalid skip to position, playlist length: " + trackList.size() + " pos: " + newPos);
            return;
        }
        playTrack(trackList.get(pos));
        requester.advancePlayhead(partyId, pos);
        requester.updatePlayState(partyId, getState());
    }

    public Track getCurrentlyPlaying() {
        return trackList.get(pos);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        if (trackLoaded() && spotifyPlayer.getPlaybackState().positionMs != 0) {
            resume();
        } else {
            if (!trackList.isEmpty()) {
                playTrack(trackList.get(pos));
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
            return spotifyPlayer.getMetadata().currentTrack.durationMs;
        } else {
            return 0;
        }
    }

    public int getCurrentPos() {
        return pos;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
