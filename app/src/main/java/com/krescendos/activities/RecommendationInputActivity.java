package com.krescendos.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;
import com.krescendos.buttons.RecommendButtonClickListener;
import com.krescendos.web.Requester;

public class RecommendationInputActivity extends AppCompatActivity {

    private Button[] buttons;

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

        buttons = new Button[8];
        buttons[0] = (Button) findViewById(R.id.recommend_button_1_1);
        buttons[1] = (Button) findViewById(R.id.recommend_button_1_2);
        buttons[2] = (Button) findViewById(R.id.recommend_button_2_1);
        buttons[3] = (Button) findViewById(R.id.recommend_button_2_2);
        buttons[4] = (Button) findViewById(R.id.recommend_button_3_1);
        buttons[5] = (Button) findViewById(R.id.recommend_button_3_2);
        buttons[6] = (Button) findViewById(R.id.recommend_button_4_1);
        buttons[7] = (Button) findViewById(R.id.recommend_button_4_2);

        Context context = getApplicationContext();
        buttons[0].setOnClickListener(new RecommendButtonClickListener(context, buttons, 0));
        buttons[1].setOnClickListener(new RecommendButtonClickListener(context, buttons, 1));
        buttons[2].setOnClickListener(new RecommendButtonClickListener(context, buttons, 2));
        buttons[3].setOnClickListener(new RecommendButtonClickListener(context, buttons, 3));
        buttons[4].setOnClickListener(new RecommendButtonClickListener(context, buttons, 4));
        buttons[5].setOnClickListener(new RecommendButtonClickListener(context, buttons, 5));
        buttons[6].setOnClickListener(new RecommendButtonClickListener(context, buttons, 6));
        buttons[7].setOnClickListener(new RecommendButtonClickListener(context, buttons, 7));

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
