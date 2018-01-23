package com.krescendos.spinner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krescendos.R;

public class SpinnerView extends LinearLayout {

    private ImageView spinnerIcon;
    private TextView textView;
    private Animation animation;
    private boolean running;

    public SpinnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely);
        this.running = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.spinner, this);

        spinnerIcon = layout.findViewById(R.id.spinner_icon);
        textView =  layout.findViewById(R.id.spinner_icon_text);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void start() {
        if (running) {
            return;
        }
        spinnerIcon.setVisibility(View.VISIBLE);
        spinnerIcon.startAnimation(animation);

        textView.setVisibility(View.VISIBLE);

        running = true;
    }

    public void hide() {
        spinnerIcon.clearAnimation();
        spinnerIcon.setVisibility(View.GONE);

        textView.setVisibility(View.GONE);

        running = false;
    }
}
