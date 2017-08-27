package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.Party;
import com.krescendos.player.SeekBarNoChangeListener;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.timer.OnTimerUpdateListener;
import com.krescendos.timer.UpdateTimer;
import com.krescendos.web.PartyStateChangeListener;
import com.krescendos.web.PlayheadIndexChangeListener;
import com.krescendos.web.PlaylistChangeListener;

public class ClientPlayerActivity extends AppCompatActivity {

    private Party party;
    private UpdateTimer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_player);

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId());

        TrackListAdapter listAdapter = new TrackListAdapter(getApplicationContext());
        listAdapter.setItemsSelectable(false);

        ListView listView = (ListView) findViewById(R.id.client_playerList);
        listView.setAdapter(listAdapter);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.client_seek_bar);
        seekBar.setOnTouchListener(new SeekBarNoChangeListener());

        updateTimer = new UpdateTimer(new OnTimerUpdateListener() {
            @Override
            public void onUpdate(final long time) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time < seekBar.getMax()){
                            seekBar.setProgress((int)time);
                        }
                    }
                });
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.client_current_track_layout);
        ref.child("playlist").addValueEventListener(new PlaylistChangeListener(listAdapter));
        ref.child("playheadIndex").addValueEventListener(new PlayheadIndexChangeListener(getApplicationContext(), layout, listAdapter));
        ref.child("partyState").addValueEventListener(new PartyStateChangeListener(seekBar, listAdapter, updateTimer));

        // Compatibility between versions
        if (getActionBar() != null) {
            getActionBar().setTitle(party.getName());
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(party.getName());
        }

        ImageButton add = (ImageButton) findViewById(R.id.client_add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("party", new Gson().toJson(party));
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


    }
}