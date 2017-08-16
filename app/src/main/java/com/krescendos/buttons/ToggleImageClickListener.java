package com.krescendos.buttons;

import android.view.View;
import android.widget.ImageButton;

public class ToggleImageClickListener implements View.OnClickListener {

    private ImageButton imageButton;
    private int offResId;
    private int onResId;
    private boolean on;

    public ToggleImageClickListener(ImageButton imageButton, int offResId, int onResId){
        this.imageButton = imageButton;
        this.offResId = offResId;
        this.onResId = onResId;
        on = false;
    }

    @Override
    public void onClick(View view) {
        on = !on;
        if (on) {
            imageButton.setImageResource(onResId);
        } else {
            imageButton.setImageResource(offResId);
        }
    }
}
