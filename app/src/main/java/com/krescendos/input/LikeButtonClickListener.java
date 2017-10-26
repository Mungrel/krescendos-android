package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.model.VoteItem;
import com.krescendos.utils.FirebaseRefs;
import com.krescendos.vote.VoteDirection;
import com.krescendos.vote.VoteTransaction;

public class LikeButtonClickListener implements View.OnClickListener {

    private ImageButton likeButton;
    private ImageButton dislikeButton;

    private String partyId;
    private VoteItem<Track> item;

    public LikeButtonClickListener(ImageButton likeButton, ImageButton dislikeButton, String partyId, VoteItem<Track> item) {
        this.likeButton = likeButton;
        this.dislikeButton = dislikeButton;
        this.likeButton.setTag("off");
        this.dislikeButton.setTag("off");
        this.partyId = partyId;
        this.item = item;
    }

    @Override
    public void onClick(View view) {
        toggleState();
        DatabaseReference voteCountRef = FirebaseRefs.getVoteCountRef(partyId, item.getDbKey());
        boolean off = likeButton.getTag().equals("off");
        VoteDirection direction = (off) ? VoteDirection.DOWN : VoteDirection.UP;
        voteCountRef.runTransaction(new VoteTransaction(direction));
        updateImage();
    }

    private void updateImage() {
        boolean off = likeButton.getTag().equals("off");
        if (off) {
            likeButton.setImageResource(R.drawable.like_off);
        } else {
            likeButton.setImageResource(R.drawable.like_on);
            dislikeButton.setImageResource(R.drawable.dislike_off);
        }
    }

    private void toggleState() {
        boolean off = likeButton.getTag().equals("off");
        if (off) {
            likeButton.setTag("on");
            dislikeButton.setTag("off");
        } else {
            likeButton.setTag("off");
        }
    }
}
