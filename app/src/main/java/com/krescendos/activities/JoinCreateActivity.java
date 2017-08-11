package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.krescendos.R;

public class JoinCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.join_create_toolbar);
        setSupportActionBar(toolbar);

        Button joinButton = (Button) findViewById(R.id.joinButton);
        Button createButton = (Button) findViewById(R.id.createButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(intent);
            }
        });
    }
}
