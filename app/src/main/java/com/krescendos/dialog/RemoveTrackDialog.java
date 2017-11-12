package com.krescendos.dialog;

import android.app.AlertDialog;

public class RemoveTrackDialog {

    private YesNoListener listener;
    private AlertDialog dialog;

    public RemoveTrackDialog(YesNoListener listener) {
        this.listener = listener;
    }
    
}
