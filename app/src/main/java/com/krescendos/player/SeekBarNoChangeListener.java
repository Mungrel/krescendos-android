package com.krescendos.player;

import android.view.MotionEvent;
import android.view.View;

public class SeekBarNoChangeListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
