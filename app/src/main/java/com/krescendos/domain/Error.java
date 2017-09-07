package com.krescendos.domain;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Error {

    private int status;
    private String message;
    private String userMessage;

    public static Error fromVolleyError(VolleyError volleyError){
        NetworkResponse networkResponse = volleyError.networkResponse;
        Error error = null;
        if (networkResponse != null){
            String errorStr = null;
            try {
                JSONObject obj = new JSONObject(new String(networkResponse.data));
                errorStr = obj.getJSONObject("error").toString();
            } catch (JSONException e) {
                Log.e("PARSE_ERROR", "Failed to parse error");
            }
            error = new Gson().fromJson(errorStr, Error.class);
        }
        return error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
