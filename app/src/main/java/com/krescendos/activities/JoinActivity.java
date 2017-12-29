package com.krescendos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.krescendos.R;
import com.krescendos.input.HideKeyboardListener;
import com.krescendos.input.Keyboard;
import com.krescendos.input.TextChangeListener;
import com.krescendos.model.Error;
import com.krescendos.model.Party;
import com.krescendos.web.Requester;
import com.krescendos.web.network.ConnectionLostListener;
import com.krescendos.web.network.NetworkChangeReceiver;
import com.krescendos.web.network.NetworkUtil;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        final Requester requester = Requester.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.join_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_text);
        toolbarTitle.setText(R.string.join);

        final Button joinCodeSubmit = (Button) findViewById(R.id.joinCodeSubmitButton);
        final EditText text1 = (EditText) findViewById(R.id.joinCode1);
        final EditText text2 = (EditText) findViewById(R.id.joinCode2);
        final EditText text3 = (EditText) findViewById(R.id.joinCode3);
        final EditText text4 = (EditText) findViewById(R.id.joinCode4);
        final EditText text5 = (EditText) findViewById(R.id.joinCode5);
        final EditText text6 = (EditText) findViewById(R.id.joinCode6);

        text1.requestFocus();

        text1.addTextChangedListener(new TextChangeListener(text1, text2));
        text2.addTextChangedListener(new TextChangeListener(text1, text3));
        text3.addTextChangedListener(new TextChangeListener(text2, text4));
        text4.addTextChangedListener(new TextChangeListener(text3, text5));
        text5.addTextChangedListener(new TextChangeListener(text4, text6));
        text6.addTextChangedListener(new TextChangeListener(text5, text6));
        text6.addTextChangedListener(new HideKeyboardListener(JoinActivity.this));

        final TextView errorText = (TextView) findViewById(R.id.joinErrorTextView);

        joinCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinCodeSubmit.setEnabled(false);
                joinCodeSubmit.setText(R.string.connecting);
                String text = text1.getText().toString();
                text += text2.getText().toString();
                text += text3.getText().toString();
                text += text4.getText().toString();
                text += text5.getText().toString();
                text += text6.getText().toString();

                errorText.setVisibility(View.INVISIBLE);
                requester.join(text, new Response.Listener<Party>() {
                    @Override
                    public void onResponse(Party response) {
                        Intent intent = new Intent(JoinActivity.this, ClientPlayerActivity.class);
                        intent.putExtra("party", response);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        joinCodeSubmit.setEnabled(true);
                        joinCodeSubmit.setText(R.string.join_short);
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
                        joinCodeSubmit.setEnabled(true);
                        joinCodeSubmit.setText(R.string.join_short);
                    }
                });
            }
        });

        NetworkChangeReceiver receiver = new NetworkChangeReceiver(new ConnectionLostListener() {
            @Override
            public void onNetworkConnectionLost() {
                finish();
            }
        });

        NetworkUtil.registerConnectivityReceiver(JoinActivity.this, receiver);

        //Keyboard.show(JoinActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Keyboard.hide(JoinActivity.this);
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TOUCH", "onTouchEvent");
        Keyboard.show(JoinActivity.this);

        return true;
    }

    @Override
    protected void onDestroy() {
        Log.d("DESTROY", "JoinActivity destroyed");
        NetworkUtil.unregisterConnectivityReceiver(JoinActivity.this);

        super.onDestroy();
    }
}
