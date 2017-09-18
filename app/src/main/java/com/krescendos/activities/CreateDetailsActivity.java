package com.krescendos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ServiceWorkerClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.domain.Error;
import com.krescendos.web.Requester;
import com.krescendos.web.UnimplementedClickListener;

import org.json.JSONObject;

public class CreateDetailsActivity extends AppCompatActivity {

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_details);
        final Requester requester = new Requester(CreateDetailsActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_details_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.create);

        final Button partyCreate = (Button) findViewById(R.id.partyCreateButton);
        final EditText partyName = (EditText) findViewById(R.id.partyNameField);
        partyName.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        final TextView errorText = (TextView) findViewById(R.id.createErrorTextView);

        partyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = partyName.getText().toString();
                errorText.setVisibility(View.INVISIBLE);
                partyCreate.setEnabled(false);
                partyCreate.setText(R.string.connecting);
                requester.create(name, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(CreateDetailsActivity.this, CreateStartActivity.class);
                        intent.putExtra("party", response.toString());
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        partyCreate.setEnabled(true);
                        partyCreate.setText(R.string.create_short);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Error error = Error.fromVolleyError(volleyError);
                        if (error != null) {
                            Log.e("ERROR", error.getStatus() + ": " + error.getMessage());
                            errorText.setText(error.getUserMessage());
                        }
                        errorText.setVisibility(View.VISIBLE);
                        partyCreate.setEnabled(true);
                        partyCreate.setText(R.string.create_short);
                    }
                });
            }
        });

        Switch autoSuggest = (Switch) findViewById(R.id.autoSuggestSwitch);
        Switch othersSuggest =  (Switch) findViewById(R.id.othersSuggestSwitch);

        autoSuggest.setOnClickListener(new UnimplementedClickListener(CreateDetailsActivity.this));
        othersSuggest.setOnClickListener(new UnimplementedClickListener(CreateDetailsActivity.this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
