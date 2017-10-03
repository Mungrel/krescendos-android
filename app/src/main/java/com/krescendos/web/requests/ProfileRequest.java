package com.krescendos.web.requests;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.krescendos.model.Profile;
import com.krescendos.web.DefaultErrorListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends JsonRequest<Profile> {

    private static final String url = "https://api.spotify.com/v1/me";

    private String accessToken;

    public ProfileRequest(Context context, String accessToken, Response.Listener<Profile> listener) {
        super(Method.GET, url, null, listener, new DefaultErrorListener());
        this.accessToken = accessToken;
    }

    @Override
    protected Response<Profile> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(new Gson().fromJson(jsonString, Profile.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

}
