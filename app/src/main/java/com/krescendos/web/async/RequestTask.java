package com.krescendos.web.async;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.lang.reflect.Type;

public class RequestTask<T> extends AsyncTask<Void, Void, T> {
    private AsyncResponseListener<T> responseListener;
    private HttpRequest request;


    public RequestTask(AsyncResponseListener<T> responseListener, HttpRequest request) {
        this.responseListener = responseListener;
        this.request = request;
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
        responseListener.onResponse(result);
    }
}
