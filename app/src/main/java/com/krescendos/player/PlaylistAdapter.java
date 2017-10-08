package com.krescendos.player;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.text.TextUtils;
import com.krescendos.web.Requester;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Handles all the GUI stuff for the up-next list and current track
 */
public class PlaylistAdapter {

    private Queue<Track> tracks;
    private Track currentTrack;
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout upNextLayout;
    private LinearLayout currentTrackLayout;

    public PlaylistAdapter(Context context, LinearLayout upNextLayout, LinearLayout currentTrackLayout) {
        this.tracks = new LinkedList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.upNextLayout = upNextLayout;
        this.currentTrackLayout = currentTrackLayout;
    }

    public void appendTrack(Track track) {
        tracks.add(track);
        appendUpNextLayout(track);
    }

    public void poll() {
        currentTrack = tracks.poll();
        updateCurrentTrackLayout();
    }

    private void updateCurrentTrackLayout() {
        TextView trackTitle = currentTrackLayout.findViewById(R.id.current_track_title);
        TextView artistAlbum = currentTrackLayout.findViewById(R.id.current_track_artist_album);
        NetworkImageView albumArt = currentTrackLayout.findViewById(R.id.current_track_album_art);

        trackTitle.setText(currentTrack.getName());
        artistAlbum.setText(TextUtils.join(currentTrack.getArtists()) + " - " + currentTrack.getAlbum().getName());
        albumArt.setImageUrl(currentTrack.getAlbum().getLargestImage().getUrl(), Requester.getInstance(context).getImageLoader());
    }

    private void appendUpNextLayout(Track appendedTrack) {
        LinearLayout listItem = (LinearLayout) inflater.inflate(R.layout.player_list_layout, upNextLayout);

        TextView trackName = listItem.findViewById(R.id.up_next_track_name);
        TextView artistAlbum = listItem.findViewById(R.id.up_next_artist_album);
        NetworkImageView albumArt = listItem.findViewById(R.id.up_next_album_art);

        trackName.setText(appendedTrack.getName());
        artistAlbum.setText(TextUtils.join(appendedTrack.getArtists()));
        albumArt.setImageUrl(appendedTrack.getAlbum().getSmallestImage().getUrl(), Requester.getInstance(context).getImageLoader());

        upNextLayout.addView(listItem);
    }

}
