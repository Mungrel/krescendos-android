package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.krescendos.domain.Party;
import com.krescendos.player.SeekBarNoChangeListener;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.text.TextUtils;
import com.krescendos.timer.UpdateTimer;
import com.krescendos.web.PartyStateChangeListener;
import com.krescendos.web.PlayheadIndexChangeListener;
import com.krescendos.web.PlaylistChangeListener;

import java.util.Timer;
import java.util.TimerTask;

public class ClientPlayerActivity extends AppCompatActivity {

    private Party party;
    private UpdateTimer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_player);

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId());

        ListView listView = (ListView) findViewById(R.id.client_playerList);
        final TrackListAdapter listAdapter = new TrackListAdapter(getApplicationContext(), listView);
        listAdapter.setItemsSelectable(false);
        listView.setAdapter(listAdapter);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(party.getName());

        TextView partyCode = (TextView) findViewById(R.id.party_code);
        partyCode.setText(TextUtils.space(party.getPartyId()));

        final SeekBar seekBar = (SeekBar) findViewById(R.id.client_seek_bar);
        seekBar.setOnTouchListener(new SeekBarNoChangeListener());

        updateTimer = new UpdateTimer();

        LinearLayout layout = (LinearLayout) findViewById(R.id.client_current_track_layout);
        DatabaseReference playHeadIndexRef = ref.child("playheadIndex");
        DatabaseReference partyStateRef = ref.child("partyState");
        PlayheadIndexChangeListener playheadIndexChangeListener = new PlayheadIndexChangeListener(getApplicationContext(), layout, listAdapter, seekBar);
        PartyStateChangeListener partyStateChangeListener = new PartyStateChangeListener(updateTimer);
        ref.child("playlist").addValueEventListener(new PlaylistChangeListener(getApplicationContext(), party.getPartyId(), listAdapter,
                playHeadIndexRef, partyStateRef, playheadIndexChangeListener, partyStateChangeListener));


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
                    }
                });
            }
        }, 0, UpdateTimer.REPEAT_TIME_MS);

        ImageButton add = (ImageButton) findViewById(R.id.client_add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("party", new Gson().toJson(party));
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, SearchActivity.SEARCH_CODE);
            }
        });
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