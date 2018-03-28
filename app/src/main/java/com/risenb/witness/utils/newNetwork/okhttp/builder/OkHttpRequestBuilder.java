package com.risenb.witness.utils.newNetwork.okhttp.builder;

import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected int id;

    public OkHttpRequestBuilder() {
    }

    public OkHttpRequestBuilder id(int id) {
        this.id = id;
        return this;
    }

    public OkHttpRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public OkHttpRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public OkHttpRequestBuilder addHeader(String key, String val) {
        if(this.headers == null) {
            this.headers = new LinkedHashMap();
        }

        this.headers.put(key, val);
        return this;
    }

    public abstract RequestCall build();
}
