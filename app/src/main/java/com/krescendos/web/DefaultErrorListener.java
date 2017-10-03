package com.krescendos.web;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class DefaultErrorListener implements Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        Log.e("NET-ERROR", "" + volleyError.getMessage());
    }
}
