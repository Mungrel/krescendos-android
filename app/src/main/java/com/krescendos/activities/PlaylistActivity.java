package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.playlist.PlaylistAdapter;
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

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(String.format("%s%s", spotifyUsername, getString(R.string.playlists_title)));

        final PlaylistAdapter listAdapter = new PlaylistAdapter(PlaylistActivity.this);

        final ListView resultsView = (ListView) findViewById(R.id.playlists_list);
        resultsView.setAdapter(listAdapter);

        Requester requester = Requester.getInstance(PlaylistActivity.this);
        requester.userPlaylists(spotifyUsername, new Response.Listener<List<Playlist>>() {
            @Override
            public void onResponse(List<Playlist> response) {
                listAdapter.updateResults(response);
            }
        });

    }
}
