package com.risenb.witness.utils.newNetwork.okhttp.builder;

import java.util.Map;

public interface HasParamsable {
    OkHttpRequestBuilder params(Map<String, String> var1);

    OkHttpRequestBuilder addParams(String var1, String var2);
}
