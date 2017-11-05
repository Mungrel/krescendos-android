package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.dialog.OnQuickDialogCloseListener;
import com.krescendos.dialog.QuickDialog;
import com.krescendos.firebase.FirebaseManager;
import com.krescendos.model.Party;
import com.krescendos.model.Profile;
import com.krescendos.player.CurrentlyPlayingAdapter;
import com.krescendos.player.SeekBarUserChangeListener;
import com.krescendos.player.TrackPlayer;
import com.krescendos.player.UpNextAdapter;
import com.krescendos.state.CurrentlyPlayingChangeListener;
import com.krescendos.state.StateUpdateRequestListener;
import com.krescendos.state.UpNextChangeListener;
import com.krescendos.utils.TextUtils;
import com.krescendos.utils.TimeUtils;
import com.krescendos.web.Requester;
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
    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 1337;

    private UpNextAdapter upNextAdapter;
    private CurrentlyPlayingAdapter currentlyPlayingAdapter;
    private TrackPlayer mPlayer;
    private Requester requester;
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

        party = getIntent().getExtras().getParcelable("party");

        requester = Requester.getInstance(HostPlayerActivity.this);

        ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId()).orderByKey().getRef();

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ImageButton fwd = (ImageButton) findViewById(R.id.host_skip_button);
        fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseManager.advancePlayhead(party.getPartyId());
            }
        });

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(party.getName());

        TextView partyCode = (TextView) findViewById(R.id.party_code);
        partyCode.setText(TextUtils.space(party.getPartyId()));

        timeElapsed = (TextView) findViewById(R.id.host_time_elapsed);
        timeRemaining = (TextView) findViewById(R.id.host_time_remaining);

        ImageButton add = (ImageButton) findViewById(R.id.host_add_button);

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

        LinearLayout currentTrackLayout = (LinearLayout) findViewById(R.id.host_current_track_layout);
        LinearLayout upNextLayout = (LinearLayout) findViewById(R.id.playerList);
        seekBar = (SeekBar) findViewById(R.id.host_seek_bar);

        upNextAdapter = new UpNextAdapter(HostPlayerActivity.this, upNextLayout, party.getPartyId());
        currentlyPlayingAdapter = new CurrentlyPlayingAdapter(HostPlayerActivity.this, currentTrackLayout, seekBar);

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
                final AuthenticationResponse authenticationResponse = AuthenticationClient.getResponse(resultCode, intent);
                if (authenticationResponse.getType() == AuthenticationResponse.Type.TOKEN) {
                    requester.isPremiumUser(authenticationResponse.getAccessToken(), new Response.Listener<Profile>() {
                        @Override
                        public void onResponse(Profile response) {
                            if (!response.isPremiumUser()) {
                                QuickDialog quickDialog = new QuickDialog(HostPlayerActivity.this,
                                        "Spotify Premium", "A Spotify Premium account is required to host a party.",
                                        new OnQuickDialogCloseListener() {
                                            @Override
                                            public void onClose() {
                                                finish();
                                            }
                                        });
                                quickDialog.show();
                            }
                        }
                    });
                    Config playerConfig = new Config(this, authenticationResponse.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                        @Override
                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
                            mPlayer = new TrackPlayer(spotifyPlayer, HostPlayerActivity.this, party.getPartyId());
                            seekBar.setOnSeekBarChangeListener(new SeekBarUserChangeListener(mPlayer));

                            ref.child("playlist").orderByChild("voteCount").addChildEventListener(new UpNextChangeListener(upNextAdapter));
                            ref.child("currentlyPlaying").addValueEventListener(new CurrentlyPlayingChangeListener(currentlyPlayingAdapter, mPlayer, HostPlayerActivity.this, party.getPartyId()));
                            ref.child("partyStateUpdateRequested").addValueEventListener(new StateUpdateRequestListener(party.getPartyId(), mPlayer));
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("HostPlayerActivity", "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;


                } else if (authenticationResponse.getType() == AuthenticationResponse.Type.ERROR) {
                    Log.d("LOGINERROR", authenticationResponse.getError());
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