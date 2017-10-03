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
import com.krescendos.model.Party;
import com.krescendos.model.PartyState;
import com.krescendos.model.Profile;
import com.krescendos.model.SpotifySeedCollection;
import com.krescendos.model.Track;
import com.krescendos.web.requests.AdvancePlayheadRequest;
import com.krescendos.web.requests.AppendRequest;
import com.krescendos.web.requests.CreateRequest;
import com.krescendos.web.requests.JoinRequest;
import com.krescendos.web.requests.PollPostLearnerRequest;
import com.krescendos.web.requests.ProfileRequest;
import com.krescendos.web.requests.RecommendRequest;
import com.krescendos.web.requests.RequestStateUpdateRequest;
import com.krescendos.web.requests.SearchRequest;
import com.krescendos.web.requests.UpdatePlayStateRequest;

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
    public void recommend(SpotifySeedCollection collection, Response.Listener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("recommend");
        String url = builder.build().toString();

        RecommendRequest request = new RecommendRequest(url, collection, listener);
        
        requestQueue.add(request);
    }

    public void search(String searchTerm, Response.Listener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        SearchRequest searchRequest = new SearchRequest(url, listener);
        requestQueue.add(searchRequest);
    }

    public void create(String partyName, String welcomeMessage, Response.Listener<Party> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
            builder.appendQueryParameter("msg", welcomeMessage);
        }
        String url = builder.build().toString();

        CreateRequest request = new CreateRequest(url, listener);
        requestQueue.add(request);
    }

    public void join(String code, Response.Listener<Party> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        JoinRequest request = new JoinRequest(url, listener, errorListener);
        requestQueue.add(request);
    }

    // No response expected, so we'll handle the response listener
    public void append(String code, Track track) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("playlist").appendQueryParameter("spotifyTrackId", track.getId());
        String url = builder.build().toString();

        AppendRequest request = new AppendRequest(url);
        requestQueue.add(request);
    }

    public void advancePlayhead(String code) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("advancePlayhead");
        String url = builder.build().toString();

        AdvancePlayheadRequest request = new AdvancePlayheadRequest(url);
        requestQueue.add(request);
    }

    public void advancePlayhead(String code, int newPos) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("advancePlayhead").appendQueryParameter("nextIndex", "" + newPos);
        String url = builder.build().toString();

        AdvancePlayheadRequest request = new AdvancePlayheadRequest(url);
        requestQueue.add(request);
    }

    public void updatePlayState(String code, PartyState state) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("partyState");
        String url = builder.build().toString();

        UpdatePlayStateRequest request = new UpdatePlayStateRequest(url, state);
        requestQueue.add(request);
    }

    public void requestPartyStateUpdate(String code) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendPath(code).appendPath("update");
        String url = builder.build().toString();

        RequestStateUpdateRequest request = new RequestStateUpdateRequest(url);
        requestQueue.add(request);
    }

    public void isPremiumUser(String userAccessToken, Response.Listener<Profile> responseListener) {
        ProfileRequest profileRequest = new ProfileRequest(context, userAccessToken, responseListener);
        requestQueue.add(profileRequest);
    }

    public void pollPostLearner(List<String> userSelection, Response.Listener<List<String>> responseListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("kurtis");
        String url = builder.build().toString();

        PollPostLearnerRequest request = new PollPostLearnerRequest(url, userSelection, responseListener);
        requestQueue.add(request);
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
