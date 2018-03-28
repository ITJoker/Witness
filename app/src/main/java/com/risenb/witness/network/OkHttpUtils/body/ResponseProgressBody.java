package com.risenb.witness.network.OkHttpUtils.body;

import com.risenb.witness.network.OkHttpUtils.response.DownloadResponseHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 重写responseBody 设置下载进度监听
 */
public class ResponseProgressBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private DownloadResponseHandler mDownloadResponseHandler;
    private BufferedSource bufferedSource;

    public ResponseProgressBody(ResponseBody responseBody, DownloadResponseHandler downloadResponseHandler) {
        this.mResponseBody = responseBody;
        this.mDownloadResponseHandler = downloadResponseHandler;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (mDownloadResponseHandler != null) {
                    mDownloadResponseHandler.onProgress(totalBytesRead, contentLength());
                }
                return bytesRead;
            }
        };
    }

}
