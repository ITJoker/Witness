package com.risenb.witness.network.OkHttpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {

    //这里可以把需要设置的 head 传进来
    public HeadInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request mRquest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Cookie", "add cookies here")
                .build();

        return chain.proceed(mRquest);
    }
}
