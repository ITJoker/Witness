package com.risenb.witness.network.OkHttpUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.risenb.witness.network.OkHttpUtils.body.ProgressRequestBody;
import com.risenb.witness.network.OkHttpUtils.body.ResponseProgressBody;
import com.risenb.witness.network.OkHttpUtils.response.DownloadResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.IResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyOkHttp {

    private OkHttpClient client;
    private static MyOkHttp instance;
    private Handler mHandler;

    public MyOkHttp() {

        //日志拦截器
        HttpLoggingInterceptor mLogInterceptor = new HttpLoggingInterceptor();
        mLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接的超时时间
                .writeTimeout(50, TimeUnit.SECONDS)   //设置响应的超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 请求的超时时间
                .cookieJar(new CookiesManager())//允许使用Cookie
                .addInterceptor(mLogInterceptor)//日志拦截器
                //  .addInterceptor(new HeadInterceptor()) //统一设置 头部参数
                .retryOnConnectionFailure(true)
                //重连
                .build();
        mHandler = new Handler(Looper.myLooper());
    }


    private class CookiesManager implements CookieJar {
        HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url, cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }

    /**
     * MyOkHttp
     */
    public static MyOkHttp get() {
        if (instance == null) {
            synchronized (MyOkHttp.class) {
                instance = new MyOkHttp();
            }
        }
        return instance;
    }

    /**
     * post 请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void post(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        //发起request
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .tag(context)
                .build();

        client.newCall(request).enqueue(new MyCallback(mHandler, responseHandler));
    }

    /**
     * get请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //拼接url
        String get_url = url;
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        //发起request
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .build();

        client.newCall(request).enqueue(new MyCallback(mHandler, responseHandler));
    }


    /**
     * 上传文件
     *
     * @param context         发起请求的context
     * @param url             url
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, File> files, final IResponseHandler responseHandler) {
        upload(context, url, null, files, responseHandler);
    }

    /**
     * 上传文件
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, String> params, Map<String, File> files, final IResponseHandler responseHandler) {

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""), fileBody);
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(new ProgressRequestBody(multipartBuilder.build(), responseHandler))
                .tag(context)
                .build();

        client.newCall(request).enqueue(new MyCallback(mHandler, responseHandler));
    }

    /**
     * 下载文件
     * @param context                 发起请求的context
     * @param url                     下载地址
     * @param fileDir                 下载目的目录
     * @param filename                下载目的文件名
     * @param downloadResponseHandler 下载回调
     */
    public void download(Context context, String url, String fileDir, String filename, final DownloadResponseHandler downloadResponseHandler) {

        //发起request
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(url)
                .tag(context)
                .build();

        client.newBuilder()
                .addNetworkInterceptor(new Interceptor() {      //设置拦截器
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ResponseProgressBody(originalResponse.body(), downloadResponseHandler))
                                .build();
                    }
                })
                .build()
                .newCall(request)
                .enqueue(new MyDownloadCallback(mHandler, downloadResponseHandler, fileDir, filename));
    }

    /**
     * 取消当前context的所有请求
     */
    public void cancel(Context context) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }

    //下载回调
    private class MyDownloadCallback implements Callback {

        private Handler mHandler;
        private DownloadResponseHandler mDownloadResponseHandler;
        private String mFileDir;
        private String mFilename;

        public MyDownloadCallback(Handler handler, DownloadResponseHandler downloadResponseHandler, String fileDir, String filename) {
            mHandler = handler;
            mDownloadResponseHandler = downloadResponseHandler;
            mFileDir = fileDir;
            mFilename = filename;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            LogUtils.e("onFailure", e);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFailure(e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                response.body().contentLength();
                File file = null;
                try {
                    file = saveFile(response, mFileDir, mFilename);
                } catch (final IOException e) {
                    LogUtils.e("onResponse saveFile fail", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadResponseHandler.onFailure("onResponse saveFile fail." + e.toString());
                        }
                    });
                }

                final File newFile = file;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFinish(newFile);
                    }
                });
            } else {
                LogUtils.e("onResponse fail status=" + response.code());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFailure("fail status=" + response.code());
                    }
                });
            }
        }
    }

    private class MyCallback implements Callback {

        private Handler mHandler;
        private IResponseHandler mResponseHandler;

        public MyCallback(Handler handler, IResponseHandler responseHandler) {
            mHandler = handler;
            mResponseHandler = responseHandler;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            LogUtils.e("onFailure", e);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(0, e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                final String response_body = response.body().string();

                if (mResponseHandler instanceof JsonResponseHandler) {       //json回调
                    try {
                        final JSONObject jsonBody = new JSONObject(response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((JsonResponseHandler) mResponseHandler).onSuccess(response.code(), jsonBody);
                            }
                        });
                    } catch (JSONException e) {
                        LogUtils.e("onResponse fail parse jsonobject, body=" + response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResponseHandler.onFailure(response.code(), "fail parse jsonobject, body=" + response_body);
                            }
                        });
                    }
                } else if (mResponseHandler instanceof GsonResponseHandler) {    //gson回调
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LogUtils.e("response_body" + response_body);
                                Gson gson = new Gson();
                                ((GsonResponseHandler) mResponseHandler).onSuccess(response.code(),
                                        gson.fromJson(response_body, ((GsonResponseHandler) mResponseHandler).getType()));
                            } catch (Exception e) {
                                LogUtils.e("onResponse fail parse gson", e);
                                mResponseHandler.onFailure(response.code(), "fail parse gson, body=" + response_body);
                            }
                        }
                    });
                } else if (mResponseHandler instanceof RawResponseHandler) {     //raw字符串回调
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((RawResponseHandler) mResponseHandler).onSuccess(response.code(), response_body);
                        }
                    });
                }
            } else {
                LogUtils.e("onResponse fail status=" + response.code());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponseHandler.onFailure(0, "fail status=" + response.code());
                    }
                });
            }
        }
    }

    //保存文件
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public OkHttpClient getClient() {
        if (null == client) {
            MyOkHttp.get();
        }
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

}
