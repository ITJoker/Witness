package com.risenb.witness.network.OkHttpUtils.response;

public interface IResponseHandler {
    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
