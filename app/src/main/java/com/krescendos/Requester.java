package com.krescendos;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.Map;

public class Requester {

    private RequestQueue requestQueue;
    private static String baseURL = "localhost:8080";
    public static String RECOMMENDATION = baseURL+"/recommend";
    public static String SEARCH = baseURL+"/search";

    public Requester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void get(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void get(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Map<String, String> params) {
        url = "http://192.168.43.247/recommend";
        if (!params.keySet().isEmpty()){
            url+="?";
            for (String s : params.keySet()){
                url += s+"="+params.get(s)+"&";
            }
            if (url.endsWith("&")){
                url = url.substring(0, url.length()-1);
            }
        }

        Log.d("SENDING:", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

}
