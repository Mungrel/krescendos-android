package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.input.UnimplementedInputListener;
import com.krescendos.model.Error;
import com.krescendos.model.Party;
import com.krescendos.web.Requester;
import com.krescendos.web.async.AsyncResponseListener;
import com.krescendos.web.network.ConnectionLostListener;
import com.krescendos.web.network.NetworkChangeReceiver;
import com.krescendos.web.network.NetworkUtil;

public class CreateDetailsActivity extends AppCompatActivity {

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_details);
        final Requester requester = Requester.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_details_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.create);

        final Button partyCreate = (Button) findViewById(R.id.partyCreateButton);
        final EditText partyName = (EditText) findViewById(R.id.partyNameField);
        final EditText partyWelcomeMessage = (EditText) findViewById(R.id.partyWelcomeField);

        Switch autoSuggest = (Switch) findViewById(R.id.autoSuggestSwitch);
        final Switch othersSuggest = (Switch) findViewById(R.id.othersSuggestSwitch);

        partyName.requestFocus();

        final TextView errorText = (TextView) findViewById(R.id.createErrorTextView);

        partyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = partyName.getText().toString();
                String welcomeMessage = partyWelcomeMessage.getText().toString();
                errorText.setVisibility(View.INVISIBLE);
                partyCreate.setEnabled(false);
                partyCreate.setText(R.string.connecting);
                requester.create(name, welcomeMessage, othersSuggest.isChecked(), new AsyncResponseListener<Party>() {
                    @Override
                    public void onResponse(Party response) {
                        Intent intent = new Intent(CreateDetailsActivity.this, CreateStartActivity.class);
                        intent.putExtra("party", response);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        partyCreate.setEnabled(true);
                        partyCreate.setText(R.string.create_short);
                    }
                });
            }
        });


        autoSuggest.setOnTouchListener(new UnimplementedInputListener(CreateDetailsActivity.this));

        NetworkChangeReceiver receiver = new NetworkChangeReceiver(new ConnectionLostListener() {
            @Override
            public void onNetworkConnectionLost() {
                finish();
            }
        });

        NetworkUtil.registerConnectivityReceiver(CreateDetailsActivity.this, receiver);

    }

    @Override
    protected void onDestroy() {
        NetworkUtil.unregisterConnectivityReceiver(CreateDetailsActivity.this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
