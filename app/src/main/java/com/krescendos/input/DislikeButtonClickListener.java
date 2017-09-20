package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;

public class DislikeButtonClickListener implements View.OnClickListener {

    private ImageButton dislikeButton;
    private ImageButton likeButton;

    public DislikeButtonClickListener(ImageButton dislikeButton, ImageButton likeButton) {
        this.dislikeButton = dislikeButton;
        this.likeButton = likeButton;
        this.dislikeButton.setTag("off");
        this.likeButton.setTag("off");
    }

    @Override
    public void onClick(View view) {
        boolean off = dislikeButton.getTag().equals("off");
        if (off) {
            dislikeButton.setImageResource(R.drawable.dislike_on);
            dislikeButton.setTag("on");
            likeButton.setImageResource(R.drawable.like_off);
            likeButton.setTag("off");
        } else {
            dislikeButton.setImageResource(R.drawable.dislike_off);
            dislikeButton.setTag("off");
        }
    }
}
