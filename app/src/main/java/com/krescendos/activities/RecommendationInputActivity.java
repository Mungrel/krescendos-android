package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;
import com.krescendos.buttons.RecommendButtonClickListener;
import com.krescendos.web.Requester;

import java.util.List;

public class RecommendationInputActivity extends AppCompatActivity {

    private Button opt0;
    private Button opt1;
    private Button opt2;
    private Button opt3;
    private Button opt4;
    private Button opt5;
    private Button opt6;
    private Button opt7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recommend_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.recommend);

        Requester requester = Requester.getInstance(getApplicationContext());

        opt0 = (Button) findViewById(R.id.recommend_button_1_1);
        opt1 = (Button) findViewById(R.id.recommend_button_1_2);
        opt2 = (Button) findViewById(R.id.recommend_button_2_1);
        opt3 = (Button) findViewById(R.id.recommend_button_2_2);
        opt4 = (Button) findViewById(R.id.recommend_button_3_1);
        opt5 = (Button) findViewById(R.id.recommend_button_3_2);
        opt6 = (Button) findViewById(R.id.recommend_button_4_1);
        opt7 = (Button) findViewById(R.id.recommend_button_4_2);

        opt0.setOnClickListener(new RecommendButtonClickListener(opt0));
        opt1.setOnClickListener(new RecommendButtonClickListener(opt1));
        opt2.setOnClickListener(new RecommendButtonClickListener(opt2));
        opt3.setOnClickListener(new RecommendButtonClickListener(opt3));
        opt4.setOnClickListener(new RecommendButtonClickListener(opt4));
        opt5.setOnClickListener(new RecommendButtonClickListener(opt5));
        opt6.setOnClickListener(new RecommendButtonClickListener(opt6));
        opt7.setOnClickListener(new RecommendButtonClickListener(opt7));

        Button doneButton = (Button) findViewById(R.id.recommend_done_button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
