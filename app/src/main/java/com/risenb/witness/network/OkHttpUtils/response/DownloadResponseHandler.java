package com.risenb.witness.network.OkHttpUtils.response;

import java.io.File;

/**
 * 下载回调
 */
public abstract class DownloadResponseHandler {
    public abstract void onFinish(File download_file);
    public abstract void onProgress(long currentBytes, long totalBytes);
    public abstract void onFailure(String error_msg);
}
