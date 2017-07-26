package com.krescendos.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.Party;
import com.krescendos.domain.Track;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.web.Requester;
import com.krescendos.web.TrackChangeListener;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;

import java.util.ArrayList;
import java.util.List;

public class ClientPlayerActivity extends AppCompatActivity implements ConnectionStateCallback {
    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int SEARCH_CODE = 1234;

    private List<Track> trackList;
    private TrackListAdapter listAdapter;
    private Party party;
    private Requester requester;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_player);
        requester = new Requester(getApplicationContext());

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        ref = FirebaseDatabase.getInstance().getReference(party.getId());

        trackList = new ArrayList<Track>();
        listAdapter = new TrackListAdapter(getApplicationContext(), trackList);
        listAdapter.notifyDataSetChanged();
        ListView listView = (ListView) findViewById(R.id.client_playerList);
        listView.setAdapter(listAdapter);

        // Compatibility between versions
        if (getActionBar() != null) {
            getActionBar().setTitle(party.getName());
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(party.getName());
        }

        ref.addValueEventListener(new TrackChangeListener(listAdapter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTrackItem:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, SEARCH_CODE);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        switch (requestCode) {
            case SEARCH_CODE:
                Gson gson = new Gson();
                Track track = gson.fromJson(intent.getStringExtra("AddedTrack"), Track.class);
                Log.d("APPENDTRACK", "Track: " + track.getName());
                trackList.add(track);
                listAdapter.updateTracks(trackList);
                requester.append(party.getId(), track);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Log.d("HostPlayerActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("HostPlayerActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("HostPlayerActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("HostPlayerActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("HostPlayerActivity", "Received connection message: " + message);
    }
}