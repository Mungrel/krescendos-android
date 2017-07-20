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
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.Map;

public class Requester {

    private RequestQueue requestQueue;
    private static String baseURL = "http://192.168.1.235:8080";
    public static String RECOMMEND = baseURL+"/recommend";
    public static String SEARCH = baseURL+"/search";

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

}
