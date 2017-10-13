package com.krescendos.input;

import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.krescendos.R;
import com.krescendos.vote.VoteDirection;
import com.krescendos.vote.VoteHandler;

public class DislikeButtonClickListener implements View.OnClickListener {

    private ImageButton dislikeButton;
    private ImageButton likeButton;

    private DatabaseReference voteCountRef;

    public DislikeButtonClickListener(ImageButton dislikeButton, ImageButton likeButton, DatabaseReference voteCountRef) {
        this.dislikeButton = dislikeButton;
        this.likeButton = likeButton;
        this.dislikeButton.setTag("off");
        this.likeButton.setTag("off");
        this.voteCountRef = voteCountRef;
    }

    @Override
    public void onClick(View view) {
        voteCountRef.runTransaction(new VoteHandler(VoteDirection.DOWN));
        updateImage();
    }
    
    private void updateImage() {
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
