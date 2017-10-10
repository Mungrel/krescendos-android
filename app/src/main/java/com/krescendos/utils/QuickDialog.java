package com.krescendos.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;

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

        ContextThemeWrapper wrapper = new ContextThemeWrapper(context, R.style.errorDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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
        });

        return alertDialog;
    }
}
