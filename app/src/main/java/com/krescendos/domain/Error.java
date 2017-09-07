package com.krescendos.domain;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

public class Error {

    private int status;
    private String message;
    private String userMessage;

    public static Error fromVolleyError(VolleyError volleyError){
        NetworkResponse networkResponse = volleyError.networkResponse;
        Error error = null;
        if (networkResponse != null){
            error = new Gson().fromJson(new String(networkResponse.data), Error.class);
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
