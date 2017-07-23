package com.krescendos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button partyCreate = (Button) findViewById(R.id.partyCreateButton);
        final TextView partyName = (TextView) findViewById(R.id.partyNameField);

        partyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = partyName.getText().toString();
                boolean success = createParty(name);

                if (success){
                    Intent intent = new Intent(getApplicationContext(), HostPlayerActivity.class);
                    intent.putExtra("isHost", true);
                    startActivity(intent);
                }
            }
        });
    }
// Returns true if party successfully created
    private boolean createParty(String text){
        Log.d("CREATING: ",text);
        // some api call
        return true;
    }
}
