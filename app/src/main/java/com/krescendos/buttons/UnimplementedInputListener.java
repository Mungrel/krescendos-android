package com.krescendos.buttons;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UnimplementedInputListener implements View.OnClickListener, View.OnTouchListener {

    private Toast toast;

    public UnimplementedInputListener(Context context) {
        toast = Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT);
        TextView text = toast.getView().findViewById(android.R.id.message);
        text.setBackground(null);
    }

    @Override
    public void onClick(View view) {
        toast.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        toast.show();
        return true;
    }
}
