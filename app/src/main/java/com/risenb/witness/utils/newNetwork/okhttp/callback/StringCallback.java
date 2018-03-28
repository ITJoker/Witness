package com.risenb.witness.utils.newNetwork.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

public abstract class StringCallback extends Callback<String> {
    public StringCallback() {
    }

    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }
}
