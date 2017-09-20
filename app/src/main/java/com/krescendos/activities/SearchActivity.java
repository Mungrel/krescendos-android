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
import com.krescendos.domain.Party;
import com.krescendos.search.SearchSpinner;
import com.krescendos.search.SearchTextWatcher;
import com.krescendos.search.SearchTrackListAdapter;

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
        SearchTrackListAdapter listAdapter = new SearchTrackListAdapter(SearchActivity.this, party.getPlaylist(), party.getPartyId());

        ListView resultsView = (ListView) findViewById(R.id.search_result_list);
        resultsView.setAdapter(listAdapter);

        ImageView spinnerImage = (ImageView) findViewById(R.id.search_icon_spinner);
        SearchSpinner searchSpinner = new SearchSpinner(SearchActivity.this, spinnerImage);

        EditText searchField = (EditText) findViewById(R.id.search_term_text);
        searchField.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        searchField.addTextChangedListener(new SearchTextWatcher(SearchActivity.this, listAdapter, searchSpinner));

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
