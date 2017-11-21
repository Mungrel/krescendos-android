package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.input.Keyboard;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.search.SearchTrackListAdapter;

public class PlaylistTracksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_tracks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.playlist_tracks_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Playlist playlist = new Gson().fromJson(getIntent().getStringExtra("playlist"), Playlist.class);
        String partyID = getIntent().getStringExtra("partyID");

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(playlist.getName());

        SearchTrackListAdapter listAdapter = new SearchTrackListAdapter(PlaylistTracksActivity.this, playlist.getTracks(), partyID);

        ListView resultsView = (ListView) findViewById(R.id.playlist_tracks_list);
        resultsView.setAdapter(listAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Keyboard.hide(PlaylistTracksActivity.this);
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
