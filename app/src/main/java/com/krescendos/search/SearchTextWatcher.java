package com.krescendos.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.krescendos.web.Requester;

public class SearchTextWatcher implements TextWatcher {

    private Requester requester;
    private SearchTrackListAdapter adapter;

    public SearchTextWatcher(Context context, SearchTrackListAdapter adapter){
        this.requester = Requester.getInstance(context);
        this.adapter = adapter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String term = charSequence.toString();
        requester.cancelAll();
        requester.search(term, new SearchResponseListener(adapter));
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
