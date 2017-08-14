package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.AlbumArt;
import com.krescendos.domain.Party;
import com.krescendos.domain.Track;
import com.krescendos.player.OnTrackChangeListener;
import com.krescendos.player.SeekBarChangeListener;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.player.TrackPlayer;
import com.krescendos.text.Joiner;
import com.krescendos.web.PlaylistChangeListener;
import com.krescendos.web.Requester;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HostPlayerActivity extends AppCompatActivity implements ConnectionStateCallback {
    private static final String CLIENT_ID = "aa1b7b09be0a44d88b57e72f2b269a88";
    private static final String REDIRECT_URI = "krescendosapp://callback";

    private TrackPlayer mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 1337;
    private static final int SEARCH_CODE = 1234;

    private TrackListAdapter listAdapter;
    private Requester requester;
    private boolean liked = false;

    private ImageButton playbtn;
    private SeekBar seekBar;
    private NetworkImageView albumArt;
    private TextView trackTitle;
    private TextView artistAlbum;
    private Party party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_player);
        requester = new Requester(getApplicationContext());

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId()).orderByKey().getRef();

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ImageButton fwd = (ImageButton) findViewById(R.id.host_skip_button);
        fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.next();
            }
        });

        TextView title = (TextView) findViewById(R.id.host_title_text);
        title.setText(party.getName());

        final ImageButton like = (ImageButton) findViewById(R.id.host_like_button);
        like.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                liked = !liked;
                if (liked){
                    like.setImageResource(R.drawable.like_button_on);
                } else {
                    like.setImageResource(R.drawable.like_button_off);
                }

            }
        });

        albumArt = (NetworkImageView) findViewById(R.id.host_album_image);
        trackTitle = (TextView) findViewById(R.id.host_current_track_title);
        artistAlbum = (TextView) findViewById(R.id.host_current_track_artist_album);

        playbtn = (ImageButton) findViewById(R.id.host_play_button);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else {
                    mPlayer.play();
                }
                refreshPlayBtn();
            }
        });

        listAdapter = new TrackListAdapter(getApplicationContext());
        final ListView listView = (ListView) findViewById(R.id.playerList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int targetTrackPos, long l) {
                mPlayer.skipTo(targetTrackPos);
                refreshPlayBtn();
            }
        });

        ref.child("playlist").addValueEventListener(new PlaylistChangeListener(listAdapter));

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(100);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mPlayer != null) {
                    seekBar.setProgress(mPlayer.getProgressPercent());
                }
            }
        }, 0, 300);

        seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener(mPlayer));

        // Compatibility between versions
        if (getActionBar() != null) {
            getActionBar().setTitle(party.getName());
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(party.getName());
        }
    }

    private void refreshPlayBtn() {
        if (mPlayer.isPlaying()) {
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
            case REQUEST_CODE:
                AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                    Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                        @Override
                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
                            mPlayer = new TrackPlayer(spotifyPlayer, getApplicationContext(), party.getPartyId());
                            mPlayer.setOnTrackChangeListener(new OnTrackChangeListener() {
                                @Override
                                public void onTrackChange(Track newTrack) {
                                    listAdapter.setCurrentPlayingId(newTrack.getId());
                                    listAdapter.notifyDataSetChanged();
                                    List<AlbumArt> images = newTrack.getAlbum().getImages();
                                    AlbumArt largestImage = images.get(images.size()-1);
                                    albumArt.setImageUrl(largestImage.getUrl(), requester.getImageLoader());
                                    trackTitle.setText(newTrack.getName());
                                    artistAlbum.setText(Joiner.join(newTrack.getArtists())+"  -  "+newTrack.getAlbum().getName());
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("HostPlayerActivity", "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;
                }
            case SEARCH_CODE:
                Gson gson = new Gson();
                Track track = gson.fromJson(intent.getStringExtra("AddedTrack"), Track.class);
                Log.d("APPENDTRACK", "Track: " + track.getName());
                mPlayer.queue(track); // Queue will call requester.append()
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