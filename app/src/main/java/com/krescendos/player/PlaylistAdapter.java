package com.krescendos.player;

import android.content.Context;
import android.widget.LinearLayout;

import com.krescendos.model.Track;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Handles all the GUI stuff for the up-next list and current track
 */
public class PlaylistAdapter {

    private Queue<Track> tracks;
    private Track currentTrack;
    private Context context;
    private LinearLayout upNextLayout;
    private LinearLayout currentTrackLayout;

    public PlaylistAdapter(Context context, LinearLayout upNextLayout, LinearLayout currentTrackLayout) {
        this.tracks = new LinkedList<>();
        this.context = context;
        this.upNextLayout = upNextLayout;
        this.currentTrackLayout = currentTrackLayout;
    }

    public void appendTrack(Track track) {
        tracks.add(track);
    }

    public void poll() {
        currentTrack = tracks.poll();
        updateCurrentTrackLayout();
    }

    private void updateCurrentTrackLayout() {

    }

}
