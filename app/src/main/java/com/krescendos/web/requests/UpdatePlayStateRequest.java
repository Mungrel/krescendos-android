package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.krescendos.model.PartyState;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.DefaultResponseListener;
import com.krescendos.web.LongTimeoutRetryPolicy;

import org.json.JSONObject;

public class UpdatePlayStateRequest extends JsonRequest<JSONObject>{
    public UpdatePlayStateRequest(String url, PartyState state, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, new Gson().toJson(state), new DefaultResponseListener(), new DefaultErrorListener());
        setRetryPolicy(new LongTimeoutRetryPolicy());
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return null;
    }
}
