package com.krescendos.dialog;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class Toaster {

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        TextView text = toast.getView().findViewById(android.R.id.message);
        text.setBackground(null);
        toast.show();
    }
}
