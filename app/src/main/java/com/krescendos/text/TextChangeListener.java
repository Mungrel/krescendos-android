package com.krescendos.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextChangeListener implements TextWatcher {

    private EditText left;
    private EditText right;

    public TextChangeListener(EditText left, EditText right){
        this.left = left;
        this.right = right;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().isEmpty()){
            right.requestFocus();
        } else {
            left.requestFocus();
        }
    }
}
