package com.krescendos.web;

import android.net.Uri;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.model.Profile;
import com.krescendos.model.SpotifySeedCollection;
import com.krescendos.model.Track;
import com.krescendos.web.async.AsyncResponseListener;
import com.krescendos.web.async.RequestTask;
import com.krescendos.web.async.Types;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.lang.reflect.Type;
import java.util.List;

public class Requester {

    private static final String BASE_URL = "0-1-1-32-gcb49d12-dot-krescendos-174122.appspot.com";
    private static Requester instance;

    private Gson gson;

    private Requester() {
        this.gson = new Gson();
    }

    public static Requester getInstance() {
        if (instance == null) {
            instance = new Requester();
        }

        return instance;
    }

    // Should take a list of track IDs, artist IDs, and genres, but for now just a single trackID
    public void recommend(String partyID, AsyncResponseListener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("recommend");
        builder.appendQueryParameter("id", partyID);
        String url = builder.build().toString();

        GetRequest request = Unirest.get(url);

        RequestTask<List<Track>> task = new RequestTask<List<Track>>(request, Types.LIST_TRACK, listener);
        task.execute();
    }

    public RequestTask<List<Track>> search(String searchTerm, AsyncResponseListener<List<Track>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("search").appendQueryParameter("k", searchTerm);
        String url = builder.build().toString();

        GetRequest request = Unirest.get(url);
        Type type = new TypeToken<List<Track>>(){}.getType();
        RequestTask<List<Track>> task = new RequestTask<List<Track>>(request, Types.LIST_TRACK, listener);
        task.execute();

        return task;
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
        RequestTask<Party> task = new RequestTask<Party>(request, Types.PARTY, listener);
        task.execute();
    }

    public void join(String code, AsyncResponseListener<Party> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = getBaseBuilder();
        builder.path("party").appendQueryParameter("id", code);
        String url = builder.build().toString();

        GetRequest request = Unirest.get(url);
        RequestTask<Party> task = new RequestTask<Party>(request, Types.PARTY, listener);
        task.execute();
    }

    public void isPremiumUser(String userAccessToken, AsyncResponseListener<Profile> listener) {
        GetRequest request = Unirest.get("https://api.spotify.com/v1/me");
        request.header("Authorization", "Bearer " + userAccessToken);

        RequestTask<Profile> task = new RequestTask<Profile>(request, Types.PROFILE, listener);
        task.execute();
    }

    public void pollPostLearner(List<String> userSelection, AsyncResponseListener<List<String>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("kurtis");
        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.post(url);
        RequestTask<List<String>> task = new RequestTask<List<String>>(request, Types.LIST_STRING, listener);
        task.execute();
    }

    public void userPlaylists(String username, AsyncResponseListener<List<Playlist>> listener) {
        Uri.Builder builder = getBaseBuilder();
        builder.appendPath("playlists");
        builder.appendQueryParameter("username", username);
        String url = builder.build().toString();

        HttpRequestWithBody request = Unirest.put(url);
        RequestTask<List<Playlist>> task = new RequestTask<List<Playlist>>(request, Types.LIST_PLAYLIST, listener);
        task.execute();
    }

    private Uri.Builder getBaseBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(BASE_URL);
        return builder;
    }
}
