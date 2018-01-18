package com.krescendos.search;

import android.text.Editable;
import android.text.TextWatcher;

import com.krescendos.R;
import com.krescendos.model.Track;
import com.krescendos.web.Requester;
import com.krescendos.web.async.RequestTask;

import java.util.List;

public class SearchTextWatcher implements TextWatcher {

    private Requester requester;
    private SearchTrackListAdapter adapter;
    private SearchSpinner searchSpinner;

    private RequestTask<List<Track>> previousSearchTask;

    public SearchTextWatcher(SearchTrackListAdapter adapter, SearchSpinner searchSpinner) {
        this.requester = Requester.getInstance();
        this.adapter = adapter;
        this.searchSpinner = searchSpinner;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (previousSearchTask != null) {
            previousSearchTask.cancel(true);
        }
        String term = charSequence.toString();
        if (term.isEmpty()) {
            searchSpinner.hide();
        } else {
            previousSearchTask = requester.search(term, new SearchResponseListener(adapter, searchSpinner));
            searchSpinner.setText(R.string.searching_spotify);
            searchSpinner.start();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
