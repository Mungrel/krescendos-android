package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.dialog.ConfirmDialog;
import com.krescendos.firebase.FirebaseRefs;
import com.krescendos.input.Keyboard;
import com.krescendos.model.Party;
import com.krescendos.model.Track;
import com.krescendos.player.CurrentlyPlayingAdapter;
import com.krescendos.player.SeekBarNoChangeListener;
import com.krescendos.player.UpNextAdapter;
import com.krescendos.state.AllowSuggestionsChangeListener;
import com.krescendos.state.CurrentlyPlayingChangeListener;
import com.krescendos.state.PartyStateChangeListener;
import com.krescendos.state.UpNextChangeListener;
import com.krescendos.utils.TextUtils;
import com.krescendos.utils.TimeUtils;
import com.krescendos.utils.UpdateTimer;
import com.krescendos.web.network.ConnectionLostListener;
import com.krescendos.web.network.NetworkChangeReceiver;
import com.krescendos.web.network.NetworkUtil;

import java.util.Timer;
import java.util.TimerTask;

public class ClientPlayerActivity extends AppCompatActivity {

    private Party party;
    private UpdateTimer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_player);

        party = getIntent().getExtras().getParcelable("party");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId());

        LinearLayout currentTrackLayout = (LinearLayout) findViewById(R.id.client_current_track_layout);
        LinearLayout upNextLayout = (LinearLayout) findViewById(R.id.client_playerList);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(party.getName());

        TextView partyCode = (TextView) findViewById(R.id.party_code);
        partyCode.setText(TextUtils.space(party.getPartyId()));

        final SeekBar seekBar = (SeekBar) findViewById(R.id.client_seek_bar);
        seekBar.setOnTouchListener(new SeekBarNoChangeListener());

        final UpNextAdapter upNextAdapter = new UpNextAdapter(ClientPlayerActivity.this, upNextLayout, party.getPartyId());
        final CurrentlyPlayingAdapter currentlyPlayingAdapter = new CurrentlyPlayingAdapter(ClientPlayerActivity.this, currentTrackLayout, seekBar);

        final TextView timeElapsed = (TextView) findViewById(R.id.client_time_elapsed);
        final TextView timeRemaining = (TextView) findViewById(R.id.client_time_remaining);

        updateTimer = new UpdateTimer();

        ref.child("currentlyPlaying").addValueEventListener(new CurrentlyPlayingChangeListener(currentlyPlayingAdapter));
        ref.child("playlist").orderByChild("voteCount").addChildEventListener(new UpNextChangeListener(upNextAdapter));
        ref.child("partyState").addValueEventListener(new PartyStateChangeListener(updateTimer));

        Timer UITimer = new Timer();
        UITimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentlyPlayingAdapter.getCurrentlyPlaying() == null) {
                            return;
                        }
                        Track currentTrack = currentlyPlayingAdapter.getCurrentlyPlaying().getItem();
                        long currentTrackDuration = currentTrack.getDuration_ms();
                        seekBar.setMax((int) currentTrackDuration);
                        seekBar.setProgress((int) updateTimer.getTime());
                        long remaining = currentTrackDuration - updateTimer.getTime();
                        timeElapsed.setText(TimeUtils.msTommss(updateTimer.getTime()));
                        timeRemaining.setText(String.format("-%s", TimeUtils.msTommss(remaining)));
                    }
                });
            }
        }, 0, UpdateTimer.REPEAT_TIME_MS);

        ImageButton add = (ImageButton) findViewById(R.id.client_add_button);
        View.OnClickListener baseAddListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientPlayerActivity.this, SearchActivity.class);
                intent.putExtra("party", new Gson().toJson(party));
                startActivityForResult(intent, SearchActivity.SEARCH_CODE);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        };
        add.setOnClickListener(baseAddListener);

        FirebaseRefs.getAllowSuggestionsRef(party.getPartyId()).addValueEventListener(new AllowSuggestionsChangeListener(ClientPlayerActivity.this, add, baseAddListener));

        if (party.getWelcomeMessage() != null && !party.getWelcomeMessage().isEmpty()) {
            ConfirmDialog dialog = new ConfirmDialog(ClientPlayerActivity.this, party.getName(), party.getWelcomeMessage());
            dialog.show();
        }

        NetworkChangeReceiver receiver = new NetworkChangeReceiver(new ConnectionLostListener() {
            @Override
            public void onNetworkConnectionLost() {
                finish();
            }
        });

        NetworkUtil.registerConnectivityReceiver(ClientPlayerActivity.this, receiver);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        switch (requestCode) {
            case SearchActivity.SEARCH_CODE:
                ScrollView scrollView = (ScrollView) findViewById(R.id.client_scroll_view);
                scrollView.smoothScrollTo(0, 0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Keyboard.hide(ClientPlayerActivity.this);
        NetworkUtil.unregisterConnectivityReceiver(ClientPlayerActivity.this);
        super.onDestroy();
    }
}