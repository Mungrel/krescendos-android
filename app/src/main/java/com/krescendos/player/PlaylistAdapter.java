package com.krescendos.player;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.Requester;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Handles all the GUI stuff for the up-next list and current track
 */
public class PlaylistAdapter {

    private Queue<Track> upNextTracks;
    private Track currentTrack;
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout upNextLayout;
    private LinearLayout currentTrackLayout;
    private SeekBar seekBar;

    public PlaylistAdapter(Context context, LinearLayout upNextLayout, LinearLayout currentTrackLayout, SeekBar seekBar) {
        this.upNextTracks = new LinkedList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.upNextLayout = upNextLayout;
        this.currentTrackLayout = currentTrackLayout;
        this.seekBar = seekBar;
    }

    public void appendTrack(Track track) {
        if (currentTrack == null && upNextTracks.size() == 0) {
            currentTrack = track;
            updateCurrentTrackLayout();
        } else {
            upNextTracks.add(track);
            appendUpNextLayout(track);
        }
    }

    public void poll() {
        currentTrack = upNextTracks.poll();
        updateCurrentTrackLayout();

        if (upNextLayout.getChildCount() > 0) {
            upNextLayout.removeViewAt(0);
        }
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    private void updateCurrentTrackLayout() {
        TextView trackTitle = currentTrackLayout.findViewById(R.id.current_track_title);
        TextView artistAlbum = currentTrackLayout.findViewById(R.id.current_track_artist_album);
        NetworkImageView albumArt = currentTrackLayout.findViewById(R.id.current_track_album_art);

        trackTitle.setText(currentTrack.getName());
        artistAlbum.setText(TextUtils.join(currentTrack.getArtists()) + " - " + currentTrack.getAlbum().getName());
        albumArt.setImageUrl(currentTrack.getAlbum().getLargestImage().getUrl(), Requester.getInstance(context).getImageLoader());

        seekBar.setMax((int) currentTrack.getDuration_ms());
    }

    private void appendUpNextLayout(Track appendedTrack) {
        RelativeLayout listItem = (RelativeLayout) inflater.inflate(R.layout.player_list_layout, null, false);

        TextView trackName = listItem.findViewById(R.id.up_next_track_name);
        TextView artistAlbum = listItem.findViewById(R.id.up_next_artist_album);
        NetworkImageView albumArt = listItem.findViewById(R.id.up_next_album_art);

        trackName.setText(appendedTrack.getName());
        artistAlbum.setText(TextUtils.join(appendedTrack.getArtists()));
        albumArt.setImageUrl(appendedTrack.getAlbum().getSmallestImage().getUrl(), Requester.getInstance(context).getImageLoader());

        upNextLayout.addView(listItem);
    }

}
