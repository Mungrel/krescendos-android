package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.Party;
import com.krescendos.player.TrackListAdapter;
import com.krescendos.web.PlayheadIndexChangeListener;
import com.krescendos.web.PlaylistChangeListener;

public class ClientPlayerActivity extends AppCompatActivity {

    private Party party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_player);

        party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("party").child(party.getPartyId()).orderByKey().getRef();

        TrackListAdapter listAdapter = new TrackListAdapter(getApplicationContext());
        listAdapter.setItemsSelectable(false);

        ListView listView = (ListView) findViewById(R.id.client_playerList);
        listView.setAdapter(listAdapter);

        LinearLayout layout = (LinearLayout) findViewById(R.id.client_current_track_layout);
        ref.child("playlist").addValueEventListener(new PlaylistChangeListener(listAdapter));
        ref.child("playheadIndex").addValueEventListener(new PlayheadIndexChangeListener(getApplicationContext(), layout, listAdapter));
        ref.child("partyState").addValueEventListener(new )

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