package com.krescendos;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Requester {

    private RequestQueue requestQueue;
    private static String baseURL = "localhost:8080";

    public Requester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void addRequest(JsonObjectRequest jsObjReq) {
        requestQueue.add(jsObjReq);
    }

    public void addRequest(JsonArrayRequest jsArrReq) {
        requestQueue.add(jsArrReq);
    }
}
