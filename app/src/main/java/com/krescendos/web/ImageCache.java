package com.krescendos.web;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class ImageCache implements ImageLoader.ImageCache {

    private static final int CACHE_SIZE = 10;

    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(CACHE_SIZE);

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
