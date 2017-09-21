package com.krescendos.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.krescendos.model.Error;
import com.krescendos.utils.QuickDialog;

class DefaultErrorListener implements Response.ErrorListener {

    private static final String UNKNOWN_ERROR_TEXT = "Looks like something went wrong";

    private Context context;


    DefaultErrorListener(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        Log.d("TYPE", volleyError.getClass().getName());

        if (!(volleyError instanceof ServerError)) {
            return;
        }

        Log.d("RESPONSE", "Handling error response...");
        Error error = Error.fromVolleyError(volleyError);
        String userMessage = null;
        if (error != null) {
            Log.e("ERROR", error.getStatus() + ": " + error.getMessage());
            userMessage = error.getUserMessage();
        }

        String message;
        if (userMessage != null && !userMessage.isEmpty()) {
            message = userMessage;
        } else {
            message = UNKNOWN_ERROR_TEXT;
        }

        QuickDialog dialog = new QuickDialog(context, "Oops!", message);
        dialog.show();
    }
}
