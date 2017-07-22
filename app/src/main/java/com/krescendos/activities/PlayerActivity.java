package com.krescendos.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.Requester;
import com.krescendos.TrackListAdapter;
import com.krescendos.R;
import com.krescendos.TrackPlayer;
import com.krescendos.domain.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements ConnectionStateCallback
{
    private static final String CLIENT_ID = "aa1b7b09be0a44d88b57e72f2b269a88";
    private static final String REDIRECT_URI = "krescendosapp://callback";

    private TrackPlayer mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;
    private static final int SEARCH_CODE = 1234;

    private List<Track> trackList;
    private TrackListAdapter listAdapter;
    private Requester requester;

    private boolean isHost = false;
    private ImageButton playbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        isHost = getIntent().getBooleanExtra("isHost", false);
        Log.d("USER IS HOST: ", ""+isHost);

        requester = new Requester(getApplicationContext());

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ImageButton back = (ImageButton) findViewById(R.id.skpBkBtn);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mPlayer.previous();
            }
        });

        ImageButton fwd = (ImageButton) findViewById(R.id.skipFwdBtn);
        fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.next();
            }
        });

        playbtn = (ImageButton) findViewById(R.id.playBtn);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer.isPlaying()){
                    mPlayer.pause();
                } else {
                    mPlayer.play();
                }
                refreshPlayBtn();
            }
        });

        Type listType = new TypeToken<List<Track>>() {}.getType();
        trackList = new ArrayList<Track>();
        listAdapter = new TrackListAdapter(getApplicationContext(), trackList);
        listAdapter.notifyDataSetChanged();
        ListView listView = (ListView) findViewById(R.id.playerList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int targetTrackPos, long l) {
                mPlayer.skipTo(targetTrackPos);
                refreshPlayBtn();
            }
        });
    }

    private void refreshPlayBtn(){
        if (mPlayer.isPlaying()){
            playbtn.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playbtn.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addTrackItem:
                Log.d("HERE", "hello");
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
        switch (requestCode){
            case REQUEST_CODE:
                AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                    Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                        @Override
                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
                            mPlayer = new TrackPlayer(spotifyPlayer);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("PlayerActivity", "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;
            }
            case SEARCH_CODE:
                Gson gson = new Gson();
                Track track = gson.fromJson(intent.getStringExtra("AddedTrack"), Track.class);
                Log.d("APPENDTRACK", "Track: "+track.getName());

                trackList.add(track);
                listAdapter.updateTracks(trackList);
                mPlayer.queue(track);
                refreshPlayBtn();
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Log.d("PlayerActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("PlayerActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("PlayerActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("PlayerActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("PlayerActivity", "Received connection message: " + message);
    }
}