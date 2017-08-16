package com.krescendos.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krescendos.R;
import com.krescendos.domain.Party;
import com.krescendos.web.UnimplementedClickListener;

public class CreateStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_start);

        Party party = new Gson().fromJson(getIntent().getStringExtra("party"), Party.class);

        TextView title = (TextView) findViewById(R.id.host_title_text);
        TextView titleCode = (TextView) findViewById(R.id.host_party_code);

        title.setText(party.getName());
        titleCode.setText(party.getPartyId());

        LinearLayout layout = (LinearLayout) findViewById(R.id.create_start_title_bar);

        EditText code1 = (EditText) layout.findViewById(R.id.joinCode1);
        EditText code2 = (EditText) layout.findViewById(R.id.joinCode2);
        EditText code3 = (EditText) layout.findViewById(R.id.joinCode3);
        EditText code4 = (EditText) layout.findViewById(R.id.joinCode4);
        EditText code5 = (EditText) layout.findViewById(R.id.joinCode5);
        EditText code6 = (EditText) layout.findViewById(R.id.joinCode6);

        char[] partyCode = party.getPartyId().toCharArray();

        code1.setText(""+partyCode[0]);
        code2.setText(""+partyCode[1]);
        code3.setText(""+partyCode[2]);
        code4.setText(""+partyCode[3]);
        code5.setText(""+partyCode[4]);
        code6.setText(""+partyCode[5]);

        ImageButton share = (ImageButton) findViewById(R.id.share_code_button);
        Button importSpotifyPlaylist = (Button) findViewById(R.id.import_spotify_playlist_button);
        Button krescendosRecommend = (Button) findViewById(R.id.krescendos_recommend_button);
        Button addSongsManually = (Button) findViewById(R.id.manual_add_button);

        Context context = getApplicationContext();

        share.setOnClickListener(new UnimplementedClickListener(context));
        importSpotifyPlaylist.setOnClickListener(new UnimplementedClickListener(context));
        krescendosRecommend.setOnClickListener(new UnimplementedClickListener(context));

        addSongsManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HostPlayerActivity.class);
                intent.putExtra("party", getIntent().getStringExtra("party"));
                startActivity(intent);
            }
        });

    }
}
