package com.krescendos.web;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.krescendos.model.Party;
import com.krescendos.model.Profile;
import com.krescendos.model.SpotifySeedCollection;
import com.krescendos.model.Track;
import com.krescendos.web.requests.CreateRequest;
import com.krescendos.web.requests.JoinRequest;
import com.krescendos.web.requests.PollPostLearnerRequest;
import com.krescendos.web.requests.ProfileRequest;
import com.krescendos.web.requests.RecommendRequest;
import com.krescendos.web.requests.SearchRequest;

import java.util.List;

public class Requester {

    private static final String BASE_URL = "api.kres.io";
    private static ImageLoader imageLoader;
    private static Requester instance;
    private RequestQueue requestQueue;

    private Requester(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(requestQueue, new ImageCache());
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

    public void create(String partyName, String welcomeMessage, boolean allowSuggestions, Response.Listener<Party> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
            builder.appendQueryParameter("msg", welcomeMessage);
        }
        builder.appendQueryParameter("allowSuggestions", Boolean.toString(allowSuggestions));

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

    public void isPremiumUser(String userAccessToken, Response.Listener<Profile> responseListener) {
        ProfileRequest profileRequest = new ProfileRequest(userAccessToken, responseListener);
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
        builder.scheme("https").authority(BASE_URL);
        return builder;
    }
}
