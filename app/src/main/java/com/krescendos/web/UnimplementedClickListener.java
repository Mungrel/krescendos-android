package com.krescendos.web;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class UnimplementedClickListener implements View.OnClickListener {

    private Context context;

    public UnimplementedClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Toast toast = Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
