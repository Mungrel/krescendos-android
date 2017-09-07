package com.krescendos.web;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.krescendos.domain.Error;

public class TextViewErrorListener implements Response.ErrorListener {

    private TextView textView;

    public TextViewErrorListener(TextView textView){
        this.textView = textView;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null){
            Error error = new Gson().fromJson(new String(networkResponse.data), Error.class);
            Log.e("ERROR", error.getStatus()+": "+error.getMessage());
            textView.setText(error.getUserMessage());
        }
    }
}
