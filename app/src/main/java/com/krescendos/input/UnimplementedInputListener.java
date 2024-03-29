package com.krescendos.input;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.krescendos.dialog.Toaster;

public class UnimplementedInputListener implements View.OnClickListener, View.OnTouchListener {

    private static final String TOAST_TEXT = "Coming soon!";
    private Context context;

    public UnimplementedInputListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Toaster.showToast(context, TOAST_TEXT);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Toaster.showToast(context, TOAST_TEXT);
        return true;
    }
}
