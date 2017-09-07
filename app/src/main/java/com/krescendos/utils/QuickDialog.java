package com.krescendos.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

import com.krescendos.R;

public class QuickDialog {

    private AlertDialog alertDialog;

    public QuickDialog(Context context, String title, String message){
        ContextThemeWrapper wrapper = new ContextThemeWrapper(context, R.style.errorDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null){
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = builder.create();
    }

    public void show(){
        alertDialog.show();
    }
}
