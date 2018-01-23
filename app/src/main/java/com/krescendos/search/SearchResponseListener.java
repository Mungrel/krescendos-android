package com.krescendos.search;

import com.android.volley.Response;
import com.krescendos.model.Track;
import com.krescendos.spinner.SpinnerView;
import com.krescendos.web.async.AsyncResponseListener;

import java.util.List;

public class SearchResponseListener implements AsyncResponseListener<List<Track>> {

    private SearchTrackListAdapter adapter;
    private SpinnerView searchSpinner;

    public SearchResponseListener(SearchTrackListAdapter adapter, SpinnerView searchSpinner) {
        this.adapter = adapter;
        this.searchSpinner = searchSpinner;
    }

    @Override
    public void onResponse(List<Track> response) {
        searchSpinner.hide();
        adapter.updateResults(response);
    }
}
