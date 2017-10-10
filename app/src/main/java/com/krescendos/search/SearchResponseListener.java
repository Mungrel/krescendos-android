package com.krescendos.search;

import com.android.volley.Response;
import com.krescendos.model.Track;

import java.util.List;

public class SearchResponseListener implements Response.Listener<List<Track>> {

    private SearchTrackListAdapter adapter;
    private SearchSpinner searchSpinner;

    public SearchResponseListener(SearchTrackListAdapter adapter, SearchSpinner searchSpinner) {
        this.adapter = adapter;
        this.searchSpinner = searchSpinner;
    }

    @Override
    public void onResponse(List<Track> response) {
        searchSpinner.hide();
        adapter.updateResults(response);
    }
}
