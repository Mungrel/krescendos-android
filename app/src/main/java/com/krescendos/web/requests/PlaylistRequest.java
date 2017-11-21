package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.krescendos.model.Party;
import com.krescendos.model.Playlist;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.LongTimeoutRetryPolicy;

import java.io.UnsupportedEncodingException;

public class PlaylistRequest extends JsonRequest<Playlist> {
    public PlaylistRequest(String url, Response.Listener<Playlist> listener) {
        super(Method.PUT, url, null, listener, new DefaultErrorListener());
        setRetryPolicy(new LongTimeoutRetryPolicy());
    }

    @Override
    protected Response<Playlist> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            Playlist playlist = new Gson().fromJson(jsonString, Playlist.class);
            return Response.success(playlist, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
