package com.krescendos.player;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.input.DislikeButtonClickListener;
import com.krescendos.input.LikeButtonClickListener;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.Requester;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Handles all the GUI stuff for the up-next list and current track
 */
public class PlaylistAdapter {

    private LinkedList<VoteItem<Track>> upNextTracks;
    private Track currentTrack;
    private String partyId;

    private Context context;
    private LayoutInflater inflater;
    private LinearLayout upNextLayout;
    private LinearLayout currentTrackLayout;
    private SeekBar seekBar;

    public PlaylistAdapter(Context context, LinearLayout upNextLayout, LinearLayout currentTrackLayout, SeekBar seekBar, String partyId) {
        this.upNextTracks = new LinkedList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.upNextLayout = upNextLayout;
        this.currentTrackLayout = currentTrackLayout;
        this.seekBar = seekBar;
        this.partyId = partyId;
    }

    public void insert(int index, VoteItem<Track> trackVoteItem) {
        if (currentTrack == null && upNextTracks.size() == 0) {
            currentTrack = trackVoteItem.getItem();
            updateCurrentTrackLayout();
        } else {
            if (index >= upNextTracks.size()) {
                upNextTracks.add(trackVoteItem);
            } else {
                upNextTracks.add(index, trackVoteItem);
            }

            insertUpNextLayout(index, trackVoteItem);
        }
    }

    public void poll() {
        currentTrack = upNextTracks.poll().getItem();
        updateCurrentTrackLayout();

        if (upNextLayout.getChildCount() > 0) {
            upNextLayout.removeViewAt(0);
        }
    }

    /*
    Basically, only way to move shit by index is to remove it and re-add it
    Doing shit by indexes is so mf prone to off-by-ones, Mark Wilson would have a field day

    Anyway, basically, if the new index is past the old index, when you remove the old view,
    you gotta minus 1 of the new index, because everything would've been shifted back 1 space.
    Vice-versa for if the new index is before the old index, you gotta add 1. (Or maybe you do nothing again)
    FUCK IDK

    Fuck knows what happens if they're equal. (Maybe do nothing?)
    Yeah. Do nothing.
     */
    public void moveItem(int oldIndex, int newIndex) {
        if (oldIndex == newIndex) {
            return;
        } else if (oldIndex < newIndex) {
            newIndex--;
        }

        RelativeLayout item = (RelativeLayout) upNextLayout.getChildAt(oldIndex);
        upNextLayout.removeViewAt(oldIndex);
        upNextLayout.addView(item, newIndex);
    }

    public Track getCurrentItem() {
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

    private void insertUpNextLayout(int index, VoteItem<Track> item) {
        Track track = item.getItem();

        RelativeLayout listItem = (RelativeLayout) inflater.inflate(R.layout.player_list_layout, null, false);

        TextView trackName = listItem.findViewById(R.id.up_next_track_name);
        TextView artistAlbum = listItem.findViewById(R.id.up_next_artist_album);
        NetworkImageView albumArt = listItem.findViewById(R.id.up_next_album_art);

        ImageButton likeButton = listItem.findViewById(R.id.like);
        ImageButton dislikeButton = listItem.findViewById(R.id.dislike);

        likeButton.setOnClickListener(new LikeButtonClickListener(likeButton, dislikeButton, partyId, item));
        dislikeButton.setOnClickListener(new DislikeButtonClickListener(dislikeButton, likeButton, partyId, item));

        trackName.setText(track.getName());
        artistAlbum.setText(TextUtils.join(track.getArtists()));
        albumArt.setImageUrl(track.getAlbum().getSmallestImage().getUrl(), Requester.getInstance(context).getImageLoader());

        if (index >= upNextLayout.getChildCount()) {
            upNextLayout.addView(listItem);
        } else {
            upNextLayout.addView(listItem, index);
        }

    }

}
