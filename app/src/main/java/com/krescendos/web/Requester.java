package com.krescendos.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krescendos.domain.Track;
import com.krescendos.player.PlayState;

import org.json.JSONArray;
import org.json.JSONObject;

public class Requester {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static int TIMEOUT_MS = 5000;

    private static Requester instance;

    private Requester(Context context) {
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
                listener, new DefaultErrorListener());
        jsonArrayRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonArrayRequest);
    }

    public void search(String searchTerm, Response.Listener<JSONArray> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, new DefaultErrorListener());
        jsonArrayRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonArrayRequest);
    }

    public void create(String partyName, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                listener, errorListener);
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    public void join(String code, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                listener, errorListener);
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    // No response expected, so we'll handle the response listener
    public void append(String code, Track track) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("playlist").appendQueryParameter("spotifyTrackId", track.getId());
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    public void nextTrack(String code) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("next");
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    public void nextTrack(String code, int newPos) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("next").appendQueryParameter("index", "" + newPos);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    public void updatePlayState(String code, PlayState state, long timeMs) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("state")
                .appendQueryParameter("newState", state.toString().toLowerCase())
                .appendQueryParameter("trackTime", "" + timeMs);
        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        jsonObjectRequest.setRetryPolicy(new LongTimeoutRetryPolicy(TIMEOUT_MS));
        requestQueue.add(jsonObjectRequest);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private Uri.Builder getBaseBuilder() {
        Uri.Builder builder = new Uri.Builder();
        String baseURL = "krescendos-174122.appspot.com";
        builder.scheme("https").authority(baseURL);
        return builder;
    }
}
