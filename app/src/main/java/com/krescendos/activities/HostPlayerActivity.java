package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.input.DislikeButtonClickListener;
import com.krescendos.input.LikeButtonClickListener;
import com.krescendos.model.Party;
import com.krescendos.model.Track;
import com.krescendos.player.OnTrackChangeListener;
import com.krescendos.player.SeekBarUserChangeListener;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.player.TrackPlayer;
import com.krescendos.text.TextUtils;
import com.krescendos.text.TimeUtils;
import com.krescendos.state.PlayheadIndexChangeListener;
import com.krescendos.state.PlaylistChangeListener;
import com.krescendos.state.StateUpdateRequestListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class HostPlayerActivity extends AppCompatActivity implements ConnectionStateCallback {
    private static final String CLIENT_ID = "aa1b7b09be0a44d88b57e72f2b269a88";
    private static final String REDIRECT_URI = "krescendosapp://callback";

    private TrackPlayer mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 1337;

    private TrackListAdapter listAdapter;
    private Party party;
    private DatabaseReference ref;

    private ImageButton playButton;
    private SeekBar seekBar;
    private TextView timeElapsed;
    private TextView timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_player);

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId()).orderByKey().getRef();

        LinearLayout currentTrackLayout = (LinearLayout) findViewById(R.id.host_current_track_layout);

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

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(party.getName());

        TextView partyCode = (TextView) findViewById(R.id.party_code);
        partyCode.setText(TextUtils.space(party.getPartyId()));

        timeElapsed = (TextView) findViewById(R.id.host_time_elapsed);
        timeRemaining = (TextView) findViewById(R.id.host_time_remaining);

        ImageButton like = (ImageButton) findViewById(R.id.host_like_button);
        ImageButton dislike = (ImageButton) findViewById(R.id.host_dislike_button);
        ImageButton add = (ImageButton) findViewById(R.id.host_add_button);

        like.setTag("off");
        dislike.setTag("off");

        like.setOnClickListener(new LikeButtonClickListener(like, dislike));
        dislike.setOnClickListener(new DislikeButtonClickListener(dislike, like));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostPlayerActivity.this, SearchActivity.class);
                intent.putExtra("party", new Gson().toJson(party));
                startActivityForResult(intent, SearchActivity.SEARCH_CODE);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        playButton = (ImageButton) findViewById(R.id.host_play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
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

        ListView listView = (ListView) findViewById(R.id.playerList);
        listAdapter = new TrackListAdapter(HostPlayerActivity.this, listView, currentTrackLayout);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int targetTrackPos, long l) {
                mPlayer.skipTo(targetTrackPos);
                refreshPlayBtn();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.host_seek_bar);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer != null && !mPlayer.isDragging()) {
                            seekBar.setMax((int) mPlayer.getCurrentTrackLength());
                            seekBar.setProgress((int) mPlayer.getCurrentTrackTime());
                            long remaining = mPlayer.getCurrentTrackLength() - mPlayer.getCurrentTrackTime();
                            timeElapsed.setText(TimeUtils.msTommss(mPlayer.getCurrentTrackTime()));
                            timeRemaining.setText(String.format("-%s", TimeUtils.msTommss(remaining)));
                            refreshPlayBtn();
                        }
                    }
                });
            }
        }, 0, 200);
    }

    private void refreshPlayBtn() {
        if (mPlayer.isPlaying()) {
            playButton.setImageResource(R.drawable.pause_button);
        } else {
            playButton.setImageResource(R.drawable.play_button);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_menu, menu);
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
                            mPlayer = new TrackPlayer(spotifyPlayer, HostPlayerActivity.this, party.getPartyId());
                            seekBar.setOnSeekBarChangeListener(new SeekBarUserChangeListener(mPlayer));
                            ref.child("playlist").addValueEventListener(new PlaylistChangeListener(listAdapter, mPlayer));
                            ref.child("playheadIndex").addValueEventListener(new PlayheadIndexChangeListener(listAdapter, seekBar));
                            ref.child("partyStateUpdateRequested").addValueEventListener(new StateUpdateRequestListener(HostPlayerActivity.this, party.getPartyId(), mPlayer));
                            mPlayer.setOnTrackChangeListener(new OnTrackChangeListener() {
                                @Override
                                public void onTrackChange(Track newTrack) {
                                    listAdapter.setCurrentPosition(mPlayer.getCurrentPos());
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("HostPlayerActivity", "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;


                } else if (response.getType() == AuthenticationResponse.Type.ERROR) {
                    Log.d("LOGINERROR", response.getError());
                }
            case SearchActivity.SEARCH_CODE:
                ScrollView scrollView = (ScrollView) findViewById(R.id.host_scroll_view);
                scrollView.smoothScrollTo(0, 0);
                refreshPlayBtn();
        }
    }

    @Override
    public void onBackPressed() {
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