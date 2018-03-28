package com.risenb.witness.utils.newNetwork.okhttp.request;

import com.risenb.witness.utils.newNetwork.okhttp.OkHttpUtils;
import com.risenb.witness.utils.newNetwork.okhttp.builder.PostFormBuilder;
import com.risenb.witness.utils.newNetwork.okhttp.callback.Callback;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostFormRequest extends OkHttpRequest {
    private List<PostFormBuilder.FileInput> files;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files, int id) {
        super(url, tag, params, headers, id);
        this.files = files;
    }

    protected RequestBody buildRequestBody() {
        if (this.files != null && !this.files.isEmpty()) {
            MultipartBody.Builder var5 = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
            this.addParams(var5);

            for (int var6 = 0; var6 < this.files.size(); ++var6) {
                PostFormBuilder.FileInput fileInput = this.files.get(var6);
                RequestBody fileBody = RequestBody.create(MediaType.parse(this.guessMimeType(fileInput.filename)), fileInput.file);
                var5.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }

            return var5.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            this.addParams(builder);
            FormBody i = builder.build();
            return i;
        }
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) {
            return requestBody;
        } else {
            CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
                public void onRequestProgress(final long bytesWritten, final long contentLength) {
                    OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                        public void run() {
                            callback.inProgress((float) bytesWritten * 1.0F / (float) contentLength, contentLength, PostFormRequest.this.id);
                        }
                    });
                }
            });
            return countingRequestBody;
        }
    }

    protected Request buildRequest(RequestBody requestBody) {
        return this.builder.post(requestBody).build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;

        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }

        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder) {
        if (this.params != null && !this.params.isEmpty()) {
            Iterator var2 = this.params.keySet().iterator();

            while (var2.hasNext()) {
                String key = (String) var2.next();
                builder.addPart(Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + key + "\""}), RequestBody.create((MediaType) null, (String) this.params.get(key)));
            }
        }

    }

    private void addParams(FormBody.Builder builder) {
        if (this.params != null) {
            Iterator var2 = this.params.keySet().iterator();

            while (var2.hasNext()) {
                String key = (String) var2.next();
                builder.add(key, (String) this.params.get(key));
            }
        }

    }
}
