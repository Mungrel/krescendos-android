package com.krescendos;


import android.util.Log;
import android.widget.SeekBar;

import com.krescendos.domain.Track;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// Spotify player wrapper to make things easier

public class TrackPlayer {

    private List<Track> trackList;
    private int pos;
    private SpotifyPlayer spotifyPlayer;
    private boolean isPlaying;
    private SeekBar seekBar;

    public TrackPlayer(final SpotifyPlayer spotifyPlayer, final SeekBar seekBar){
        this.spotifyPlayer = spotifyPlayer;
        this.trackList = new ArrayList<Track>();
        this.pos = 0;
        this.isPlaying = false;
        this.seekBar = seekBar;

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

        seekBar.setMax(100);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (trackLoaded()){
                    double trackDur = (double)spotifyPlayer.getMetadata().currentTrack.durationMs;
                    double currentPos = (double)spotifyPlayer.getPlaybackState().positionMs;
                    double prog = (currentPos/trackDur) * 100;
                    int progressPercent = (int)Math.round(prog);
                    seekBar.setProgress(progressPercent);
                }
            }
        }, 0, 400);
    }

    private void playTrack(Track track){
        Log.d("PLAYING", track.getName());
        spotifyPlayer.playUri(null, track.getTrackURI(), 0, 0);
        isPlaying = true;

    }

    private boolean trackLoaded(){
        return ((spotifyPlayer != null) && (spotifyPlayer.getMetadata() != null) &&
                (spotifyPlayer.getPlaybackState() != null) &&
                (spotifyPlayer.getMetadata().currentTrack != null));
    }

    public void seekTo(int percent){
        if (!trackLoaded()){
            return;
        }
        double trackDur = spotifyPlayer.getMetadata().currentTrack.durationMs;
        double onePercent = trackDur/100;
        int newPos = (int)Math.round(onePercent*percent);
        spotifyPlayer.seekToPosition(null, newPos);
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
