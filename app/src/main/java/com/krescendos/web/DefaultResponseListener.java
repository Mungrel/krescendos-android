package com.krescendos.web;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

public class DefaultResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        Log.d("RESPONSE", response.toString());
    }
}
