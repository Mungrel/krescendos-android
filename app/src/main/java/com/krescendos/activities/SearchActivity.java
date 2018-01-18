package com.krescendos.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.model.Party;
import com.krescendos.model.Track;
import com.krescendos.search.SearchSpinner;
import com.krescendos.search.SearchTextWatcher;
import com.krescendos.search.SearchTrackListAdapter;
import com.krescendos.web.Requester;
import com.krescendos.web.async.AsyncResponseListener;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    protected static final int SEARCH_CODE = 1234;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Party party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);

        toolbarTitle.setText(party.getName());
        List<Track> playlist = party.getPlaylist();
        final SearchTrackListAdapter listAdapter = new SearchTrackListAdapter(SearchActivity.this, playlist, party.getPartyId());

        ListView resultsView = (ListView) findViewById(R.id.search_result_list);
        resultsView.setAdapter(listAdapter);

        if (listAdapter.isAcceptingRecommendations()) {
            if (playlist == null || playlist.isEmpty()) {
                Requester.getInstance().recommend(null, new AsyncResponseListener<List<Track>>() {
                    @Override
                    public void onResponse(List<Track> response) {
                        if(listAdapter.isAcceptingRecommendations() && !response.isEmpty()) {
                            listAdapter.updateResults(response);
                        }
                    }
                });
            }
        }

        ImageView spinnerImage = (ImageView) findViewById(R.id.search_icon_spinner);

        final TextView searchSpinnerText = (TextView) findViewById(R.id.search_icon_spinner_text);

        SearchSpinner searchSpinner = new SearchSpinner(SearchActivity.this, spinnerImage, searchSpinnerText);

        EditText searchField = (EditText) findViewById(R.id.search_term_text);
        searchField.requestFocus();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        searchField.addTextChangedListener(new SearchTextWatcher(listAdapter, searchSpinner));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
