package com.krescendos.buttons;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendButtonClickListener implements View.OnClickListener {

    private static List<String> results;
    private Button button;

    public RecommendButtonClickListener(Button button){
        this.button = button;
        if (results == null){
            results = Collections.synchronizedList(new ArrayList<String>(10));
        }
    }

    @Override
    public void onClick(View view) {
        if (button.isShown()){
            results.add(button.getText().toString());
        }
    }

    public static List<String> getResults(){
        return results;
    }
}
