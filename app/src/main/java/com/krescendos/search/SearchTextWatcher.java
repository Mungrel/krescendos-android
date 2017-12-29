package com.krescendos.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.krescendos.web.Requester;

public class SearchTextWatcher implements TextWatcher {

    private Requester requester;
    private SearchTrackListAdapter adapter;
    private SearchSpinner searchSpinner;

    public SearchTextWatcher(Context context, SearchTrackListAdapter adapter, SearchSpinner searchSpinner) {
        this.requester = Requester.getInstance();
        this.adapter = adapter;
        this.searchSpinner = searchSpinner;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String term = charSequence.toString();
        if (term.isEmpty()) {
            searchSpinner.hide();
        } else {
            requester.search(term, new SearchResponseListener(adapter, searchSpinner));
            searchSpinner.start();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
