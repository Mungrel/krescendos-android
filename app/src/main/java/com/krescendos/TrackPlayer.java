package com.krescendos;


import android.util.Log;

import com.krescendos.domain.Track;
import com.spotify.sdk.android.player.Error;
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

    public TrackPlayer(final SpotifyPlayer spotifyPlayer){
        this.spotifyPlayer = spotifyPlayer;
        this.trackList = new ArrayList<Track>();
        this.pos = 0;
        this.isPlaying = false;

        this.spotifyPlayer.addNotificationCallback(new com.spotify.sdk.android.player.Player.NotificationCallback() {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent) {
                if (playerEvent == PlayerEvent.kSpPlaybackNotifyTrackDelivered){
                    pos++;
                    if (pos == trackList.size()){
                        pos = 0;
                    }
                    playTrack(trackList.get(pos));
                }
            }

            @Override
            public void onPlaybackError(Error error) {}
        });
    }

    private void playTrack(Track track){
        Log.d("PLAYING", track.getName());
        spotifyPlayer.playUri(null, track.getTrackURI(), 0, 0);
        isPlaying = true;
    }

    public void queue(Track track){
        trackList.add(track);
    }

    public void pause(){
        spotifyPlayer.pause(null);
        isPlaying = false;
    }

    private void resume(){
        spotifyPlayer.resume(null);
        isPlaying = true;
    }

    public void previous(){
        pos--;
        if (pos < 0){
            pos = trackList.size()-1;
        }
        if (isPlaying){
            playTrack(trackList.get(pos));
        }
    }

    public void next(){
        pos++;
        if (pos == trackList.size()){
            pos = 0;
        }
        if (isPlaying){
            playTrack(trackList.get(pos));
        }
    }

    public void skipTo(int newPos){
        Log.d("SKIPTO: ", ""+newPos);
        if (newPos == pos){
            return;
        }
        if (newPos >= 0 && newPos < trackList.size()){
            pos = newPos;
        } else {
            Log.d("ERROR:", "Invalid skip to position, playlist length: "+trackList.size()+" pos: "+newPos);
            return;
        }
        playTrack(trackList.get(pos));
    }

    public Track getCurrentlyPlaying(){
        return trackList.get(pos);
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void play(){
        if (spotifyPlayer.getPlaybackState().positionMs != 0){
            resume();
        } else {
            playTrack(trackList.get(pos));
        }
    }
}
