package com.krescendos;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.krescendos.domain.Party;
import com.krescendos.domain.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Requester {

    private RequestQueue requestQueue;
    private static String baseURL = "https://krescendos-174122.appspot.com";
    private static String RECOMMEND = baseURL+"/recommend";
    private static String SEARCH = baseURL+"/search";
    private static String CREATE = baseURL+"/create";
    private static String JOIN = baseURL+"/join";
    private static String APPEND_TRACK = baseURL+"/append";

    public Requester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Should take a list of track IDs, artist IDs, and genres, but for now just a single trackID
    public void recommend(String trackID, Response.Listener<JSONArray> listener){
        String url = Requester.RECOMMEND+"?trackSeed="+trackID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonArrayRequest);
    }

    public void search(String searchTerm, Response.Listener<JSONArray> listener){
        String url = Requester.SEARCH+"?k="+searchTerm;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonArrayRequest);
    }

    public void create(String partyName, Response.Listener<JSONObject> listener){
        String url = Requester.CREATE+"?name="+partyName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonObjectRequest);
    }

    public void join(String code, Response.Listener<JSONObject> listener){
        String url = Requester.JOIN+"?code="+code;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        requestQueue.add(jsonObjectRequest);
    }

    // No response expected, so we'll handle the response listener
    public void append(String code, Track track){
        String url = Requester.APPEND_TRACK+"?code="+code;
        String json = new Gson().toJson(track, Track.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, listener, new DefaultErrorListener());
        requestQueue.add(jsonObjectRequest);
    }

}
