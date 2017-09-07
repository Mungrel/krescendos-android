package com.krescendos.web;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.activities.HostPlayerActivity;
import com.krescendos.domain.Error;
import com.krescendos.utils.QuickDialog;

public class DefaultErrorListener implements Response.ErrorListener {

    private static final String UNKNOWN_ERROR_TEXT = "Looks like something went wrong";

    private Context context;


    public DefaultErrorListener(Context context){
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        Log.d("TYPE", volleyError.getClass().getName());

        if (!(volleyError instanceof ServerError)){
            return;
        }

        Log.d("RESPONSE", "Handling error response...");
        Error error = Error.fromVolleyError(volleyError);
        String userMessage = null;
        if (error != null){
            Log.e("ERROR", error.getStatus()+": "+error.getMessage());
            userMessage = error.getUserMessage();
        }

        String message = null;
        if (userMessage != null && !userMessage.isEmpty()){
            message = userMessage;
        } else {
            message = UNKNOWN_ERROR_TEXT;
        }

        QuickDialog dialog = new QuickDialog(context, "Oops!", message);
        dialog.show();
    }
}
