package com.krescendos.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.krescendos.R;

public class DialogShowListener implements DialogInterface.OnShowListener {

    private Context context;
    private AlertDialog alertDialog;
    private OnQuickDialogCloseListener closeListener;

    public DialogShowListener(Context context, AlertDialog alertDialog, OnQuickDialogCloseListener closeListener) {
        this.context = context;
        this.alertDialog = alertDialog;
        this.closeListener = closeListener;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        if (closeListener != null) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.hide();
                    closeListener.onClose();
                }
            });
        }
    }
}
