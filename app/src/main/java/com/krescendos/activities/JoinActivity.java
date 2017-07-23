package com.krescendos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button joinCodeSubmit = (Button) findViewById(R.id.joinCodeSubmitButton);
        final TextView joinCodeTextField = (TextView) findViewById(R.id.joinCodeField);

        joinCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = joinCodeTextField.getText().toString();
                boolean success = joinParty(text);

                if (success){
                    Intent intent = new Intent(getApplicationContext(), ClientPlayerActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
// Returns true if party successfully joined
    private boolean joinParty(String joinCode){
        Log.d("JOINING: ", joinCode);
        // Call some api or something
        return true;
    }
}
