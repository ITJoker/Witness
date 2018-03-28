package com.risenb.witness.ui.web;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.FragmentActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 跳转界面拦截
 */
public class HelloWebViewClient extends WebViewClient {
    private FragmentActivity activity;

    public HelloWebViewClient(FragmentActivity activity) {
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 自适应屏幕
        // wv_paypal.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        if (url.indexOf("tel:") == 0) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
            getActivity().startActivity(intent);
        } else if (url.indexOf("sms:") == 0) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getActivity().startActivity(sendIntent);
        } else {
            Intent intent = new Intent(getActivity(), WebUI.class);
            intent.putExtra("url", url);
            getActivity().startActivity(intent);
        }
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();// 这里，继续执行ssl
    }
}
