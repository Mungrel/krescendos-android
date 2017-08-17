package com.krescendos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.krescendos.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final int targetIndex = getIntent().getIntExtra(TargetActivityIndex.INTENT_KEY, -1);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = fromIndex(targetIndex);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }
        };

        splashThread.start();
    }

    private Intent fromIndex(int index){
        switch (index) {
            case TargetActivityIndex.CLIENT_PLAYER:
                return new Intent(getApplicationContext(), ClientPlayerActivity.class);
            case TargetActivityIndex.CREATE_DETAILS:
                return new Intent(getApplicationContext(), CreateDetailsActivity.class);
            case TargetActivityIndex.CREATE_START:
                return new Intent(getApplicationContext(), CreateStartActivity.class);
            case TargetActivityIndex.HOST_PLAYER:
                return new Intent(getApplicationContext(), HostPlayerActivity.class);
            case TargetActivityIndex.JOIN:
                return new Intent(getApplicationContext(), JoinActivity.class);
            case TargetActivityIndex.JOIN_CREATE:
                return new Intent(getApplicationContext(), JoinCreateActivity.class);
            case TargetActivityIndex.SEARCH:
                return new Intent(getApplicationContext(), SearchActivity.class);
            default:
                return new Intent(getApplicationContext(), JoinCreateActivity.class);
        }
    }

}
