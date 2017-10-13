package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;
import com.krescendos.vote.VoteHandler;

public class LikeButtonClickListener implements View.OnClickListener {

    private ImageButton likeButton;
    private ImageButton dislikeButton;

    private VoteHandler voteHandler;

    public LikeButtonClickListener(ImageButton likeButton, ImageButton dislikeButton) {
        this.likeButton = likeButton;
        this.dislikeButton = dislikeButton;
        this.likeButton.setTag("off");
        this.dislikeButton.setTag("off");
    }

    @Override
    public void onClick(View view) {
        updateImage();
    }

    private void updateImage() {
        boolean off = likeButton.getTag().equals("off");
        if (off) {
            likeButton.setImageResource(R.drawable.like_on);
            dislikeButton.setImageResource(R.drawable.dislike_off);
            likeButton.setTag("on");
            dislikeButton.setTag("off");
        } else {
            likeButton.setImageResource(R.drawable.like_off);
            likeButton.setTag("off");
        }
    }
}
