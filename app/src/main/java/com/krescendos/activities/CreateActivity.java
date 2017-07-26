package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.Requester;

import org.json.JSONObject;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        final Requester requester = new Requester(getApplicationContext());

        Button partyCreate = (Button) findViewById(R.id.partyCreateButton);
        final EditText partyName = (EditText) findViewById(R.id.partyNameField);

        final TextView errorText = (TextView) findViewById(R.id.createErrorTextView);

        partyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = partyName.getText().toString();
                requester.create(name, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), HostPlayerActivity.class);
                        intent.putExtra("party", response.toString());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorText.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
