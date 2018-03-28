package com.risenb.witness.utils.newNetwork.okhttp.builder;

import com.risenb.witness.utils.newNetwork.okhttp.request.PostStringRequest;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;

import okhttp3.MediaType;

public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;
    private MediaType mediaType;

    public PostStringBuilder() {
    }

    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public RequestCall build() {
        return (new PostStringRequest(this.url, this.tag, this.params, this.headers, this.content, this.mediaType, this.id)).build();
    }
}
