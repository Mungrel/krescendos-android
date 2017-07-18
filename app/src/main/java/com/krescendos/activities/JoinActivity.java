package com.krescendos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.krescendos.R;

import org.w3c.dom.Text;

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
                submitCode(text);
            }
        });
    }

    private void submitCode(String joinCode){
        // Call some api or something
    }
}
