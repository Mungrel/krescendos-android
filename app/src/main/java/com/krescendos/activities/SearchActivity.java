package com.krescendos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.Party;
import com.krescendos.search.SearchTextWatcher;
import com.krescendos.search.SearchTrackListAdapter;

public class SearchActivity extends AppCompatActivity {

    protected static final int SEARCH_CODE = 1234;

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
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_text);

        toolbarTitle.setText(party.getName());
        SearchTrackListAdapter listAdapter = new SearchTrackListAdapter(getApplicationContext(), party.getPlaylist(), party.getPartyId());

        ListView resultsView = (ListView) findViewById(R.id.search_result_list);
        resultsView.setAdapter(listAdapter);

        EditText searchField = (EditText) findViewById(R.id.search_term_text);
        searchField.requestFocus();
        searchField.addTextChangedListener(new SearchTextWatcher(getApplicationContext(), listAdapter));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
