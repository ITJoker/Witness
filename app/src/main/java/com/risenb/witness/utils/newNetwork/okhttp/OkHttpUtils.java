package com.risenb.witness.utils.newNetwork.okhttp;

import com.risenb.witness.utils.newNetwork.okhttp.builder.GetBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.HeadBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.OtherRequestBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.PostFileBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.PostFormBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.builder.PostStringBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.callback.Callback;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;
import com.risenb.witness.utils.newUtils.Platform;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10000L;
    private static volatile OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            this.mOkHttpClient = new OkHttpClient();
        } else {
            this.mOkHttpClient = okHttpClient;
        }

        this.mPlatform = Platform.get();
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            Class var1 = OkHttpUtils.class;
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }

        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient((OkHttpClient) null);
    }

    public Executor getDelivery() {
        return this.mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return this.mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder("PUT");
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder("DELETE");
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder("PATCH");
    }

    public void execute(RequestCall requestCall, Callback callback) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }

        final int id = requestCall.getOkHttpRequest().getId();
        final Callback finalCallback = callback;
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.this.sendFailResultCallback(call, e, finalCallback, id);
            }

            public void onResponse(Call call, Response response) {
                try {
                    if (!call.isCanceled()) {
                        if (!finalCallback.validateReponse(response, id)) {
                            OkHttpUtils.this.sendFailResultCallback(call, new IOException("request failed , reponse\'s code is : " + response.code()), finalCallback, id);
                            return;
                        }

                        Object e = finalCallback.parseNetworkResponse(response, id);
                        OkHttpUtils.this.sendSuccessResultCallback(e, finalCallback, id);
                        return;
                    }

                    OkHttpUtils.this.sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                } catch (Exception var7) {
                    OkHttpUtils.this.sendFailResultCallback(call, var7, finalCallback, id);
                    return;
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }

                }

            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback != null) {
            this.mPlatform.execute(new Runnable() {
                public void run() {
                    callback.onError(call, e, id);
                    callback.onAfter(id);
                }
            });
        }
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback != null) {
            this.mPlatform.execute(new Runnable() {
                public void run() {
                    callback.onResponse(object, id);
                    callback.onAfter(id);
                }
            });
        }
    }

    public void cancelTag(Object tag) {
        Iterator var2 = this.mOkHttpClient.dispatcher().queuedCalls().iterator();

        Call call;
        while (var2.hasNext()) {
            call = (Call) var2.next();
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        var2 = this.mOkHttpClient.dispatcher().runningCalls().iterator();

        while (var2.hasNext()) {
            call = (Call) var2.next();
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";

        public METHOD() {
        }
    }
}
