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
        this.requester = new Requester(context);
        this.adapter = adapter;
        this.searchSpinner = searchSpinner;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        searchSpinner.start();
        String term = charSequence.toString();
        requester.cancelAll();
        if (!term.isEmpty()){
            requester.search(term, new SearchResponseListener(adapter, searchSpinner));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
