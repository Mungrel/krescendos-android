package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.krescendos.model.PartyState;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.DefaultResponseListener;
import com.krescendos.web.LongTimeoutRetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UpdatePlayStateRequest extends JsonRequest<JSONObject>{
    public UpdatePlayStateRequest(String url, PartyState state) {
        super(Method.POST, url, new Gson().toJson(state), new DefaultResponseListener(), new DefaultErrorListener());
        setRetryPolicy(new LongTimeoutRetryPolicy());
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject object = null;
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            object = new JSONObject(jsonString);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

        return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
    }
}
