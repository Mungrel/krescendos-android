package com.krescendos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;
import com.krescendos.buttons.RecommendButtonClickListener;

public class RecommendationInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recommend_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.recommend);

        Button[] buttons = new Button[8];
        buttons[0] = (Button) findViewById(R.id.recommend_button_1_1);
        buttons[1] = (Button) findViewById(R.id.recommend_button_1_2);
        buttons[2] = (Button) findViewById(R.id.recommend_button_2_1);
        buttons[3] = (Button) findViewById(R.id.recommend_button_2_2);
        buttons[4] = (Button) findViewById(R.id.recommend_button_3_1);
        buttons[5] = (Button) findViewById(R.id.recommend_button_3_2);
        buttons[6] = (Button) findViewById(R.id.recommend_button_4_1);
        buttons[7] = (Button) findViewById(R.id.recommend_button_4_2);

        buttons[0].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 0));
        buttons[1].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 1));
        buttons[2].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 2));
        buttons[3].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 3));
        buttons[4].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 4));
        buttons[5].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 5));
        buttons[6].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 6));
        buttons[7].setOnClickListener(new RecommendButtonClickListener(RecommendationInputActivity.this, buttons, 7));

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
