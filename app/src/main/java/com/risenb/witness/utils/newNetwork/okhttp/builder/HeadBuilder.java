package com.risenb.witness.utils.newNetwork.okhttp.builder;

import com.risenb.witness.utils.newNetwork.okhttp.request.OtherRequest;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;

import okhttp3.RequestBody;

public class HeadBuilder extends GetBuilder {
    public HeadBuilder() {

    }

    public RequestCall build() {
        return (new OtherRequest((RequestBody) null, (String) null, "HEAD", this.url, this.tag, this.params, this.headers, this.id)).build();
    }
}
