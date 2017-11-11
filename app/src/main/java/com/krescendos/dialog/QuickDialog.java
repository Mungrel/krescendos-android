package com.krescendos.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.krescendos.R;

public class QuickDialog {

    private AlertDialog alertDialog;
    private OnQuickDialogCloseListener closeListener;

    public QuickDialog(Context context, String title, String message) {
        this.alertDialog = buildDialog(context, title, message);
    }

    public QuickDialog(Context context, String title, String message, OnQuickDialogCloseListener closeListener) {
        this(context, title, message);
        this.closeListener = closeListener;
    }

    public void show() {
        alertDialog.show();
    }

    private AlertDialog buildDialog(final Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.errorDialog));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null) {
                    Log.d("DIALOG", "onClick, setPositive button");
                    alertDialog.dismiss();
                    if (closeListener != null) {
                        closeListener.onClose();
                    }
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogShowListener(context, alertDialog));

        return alertDialog;
    }
}
