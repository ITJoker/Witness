package com.risenb.witness.utils.newNetwork.okhttp.builder;

import android.net.Uri;

import com.risenb.witness.utils.newNetwork.okhttp.request.GetRequest;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {
    public GetBuilder() {

    }

    public RequestCall build() {
        if(this.params != null) {
            this.url = this.appendParams(this.url, this.params);
        }

        return (new GetRequest(this.url, this.tag, this.params, this.headers, this.id)).build();
    }

    protected String appendParams(String url, Map<String, String> params) {
        if(url != null && params != null && !params.isEmpty()) {
            Uri.Builder builder = Uri.parse(url).buildUpon();
            Set keys = params.keySet();
            Iterator iterator = keys.iterator();

            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                builder.appendQueryParameter(key, (String)params.get(key));
            }

            return builder.build().toString();
        } else {
            return url;
        }
    }

    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public GetBuilder addParams(String key, String val) {
        if(this.params == null) {
            this.params = new LinkedHashMap();
        }

        this.params.put(key, val);
        return this;
    }
}
