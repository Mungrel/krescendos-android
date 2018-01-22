package com.krescendos.search;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.krescendos.R;

public class SearchSpinner {

    private ImageView imageView;
    private TextView textView;
    private Animation animation;
    private boolean running;

    public SearchSpinner(Context context, ImageView imageView, TextView textView) {
        this.imageView = imageView;
        this.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely);
        this.running = false;
        this.textView = textView;
    }

    public void start() {
        if (running) {
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation);

        textView.setVisibility(View.VISIBLE);

        running = true;
    }

    public void hide() {
        imageView.clearAnimation();
        imageView.setVisibility(View.GONE);

        textView.setVisibility(View.GONE);

        running = false;
    }

    public void setText(int resID) {
        textView.setText(resID);
    }
}
