package com.krescendos.search;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.domain.Track;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

public class SearchResponseListener implements Response.Listener<JSONArray> {

    private SearchTrackListAdapter adapter;

    public SearchResponseListener(SearchTrackListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onResponse(JSONArray response) {
        Type type = new TypeToken<List<Track>>() {
        }.getType();
        List<Track> searchResults = new Gson().fromJson(response.toString(), type);
        adapter.updateResults(searchResults);
    }
}
