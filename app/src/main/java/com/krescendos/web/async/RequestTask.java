package com.krescendos.web.async;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.lang.reflect.Type;

public class RequestTask<T> extends AsyncTask<Void, Void, T> {

    private HttpRequest request;
    private AsyncResponseListener<T> responseListener;
    private Type typeOfT;

    public RequestTask(HttpRequest request, Type typeOfT) {
        this.request = request;
        this.typeOfT = typeOfT;
    }

    public RequestTask(HttpRequest request, Type typeOfT, AsyncResponseListener<T> responseListener) {
        this(request, typeOfT);
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

        return new Gson().fromJson(response, typeOfT);
    }

    @Override
    protected void onPostExecute(T result) {
        if (responseListener != null && result != null) {
            responseListener.onResponse(result);
        }
    }
}
