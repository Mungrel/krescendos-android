package com.krescendos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.krescendos.R;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.playlist.PlaylistAdapter;
import com.krescendos.search.SearchSpinner;
import com.krescendos.web.Requester;

import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String spotifyUsername = getIntent().getStringExtra("username");
        Party party = getIntent().getExtras().getParcelable("party");

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(String.format("%s%s", spotifyUsername, getString(R.string.playlists_title)));

        final PlaylistAdapter listAdapter = new PlaylistAdapter(PlaylistActivity.this, party.getPartyId());

        final ListView resultsView = (ListView) findViewById(R.id.playlists_list);
        resultsView.setAdapter(listAdapter);

        ImageView icon = (ImageView) findViewById(R.id.playlist_icon_spinner);

        final TextView noneFound = (TextView) findViewById(R.id.playlists_none_found);

        final SearchSpinner spinner = new SearchSpinner(PlaylistActivity.this, icon);
        spinner.start();

        Requester requester = Requester.getInstance();
        requester.userPlaylists(spotifyUsername, new Response.Listener<List<Playlist>>() {
            @Override
            public void onResponse(List<Playlist> response) {
                spinner.hide();

                if (response.isEmpty()) {
                    noneFound.setVisibility(View.VISIBLE);
                } else {
                    listAdapter.updateResults(response);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
