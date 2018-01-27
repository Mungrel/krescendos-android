package com.krescendos.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.krescendos.R;
import com.krescendos.local.PersistenceConstants;
import com.krescendos.local.PersistenceManager;
import com.krescendos.model.Party;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.Requester;
import com.krescendos.web.async.AsyncResponseListener;

public class JoinCreateActivity extends AppCompatActivity {

    private Intent rejoinAsHostIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_create);

        Button joinButton = (Button) findViewById(R.id.joinButton);
        Button createButton = (Button) findViewById(R.id.createButton);
        final Button rejoinButton = (Button) findViewById(R.id.rejoinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinCreateActivity.this, JoinActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinCreateActivity.this, CreateDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        // Retrieve last hosted party code from disk
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PersistenceManager.ID_PREFS, 0);
        String lasthostedID = pref.getString(PersistenceConstants.LAST_ID_HOSTED, "");

        if (!lasthostedID.isEmpty()) {
            Requester.getInstance().join(lasthostedID, new AsyncResponseListener<Party>() {
                @Override
                public void onResponse(Party response) {
                    rejoinAsHostIntent = new Intent(JoinCreateActivity.this, HostPlayerActivity.class);
                    rejoinAsHostIntent.putExtra("party", response);

                    rejoinButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(rejoinAsHostIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        }
                    });

                    rejoinButton.setVisibility(View.VISIBLE);
                }
            }, new DefaultErrorListener());
        }
    }

}
