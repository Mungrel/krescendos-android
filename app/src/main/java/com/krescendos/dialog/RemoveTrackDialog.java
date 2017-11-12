package com.krescendos.dialog;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.krescendos.R;

public class RemoveTrackDialog {

    private AlertDialog alertDialog;

    public RemoveTrackDialog(Context context, OnYesListener onYesListener) {
        this.alertDialog = buildDialog(context, onYesListener);
    }

    private AlertDialog buildDialog(Context context, final OnYesListener onYesListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.errorDialog));
        builder.setTitle("Remove");
        builder.setMessage("Remove this track from Up Next?");
        builder.setCancelable(false);

        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    onYesListener.onYes();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogShowListener(context, alertDialog));
        return null;
    }
}
