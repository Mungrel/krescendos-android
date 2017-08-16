package com.krescendos.buttons;

import android.view.View;
import android.widget.ImageButton;

import com.krescendos.R;

public class DislikeButtonClickListener implements View.OnClickListener {

    private ImageButton imageButton;
    private boolean on;

    public DislikeButtonClickListener(ImageButton imageButton){
        this.imageButton = imageButton;
        on = false;
    }

    @Override
    public void onClick(View view) {
        on = !on;
        if (on) {
            imageButton.setImageResource(R.drawable.dislike_on);
        } else {
            imageButton.setImageResource(R.drawable.dislike_off);
        }
    }
}
