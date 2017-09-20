package com.krescendos.search;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.krescendos.R;

public class SearchSpinner {

    private ImageView imageView;
    private Animation animation;
    private boolean running;

    public SearchSpinner(Context context, ImageView imageView) {
        this.imageView = imageView;
        this.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely);
        this.running = false;
    }

    public void start() {
        if (running) {
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation);
        running = true;
    }

    public void hide() {
        imageView.clearAnimation();
        imageView.setVisibility(View.GONE);
        running = false;
    }
}
