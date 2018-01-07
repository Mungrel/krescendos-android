package com.krescendos.web.async;

public interface AsyncResponseListener<T> {
    void onResponse(T response);
}
