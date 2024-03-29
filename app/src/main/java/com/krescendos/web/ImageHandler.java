package com.krescendos.web;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ImageHandler {
    private static ImageHandler instance;
    private ImageLoader imageLoader;

    private ImageHandler(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new ImageCache());
    }

    public static ImageHandler getInstance(Context context) {
        if (instance == null) {
            instance = new ImageHandler(context);
        }

        return instance;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
