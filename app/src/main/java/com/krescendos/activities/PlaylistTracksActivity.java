package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.model.Party;

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

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
    }
}
