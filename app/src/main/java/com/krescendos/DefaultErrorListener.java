package com.krescendos;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class DefaultErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ERROR:", "" + error.getMessage());
    }
}
