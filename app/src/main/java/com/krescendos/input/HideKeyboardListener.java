package com.krescendos.input;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboardListener implements TextWatcher {

    private InputMethodManager imm;

    public HideKeyboardListener(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String text = charSequence.toString();
        if (!text.isEmpty() && imm.isActive()) {
            hide();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void hide() {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
