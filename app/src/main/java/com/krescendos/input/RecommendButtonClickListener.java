package com.krescendos.input;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.krescendos.R;
import com.krescendos.web.Requester;
import com.krescendos.web.async.AsyncResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendButtonClickListener implements View.OnClickListener {

    private static List<String> userSelection;
    private Button thisButton;
    private Button[] buttons;
    private boolean buttonOn;
    private Context context;
    private Requester requester;

    public RecommendButtonClickListener(Context context, Button[] buttons, int thisButtonInd) {
        this.context = context;
        this.buttons = buttons;
        this.requester = Requester.getInstance();
        this.buttonOn = false;
        this.thisButton = buttons[thisButtonInd];

        if (userSelection == null) {
            userSelection = Collections.synchronizedList(new ArrayList<String>(10));
        }
    }

    public static List<String> getUserSelection() {
        return userSelection;
    }

    @Override
    public void onClick(View view) {
        if (thisButton.isShown()) {
            userSelection.add(thisButton.getText().toString());
            buttonOn = !buttonOn;
            if (buttonOn) {
                thisButton.setBackgroundResource(R.drawable.button_round_on);
                thisButton.setTextAppearance(context, R.style.RoundButtonTextOn);
            } else {
                thisButton.setBackgroundResource(R.drawable.button_round_off);
                thisButton.setTextAppearance(context, R.style.RoundButtonTextOff);
            }
            requester.pollPostLearner(userSelection, new AsyncResponseListener<List<String>>() {
                @Override
                public void onResponse(List<String> response) {
                    // TODO update all buttons
                }
            });
        }
    }
}
