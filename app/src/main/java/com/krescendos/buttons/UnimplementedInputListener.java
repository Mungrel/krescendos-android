package com.krescendos.buttons;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class UnimplementedInputListener implements View.OnClickListener {

    private Toast toast;

    public UnimplementedInputListener(Context context) {
        toast = Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View view) {
        toast.show();
    }
}
