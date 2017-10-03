package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.model.SpotifySeedCollection;
import com.krescendos.model.Track;
import com.krescendos.web.DefaultErrorListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

public class RecommendRequest extends JsonRequest<List<Track>> {
    public RecommendRequest(String url, SpotifySeedCollection collection, Response.Listener<List<Track>> listener) {
        super(Method.POST, url, new Gson().toJson(collection), listener, new DefaultErrorListener());
    }

    @Override
    protected Response<List<Track>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            Type t = new TypeToken<List<Track>>() {
            }.getType();
            List<Track> tracks = new Gson().fromJson(jsonString, t);
            return Response.success(tracks, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
