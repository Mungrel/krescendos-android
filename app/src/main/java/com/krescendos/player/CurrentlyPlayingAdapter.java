package com.krescendos.player;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.Requester;

public class CurrentlyPlayingAdapter {

    private Context context;
    private VoteItem<Track> currentlyPlaying;

    private TextView trackTitle;
    private TextView artistAlbum;
    private NetworkImageView albumArt;
    private SeekBar seekBar;

    public CurrentlyPlayingAdapter(Context context, LinearLayout currentTrackLayout, SeekBar seekBar) {
        this.context = context;
        this.currentlyPlaying = null;

        this.trackTitle = currentTrackLayout.findViewById(R.id.current_track_title);
        this.artistAlbum = currentTrackLayout.findViewById(R.id.current_track_artist_album);
        this.albumArt = currentTrackLayout.findViewById(R.id.current_track_album_art);
        this.seekBar = seekBar;
    }

    public void setCurrentlyPlaying(VoteItem<Track> currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
        updateUI();
    }

    public VoteItem<Track> getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    private void updateUI() {
        Track currentTrack = currentlyPlaying.getItem();

        trackTitle.setText(currentTrack.getName());
        artistAlbum.setText(TextUtils.join(currentTrack.getArtists()) + " - " + currentTrack.getAlbum().getName());
        albumArt.setImageUrl(currentTrack.getAlbum().getLargestImage().getUrl(), Requester.getInstance(context).getImageLoader());

        seekBar.setMax((int) currentTrack.getDuration_ms());
    }
}
