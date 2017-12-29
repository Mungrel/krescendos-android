package com.krescendos.web;

import android.net.Uri;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.model.Profile;
import com.krescendos.model.SpotifySeedCollection;
import com.krescendos.model.Track;
import com.krescendos.web.async.AsyncResponseListener;
import com.krescendos.web.async.RequestTask;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.util.List;

public class Requester2 {

    private static final String BASE_URL = "api.kres.io";
    private Gson gson;

    private Requester2() {
        this.gson = new Gson();
    }

    // Should take a list of track IDs, artist IDs, and genres, but for now just a single trackID
    public void recommend(SpotifySeedCollection collection, AsyncResponseListener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("recommend");
        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.post(url);
        request.body(gson.toJson(collection));

        RequestTask<List<Track>> task = new RequestTask<List<Track>>(request, listener);
        task.execute();
    }

    public void search(String searchTerm, AsyncResponseListener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        GetRequest request = Unirest.get(url);
        RequestTask<List<Track>> task = new RequestTask<List<Track>>(request, listener);
        task.execute();
    }

    public void create(String partyName, String welcomeMessage, boolean allowSuggestions, AsyncResponseListener<Party> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("party").appendQueryParameter("name", partyName);
        if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
            builder.appendQueryParameter("msg", welcomeMessage);
        }
        builder.appendQueryParameter("allowSuggestions", Boolean.toString(allowSuggestions));

        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.put(url);
        RequestTask<Party> task = new RequestTask<Party>(request, listener);
        task.execute();
    }

    public void join(String code, AsyncResponseListener<Party> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        GetRequest request = Unirest.get(url);
        RequestTask<Party> task = new RequestTask<Party>(request, listener);
    }

    public void isPremiumUser(String userAccessToken, AsyncResponseListener<Profile> listener) {
        GetRequest request = Unirest.get("https://api.spotify.com/v1/me");
        request.header("Authorization", "Bearer " + userAccessToken);

        RequestTask<Profile> task = new RequestTask<Profile>(request, listener);
    }

    public void pollPostLearner(List<String> userSelection, AsyncResponseListener<List<String>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("kurtis");
        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.post(url);
        RequestTask<List<String>> task = new RequestTask<List<String>>(request, listener);
    }

    public void userPlaylists(String username, AsyncResponseListener<List<Playlist>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("playlists");
        builder.appendQueryParameter("username", username);
        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.put(url);
        RequestTask<List<Playlist>> task = new RequestTask<List<Playlist>>(request, listener);
    }

    private Uri.Builder getBaseBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(BASE_URL);
        return builder;
    }
}
