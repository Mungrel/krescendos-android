package com.krescendos.buttons;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.krescendos.R;
import com.krescendos.web.Requester;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendButtonClickListener implements View.OnClickListener {

    private static List<String> userSelection;
    private Button button;
    private boolean buttonOn;
    private Context context;
    private Requester requester;
    private int thisButtonInd;

    public RecommendButtonClickListener(Context context, Button button, int thisButtonInd){
        this.context = context;
        this.button = button;
        this.requester = Requester.getInstance(context);
        this.buttonOn = false;
        this.thisButtonInd = thisButtonInd;

        if (userSelection == null){
            userSelection = Collections.synchronizedList(new ArrayList<String>(10));
        }
    }

    @Override
    public void onClick(View view) {
        if (button.isShown()){
            userSelection.add(button.getText().toString());
            buttonOn = !buttonOn;
            if (buttonOn){
                button.setBackgroundResource(R.drawable.button_round_on);
                button.setTextAppearance(context, R.style.RoundButtonTextOn);
            } else {
                button.setBackgroundResource(R.drawable.button_round_off);
                button.setTextAppearance(context, R.style.RoundButtonTextOff);
            }
            requester.pollPostLearner(userSelection, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // TODO update all buttons
                }
            });
        }
    }

    public static List<String> getUserSelection(){
        return userSelection;
    }
}
