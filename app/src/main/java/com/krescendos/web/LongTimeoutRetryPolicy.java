package com.krescendos.web;

import com.android.volley.DefaultRetryPolicy;

class LongTimeoutRetryPolicy extends DefaultRetryPolicy {

    private static final int TIMEOUT_MS = 3000;
    private static final int RETRY_ATTEMPTS = 3;

    LongTimeoutRetryPolicy() {
        super(TIMEOUT_MS, RETRY_ATTEMPTS, DEFAULT_BACKOFF_MULT);
    }
}
