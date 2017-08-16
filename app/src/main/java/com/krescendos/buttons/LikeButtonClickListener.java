package com.krescendos.buttons;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;

public class LikeButtonClickListener implements View.OnClickListener {

    private ImageButton imageButton;
    private boolean on;

    public LikeButtonClickListener(ImageButton imageButton){
        this.imageButton = imageButton;
        on = false;
    }

    @Override
    public void onClick(View view) {
        on = !on;
        if (on) {
            imageButton.setImageResource(R.drawable.like_on);
        } else {
            imageButton.setImageResource(R.drawable.like_off);
        }
    }
}
