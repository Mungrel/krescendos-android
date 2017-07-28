package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.web.Requester;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        final Requester requester = new Requester(getApplicationContext());

        Button joinCodeSubmit = (Button) findViewById(R.id.joinCodeSubmitButton);
        final EditText joinCodeTextField = (EditText) findViewById(R.id.joinCodeField);

        final TextView errorText = (TextView) findViewById(R.id.joinErrorTextView);

        joinCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = joinCodeTextField.getText().toString();
                errorText.setVisibility(View.INVISIBLE);
                requester.join(text, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), ClientPlayerActivity.class);
                        intent.putExtra("party", response.toString());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", ""+error.getMessage());
                        errorText.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
