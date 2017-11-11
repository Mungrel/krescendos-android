package com.krescendos.input;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboardListener implements TextWatcher {

    private Context context;

    public HideKeyboardListener(Context context) {
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String text = charSequence.toString();
        if (!text.isEmpty()) {
            Keyboard.hide(context);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

}
