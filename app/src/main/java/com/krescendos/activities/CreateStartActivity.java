package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krescendos.R;
import com.krescendos.input.Keyboard;
import com.krescendos.input.UnimplementedInputListener;
import com.krescendos.model.Party;
import com.krescendos.utils.TextUtils;
import com.krescendos.web.network.ConnectionLostListener;
import com.krescendos.web.network.NetworkChangeReceiver;
import com.krescendos.web.network.NetworkUtil;

public class CreateStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_start);

        final Party party = getIntent().getExtras().getParcelable("party");

        TextView title = (TextView) findViewById(R.id.title_text);
        TextView titleCode = (TextView) findViewById(R.id.party_code);

        title.setText(party.getName());
        titleCode.setText(TextUtils.space(party.getPartyId()));

        LinearLayout layout = (LinearLayout) findViewById(R.id.create_start_title_bar);

        EditText code1 = layout.findViewById(R.id.joinCode1);
        EditText code2 = layout.findViewById(R.id.joinCode2);
        EditText code3 = layout.findViewById(R.id.joinCode3);
        EditText code4 = layout.findViewById(R.id.joinCode4);
        EditText code5 = layout.findViewById(R.id.joinCode5);
        EditText code6 = layout.findViewById(R.id.joinCode6);

        char[] partyCode = party.getPartyId().toCharArray();

        code1.setEnabled(false);
        code1.setFocusable(false);
        code2.setEnabled(false);
        code2.setFocusable(false);
        code3.setEnabled(false);
        code3.setFocusable(false);
        code4.setEnabled(false);
        code4.setFocusable(false);
        code5.setEnabled(false);
        code5.setFocusable(false);
        code6.setEnabled(false);
        code6.setFocusable(false);

        code1.setText(String.format("%c", partyCode[0]));
        code2.setText(String.format("%c", partyCode[1]));
        code3.setText(String.format("%c", partyCode[2]));
        code4.setText(String.format("%c", partyCode[3]));
        code5.setText(String.format("%c", partyCode[4]));
        code6.setText(String.format("%c", partyCode[5]));

        ImageButton share = (ImageButton) findViewById(R.id.share_code_button);
        Button importSpotifyPlaylist = (Button) findViewById(R.id.import_spotify_playlist_button);
        Button krescendosRecommend = (Button) findViewById(R.id.krescendos_recommend_button);
        Button addSongsManually = (Button) findViewById(R.id.manual_add_button);

        importSpotifyPlaylist.setOnClickListener(new UnimplementedInputListener(this));
        krescendosRecommend.setOnClickListener(new UnimplementedInputListener(this));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Code: " + party.getPartyId();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Krescendos Party");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        addSongsManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateStartActivity.this, HostPlayerActivity.class);
                intent.putExtra("party", party);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        NetworkChangeReceiver receiver = new NetworkChangeReceiver(new ConnectionLostListener() {
            @Override
            public void onNetworkConnectionLost() {
                finish();
            }
        });

        NetworkUtil.registerConnectivityReceiver(CreateStartActivity.this, receiver);

    }

    @Override
    protected void onDestroy() {
        Keyboard.hide(CreateStartActivity.this);
        NetworkUtil.unregisterConnectivityReceiver(CreateStartActivity.this);
        super.onDestroy();
    }
}
