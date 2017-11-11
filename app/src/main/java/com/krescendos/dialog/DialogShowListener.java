package com.krescendos.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.krescendos.R;

public class DialogShowListener implements DialogInterface.OnShowListener {

    private Context context;
    private AlertDialog alertDialog;

    DialogShowListener(Context context, AlertDialog alertDialog) {
        this.context = context;
        this.alertDialog = alertDialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
    }
}
