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
        DatabaseReference voteCountRef = FirebaseRefs.getVoteCountRef(partyId, item.getDbKey());
        voteCountRef.runTransaction(new VoteTransaction(VoteDirection.DOWN));
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
