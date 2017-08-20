package com.krescendos.web;

import com.android.volley.DefaultRetryPolicy;

public class LongTimeoutRetryPolicy extends DefaultRetryPolicy {

    public LongTimeoutRetryPolicy(int timeoutMs) {
        super(timeoutMs, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }
}
