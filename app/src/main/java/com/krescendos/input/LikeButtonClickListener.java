package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.model.Track;
import com.krescendos.model.VoteDirection;
import com.krescendos.model.VoteItem;

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
        boolean dislikeWasActive = dislikeButton.getTag().equals("on");
        toggleState();
        boolean off = likeButton.getTag().equals("off");
        VoteDirection direction = (off) ? VoteDirection.DOWN : VoteDirection.UP;
        FirebaseManager.vote(partyId, item.getItemId(), direction, dislikeWasActive);
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
