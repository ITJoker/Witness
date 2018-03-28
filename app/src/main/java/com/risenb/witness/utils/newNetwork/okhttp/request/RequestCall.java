package com.risenb.witness.utils.newNetwork.okhttp.request;

import com.risenb.witness.utils.newNetwork.okhttp.OkHttpUtils;
import com.risenb.witness.utils.newNetwork.okhttp.callback.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;
    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;
    private OkHttpClient clone;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Call buildCall(Callback callback) {
        this.request = this.generateRequest(callback);
        if(this.readTimeOut <= 0L && this.writeTimeOut <= 0L && this.connTimeOut <= 0L) {
            this.call = OkHttpUtils.getInstance().getOkHttpClient().newCall(this.request);
        } else {
            this.readTimeOut = this.readTimeOut > 0L?this.readTimeOut:10000L;
            this.writeTimeOut = this.writeTimeOut > 0L?this.writeTimeOut:10000L;
            this.connTimeOut = this.connTimeOut > 0L?this.connTimeOut:10000L;
            this.clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder().readTimeout(this.readTimeOut, TimeUnit.MILLISECONDS).writeTimeout(this.writeTimeOut, TimeUnit.MILLISECONDS).connectTimeout(this.connTimeOut, TimeUnit.MILLISECONDS).build();
            this.call = this.clone.newCall(this.request);
        }

        return this.call;
    }

    private Request generateRequest(Callback callback) {
        return this.okHttpRequest.generateRequest(callback);
    }

    public void execute(Callback callback) {
        this.buildCall(callback);
        if(callback != null) {
            callback.onBefore(this.request, this.getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, callback);
    }

    public Call getCall() {
        return this.call;
    }

    public Request getRequest() {
        return this.request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return this.okHttpRequest;
    }

    public Response execute() throws IOException {
        this.buildCall((Callback)null);
        return this.call.execute();
    }

    public void cancel() {
        if(this.call != null) {
            this.call.cancel();
        }

    }
}
