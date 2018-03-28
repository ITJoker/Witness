package com.risenb.witness.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.risenb.witness.ui.PresenterBase;

public class WebP extends PresenterBase implements OnCreateContextMenuListener {
    private WebFace face;
    private String imgurl = "";
    public ValueCallback<Uri> mUploadMessage;
    public final static int FILECHOOSER_RESULTCODE = 1;

    public WebP(WebFace face, FragmentActivity activity) {
        this.face = face;
        setActivity(activity);
    }

    public void bind(WebView wv_web)// 将数据与view结合起来
    {
        String url = getActivity().getIntent().getStringExtra("url");
        String data = getActivity().getIntent().getStringExtra("data");
        // String url = "http://xinyidai.com01.org/isellcar.aspx";
        // String url = "http://114.112.173.54:8081/00.html";
        wv_web.setVerticalScrollbarOverlay(true); // 指定的垂直滚动条有叠加样式
        WebSettings settings = wv_web.getSettings();
        settings.setJavaScriptEnabled(true);// 设置WebView属性，能够执行Javascript脚本
        // settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // settings.setUseWideViewPort(true);// 设定支持viewport
        // settings.setLoadWithOverviewMode(true);
        // settings.setBuiltInZoomControls(true);
        // settings.setSupportZoom(true);// 设定支持缩放
        // 自适应屏幕
        // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // 加载需要显示的网页
        // <a onClick="window.android.startFunction('要传的参数')"
        // >点击调用java代码并传递参数</a>
        // wv_paypal.addJavascriptInterface(this, "android");

        // 支持 localstorage
        wv_web.getSettings().setDomStorageEnabled(true);
        wv_web.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = application.getPath();
        wv_web.getSettings().setAppCachePath(appCachePath);
        wv_web.getSettings().setAllowFileAccess(true);
        wv_web.getSettings().setAppCacheEnabled(true);

        if (url != null && !"".equals(url)) {
            wv_web.loadUrl(url);
        } else {
            wv_web.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
        }
        // 设置Web视图
        wv_web.setWebViewClient(new HelloWebViewClient(getActivity()));
        wv_web.setWebChromeClient(new MyWebChromeClient(getActivity(), mUploadMessage));
        wv_web.setOnCreateContextMenuListener(this);
    }

    /***
     * 功能：长按图片保存到手机
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "保存到手机") {
                    new SaveImage(getActivity(), imgurl).execute(); // Android 4.0以后要使用线程来访问网络
                } else {
                    return false;
                }
                return true;
            }
        };
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    imgurl = result.getExtra();
                    menu.setHeaderTitle("提示");
                    menu.add(0, v.getId(), 0, "保存到手机").setOnMenuItemClickListener(handler);
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    public interface WebFace {

    }

}
