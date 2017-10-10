package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.LongTimeoutRetryPolicy;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

public class PollPostLearnerRequest extends JsonRequest<List<String>> {
    public PollPostLearnerRequest(String url, List<String> userSelection, Response.Listener<List<String>> listener) {
        super(Method.POST, url, new Gson().toJson(userSelection), listener, new DefaultErrorListener());
        setRetryPolicy(new LongTimeoutRetryPolicy());
    }

    @Override
    protected Response<List<String>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            Type t = new TypeToken<List<String>>() {
            }.getType();
            List<String> tags = new Gson().fromJson(jsonString, t);
            return Response.success(tags, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
