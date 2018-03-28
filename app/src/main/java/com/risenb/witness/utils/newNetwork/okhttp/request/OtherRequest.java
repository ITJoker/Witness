package com.risenb.witness.utils.newNetwork.okhttp.request;

import android.text.TextUtils;

import com.risenb.witness.utils.newUtils.Exceptions;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

public class OtherRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequest(RequestBody requestBody, String content, String method, String url, Object tag, Map<String, String> params, Map<String, String> headers, int id) {
        super(url, tag, params, headers, id);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;
    }

    protected RequestBody buildRequestBody() {
        if(this.requestBody == null && TextUtils.isEmpty(this.content) && HttpMethod.requiresRequestBody(this.method)) {
            Exceptions.illegalArgument("requestBody and content can not be null in method:" + this.method, new Object[0]);
        }

        if(this.requestBody == null && !TextUtils.isEmpty(this.content)) {
            this.requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, this.content);
        }

        return this.requestBody;
    }

    protected Request buildRequest(RequestBody requestBody) {
        if(this.method.equals("PUT")) {
            this.builder.put(requestBody);
        } else if(this.method.equals("DELETE")) {
            if(requestBody == null) {
                this.builder.delete();
            } else {
                this.builder.delete(requestBody);
            }
        } else if(this.method.equals("HEAD")) {
            this.builder.head();
        } else if(this.method.equals("PATCH")) {
            this.builder.patch(requestBody);
        }

        return this.builder.build();
    }
}
