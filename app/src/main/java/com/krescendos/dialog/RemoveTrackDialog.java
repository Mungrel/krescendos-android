package com.krescendos.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class RemoveTrackDialog {

    private AlertDialog alertDialog;
    private DialogInterface.OnClickListener listener;

    public RemoveTrackDialog(DialogInterface.OnClickListener listener) {
        this.alertDialog = buildDialog(listener);
    }

    private AlertDialog buildDialog(DialogInterface.OnClickListener listener) {
        return null;
    }
}
