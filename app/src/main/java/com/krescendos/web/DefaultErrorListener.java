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

public class DefaultErrorListener implements Response.ErrorListener {

    private static final String UNKNOWN_ERROR_TEXT = "Looks like something went wrong";

    private Context context;


    public DefaultErrorListener(Context context){
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
            Log.e("ERROR", "Timeout/No Connection");
            Log.d("RESPONSE", new String(volleyError.networkResponse.data));
        } else if (volleyError instanceof AuthFailureError) {
            Log.e("ERROR", "AuthFailure");
            Log.d("RESPONSE", new String(volleyError.networkResponse.data));
        } else if (volleyError instanceof ServerError) {
            Log.e("ERROR", "ServerError");
            Log.d("RESPONSE", new String(volleyError.networkResponse.data));
        } else if (volleyError instanceof NetworkError) {
            Log.e("ERROR", "NetworkError");
            Log.d("RESPONSE", new String(volleyError.networkResponse.data));
        } else if (volleyError instanceof ParseError) {
            Log.e("ERROR", "ParseError");
            Log.d("RESPONSE", new String(volleyError.networkResponse.data));
        }

        Log.d("RESPONSE", "Handling error response...");
        Error error = Error.fromVolleyError(volleyError);
        String userMessage = null;
        if (error != null){
            Log.e("ERROR", error.getStatus()+": "+error.getMessage());
            userMessage = error.getUserMessage();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.errorDialog));

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
        builder.setTitle("Oops!");
       // AlertDialog alertDialog = builder.create();
        //alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
       // alertDialog.show();
        builder.show();
    }
}
