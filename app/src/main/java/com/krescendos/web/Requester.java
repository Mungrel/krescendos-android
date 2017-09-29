package com.krescendos.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.krescendos.model.PartyState;
import com.krescendos.model.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Requester {

    private static final String baseURL = "api.kres.io";

    private RequestQueue requestQueue;
    private static ImageLoader imageLoader;
    private Context context;

    private static Requester instance;

    private Requester(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static Requester getInstance(Context context) {
        if (instance == null) {
            instance = new Requester(context);
        }
        return instance;
    }

    public void cancelAll() {
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    // Should take a list of track IDs, artist IDs, and genres, but for now just a single trackID
    public void recommend(String trackID, Response.Listener<JSONArray> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("recommend").appendQueryParameter("trackSeed", trackID);
        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener(context));
        jsonArrayRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonArrayRequest);
    }

    public void search(String searchTerm, Response.Listener<JSONArray> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener(context));
        jsonArrayRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonArrayRequest);
    }

    public void create(String partyName, String welcomeMessage, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
            builder.appendQueryParameter("msg", welcomeMessage);
        }
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                listener, errorListener);
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public void join(String code, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    // No response expected, so we'll handle the response listener
    public void append(String code, Track track) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("playlist").appendQueryParameter("spotifyTrackId", track.getId());
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener(context));
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public void advancePlayhead(String code) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("advancePlayhead");
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new DefaultResponseListener(), new DefaultErrorListener(context));
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public void advancePlayhead(String code, int newPos) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("advancePlayhead").appendQueryParameter("nextIndex", "" + newPos);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new DefaultResponseListener(), new DefaultErrorListener(context));
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public void updatePlayState(String code, PartyState state) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("partyState");
        String url = builder.build().toString();

        JSONObject jsonState = null;
        try {
            jsonState = new JSONObject(new Gson().toJson(state));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonState, new DefaultResponseListener(), new DefaultErrorListener(context));
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public void requestPartyStateUpdate(String code) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("update");
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new DefaultResponseListener(), new DefaultErrorListener(context));
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public boolean isPremiumUser(String userAccessToken) {
        return false;
    }

    public void pollPostLearner(List<String> userSelection, Response.Listener<JSONArray> responseListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("kurtis");
        String url = builder.build().toString();

        JSONArray userSelectionArray = null;
        try {
            userSelectionArray = new JSONArray(new Gson().toJson(userSelection));
        } catch (JSONException e) {
            Log.d("JSONPARSE", "Failed to create JSON array");
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, userSelectionArray, responseListener, new DefaultErrorListener(context));
        jsonArrayRequest.setRetryPolicy(new LongTimeoutRetryPolicy());
        requestQueue.add(jsonArrayRequest);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private Uri.Builder getBaseBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(baseURL);
        return builder;
    }
}
