package com.krescendos.web;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krescendos.domain.Track;

import org.json.JSONArray;
import org.json.JSONObject;

public class Requester {

    private RequestQueue requestQueue;

    public Requester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Should take a list of track IDs, artist IDs, and genres, but for now just a single trackID
    public void recommend(String trackID, Response.Listener<JSONArray> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("recommend").appendQueryParameter("trackSeed", trackID);
        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonArrayRequest);
    }

    public void search(String searchTerm, Response.Listener<JSONArray> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonArrayRequest);
    }

    public void create(String partyName, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    public void join(String code, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    // No response expected, so we'll handle the response listener
    public void append(String code, Track track) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("playlist").appendQueryParameter("spotifyTrackId", track.getId());
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        requestQueue.add(jsonObjectRequest);
    }

    private Uri.Builder getBaseBuilder(){
        Uri.Builder builder = new Uri.Builder();
        String baseURL = "krescendos-174122.appspot.com";
        builder.scheme("https").authority(baseURL);
        return builder;
    }
}
