package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.model.Track;
import com.krescendos.model.VoteDirection;
import com.krescendos.model.VoteItem;

public class DislikeButtonClickListener implements View.OnClickListener {

    private ImageButton dislikeButton;
    private ImageButton likeButton;

    private String partyId;
    private VoteItem<Track> item;

    public DislikeButtonClickListener(ImageButton dislikeButton, ImageButton likeButton, String partyId, VoteItem<Track> item) {
        this.dislikeButton = dislikeButton;
        this.likeButton = likeButton;
        this.dislikeButton.setTag("off");
        this.likeButton.setTag("off");
        this.partyId = partyId;
        this.item = item;
    }

    @Override
    public void onClick(View view) {
        boolean likeWasActive = likeButton.getTag().equals("on");
        toggleState();
        boolean off = dislikeButton.getTag().equals("off");
        VoteDirection direction = (off) ? VoteDirection.UP : VoteDirection.DOWN;
        FirebaseManager.vote(partyId, item.getItemId(), direction, likeWasActive);
        updateImage();
    }

    private void updateImage() {
        boolean off = dislikeButton.getTag().equals("off");
        if (off) {
            dislikeButton.setImageResource(R.drawable.dislike_off);
        } else {
            dislikeButton.setImageResource(R.drawable.dislike_on);
            likeButton.setImageResource(R.drawable.like_off);
        }
    }

    private void toggleState() {
        boolean off = dislikeButton.getTag().equals("off");
        if (off) {
            dislikeButton.setTag("on");
            likeButton.setTag("off");
        } else {
            dislikeButton.setTag("off");
        }
    }
}
