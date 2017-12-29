package com.krescendos.web.async;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.lang.reflect.Type;

public class RequestTask<T> extends AsyncTask<Void, Void, T> {

    private HttpRequest request;
    private AsyncResponseListener<T> responseListener;

    public RequestTask(HttpRequest request) {
        this.request = request;
    }

    public RequestTask(HttpRequest request, AsyncResponseListener<T> responseListener) {
        this.request = request;
        this.responseListener = responseListener;
    }

    @Override
    protected T doInBackground(Void... voids) {
        String response = null;
        try {
            response = request.asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        Type type = new TypeToken<T>(){}.getType();

        return new Gson().fromJson(response, type);
    }

    @Override
    protected void onPostExecute(T result) {
        if (responseListener != null && result != null) {
            responseListener.onResponse(result);
        }
    }
}
