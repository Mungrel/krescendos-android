package com.krescendos.player;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.krescendos.R;
import com.krescendos.input.DislikeButtonClickListener;
import com.krescendos.input.LikeButtonClickListener;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.Requester;


/**
 * Handles all the GUI stuff for the up-next list and current track
 */
public class UpNextAdapter {

    private String partyId;
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout upNextLayout;

    public UpNextAdapter(Context context, LinearLayout upNextLayout, String partyId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.upNextLayout = upNextLayout;
        this.partyId = partyId;
    }

    public void removeItem(int index) {
        upNextLayout.removeViewAt(index);
    }

    public void moveItem(int oldIndex, int newIndex) {
        Log.d("MOVING_ITEM", "From: " + oldIndex + " To: " + newIndex);
        if (oldIndex == newIndex) {
            return;
        }

        RelativeLayout item = (RelativeLayout) upNextLayout.getChildAt(oldIndex);
        upNextLayout.removeViewAt(oldIndex);
        upNextLayout.addView(item, newIndex);
    }

    public void insertItem(int index, VoteItem<Track> item) {
        Track track = item.getItem();

        RelativeLayout listItem = (RelativeLayout) inflater.inflate(R.layout.player_list_layout, null, false);

        listItem.setOnLongClickListener(new LongClickRemoveListener(partyId, item.getItemId()));
        
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

    public void setItemVoteCount(int index, long voteCount) {
        RelativeLayout item = (RelativeLayout) upNextLayout.getChildAt(index);
        LinearLayout likeDislikeLayout = item.findViewById(R.id.like_dislike_layout);
        TextView voteCountView = likeDislikeLayout.findViewById(R.id.vote_count);

        String voteCountText = "" + voteCount;
        if (voteCount > 0) {
            voteCountText = "+" + voteCountText;
        }

        voteCountView.setText(voteCountText);

        int colour = ContextCompat.getColor(context, R.color.colorNeutralVotes);
        if (voteCount > 0) {
            colour = ContextCompat.getColor(context, R.color.colorPositiveVotes);
        } else if (voteCount < 0) {
            colour = ContextCompat.getColor(context, R.color.colorNegativeVotes);
        }

        voteCountView.setTextColor(colour);
    }

}
