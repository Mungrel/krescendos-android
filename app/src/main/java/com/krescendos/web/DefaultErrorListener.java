package com.krescendos.web;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.domain.Error;

public class DefaultErrorListener implements Response.ErrorListener {

    private static final String UNKNOWN_ERROR_TEXT = "Looks like something went wrong";

    private Context context;


    public DefaultErrorListener(Context context){
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Error error = Error.fromVolleyError(volleyError);
        String userMessage = null;
        if (error != null){
            Log.e("ERROR", error.getStatus()+": "+error.getMessage());
            userMessage = error.getUserMessage();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (userMessage != null && !userMessage.isEmpty()){
            builder.setMessage(userMessage);
        } else {
            builder.setMessage(UNKNOWN_ERROR_TEXT);
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }
}
