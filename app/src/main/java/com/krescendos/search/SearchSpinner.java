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

    public SearchSpinner(Context context, ImageView imageView){
        this.imageView = imageView;
        this.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely);
    }

    public void start(){
        imageView.startAnimation(animation);
    }

    public void stop(){
        imageView.clearAnimation();
    }

    public void hide(){
        imageView.clearAnimation();
        imageView.setVisibility(View.GONE);
    }
}
