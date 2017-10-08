package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.krescendos.model.Party;
import com.krescendos.player.SeekBarNoChangeListener;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.state.PartyStateChangeListener;
import com.krescendos.state.PlayheadIndexChangeListener;
import com.krescendos.state.PlaylistChangeListener;
import com.krescendos.text.TextUtils;
import com.krescendos.text.TimeUtils;
import com.krescendos.timer.UpdateTimer;
import com.krescendos.utils.QuickDialog;

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

        LinearLayout layout = (LinearLayout) findViewById(R.id.client_current_track_layout);
        LinearLayout playlistLayout = (LinearLayout) findViewById(R.id.client_playerList);
        final TrackListAdapter listAdapter = new TrackListAdapter(ClientPlayerActivity.this, playlistLayout, layout);
        listAdapter.setItemsSelectable(false);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(party.getName());

        TextView partyCode = (TextView) findViewById(R.id.party_code);
        partyCode.setText(TextUtils.space(party.getPartyId()));

        final SeekBar seekBar = (SeekBar) findViewById(R.id.client_seek_bar);
        seekBar.setOnTouchListener(new SeekBarNoChangeListener());

        final TextView timeElapsed = (TextView) findViewById(R.id.client_time_elapsed);
        final TextView timeRemaining = (TextView) findViewById(R.id.client_time_remaining);

        updateTimer = new UpdateTimer();

        ref.child("partyState").addValueEventListener(new PartyStateChangeListener(updateTimer));
        ref.child("playheadIndex").addValueEventListener(new PlayheadIndexChangeListener(listAdapter, seekBar));
        ref.child("playlist").addChildEventListener(new PlaylistChangeListener(listAdapter));


        Timer UITimer = new Timer();
        UITimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listAdapter.getTracks().isEmpty()) {
                            return;
                        }
                        long currentTrackDuration = listAdapter.getTracks().get(listAdapter.getCurrentPosition()).getDuration_ms();
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientPlayerActivity.this, SearchActivity.class);
                intent.putExtra("party", new Gson().toJson(party));
                startActivityForResult(intent, SearchActivity.SEARCH_CODE);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        if (party.getWelcomeMessage() != null && !party.getWelcomeMessage().isEmpty()) {
            QuickDialog dialog = new QuickDialog(ClientPlayerActivity.this, party.getName(), party.getWelcomeMessage());
            dialog.show();
        }

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
}