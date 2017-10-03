package com.krescendos.web.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.krescendos.web.DefaultErrorListener;
import com.krescendos.web.DefaultResponseListener;
import com.krescendos.web.LongTimeoutRetryPolicy;

import org.json.JSONObject;

public class AppendRequest extends JsonRequest<JSONObject>{
    public AppendRequest(String url) {
        super(Method.POST, url, null, new DefaultResponseListener(), new DefaultErrorListener());
        setRetryPolicy(new LongTimeoutRetryPolicy());
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return null;
    }
}
