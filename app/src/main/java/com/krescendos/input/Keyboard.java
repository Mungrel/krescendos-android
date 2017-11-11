package com.krescendos.input;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {

    public static void hide(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            Log.d("KEYBOARD", "Hiding");
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static void show(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && !imm.isActive()) {
            Log.d("KEYBOARD", "Showing");
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
