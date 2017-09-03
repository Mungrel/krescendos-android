package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.web.Requester;

import org.json.JSONObject;

public class CreateDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_details);
        final Requester requester = Requester.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_details_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.create);

        final Button partyCreate = (Button) findViewById(R.id.partyCreateButton);
        final EditText partyName = (EditText) findViewById(R.id.partyNameField);
        partyName.requestFocus();

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
                        Intent intent = new Intent(getApplicationContext(), CreateStartActivity.class);
                        intent.putExtra("party", response.toString());
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorText.setVisibility(View.VISIBLE);
                        partyCreate.setEnabled(true);
                        partyCreate.setText(R.string.create_short);
                    }
                });
            }
        });
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
