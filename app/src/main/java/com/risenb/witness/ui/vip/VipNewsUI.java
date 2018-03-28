package com.risenb.witness.ui.vip;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;

@ContentView(R.layout.vipnews)
public class VipNewsUI extends BaseUI {
    @ViewInject(R.id.taskinfotrain_wv_new)
    private WebView taskinfotrain_wv_new;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("新手攻略");
    }

    @Override
    protected void prepareData() {
        String url = "http://u3335178.viewer.maka.im/k/M47QXU6K";
        // 指定的垂直滚动条有叠加样式
        taskinfotrain_wv_new.setVerticalScrollbarOverlay(true);
        WebSettings webSettings = taskinfotrain_wv_new.getSettings();

        /*
         * 设置自适应屏幕，两者合用
         * 之前不能全屏是由于WebView自适应嵌套在ScrollView内产生矛盾
         */
        //将图片调整到适合WebView的大小
        webSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        /*
         * 缩放操作
         */
        //支持缩放，默认为true,是下面的前提
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件，若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        /*
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
         */
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        /*webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(MyApplication.getInstance().getPath());
        webSettings.setAppCacheMaxSize(1024 * 1024 * 5);*/

        taskinfotrain_wv_new.setOnCreateContextMenuListener(this);
        taskinfotrain_wv_new.loadUrl(url);
        taskinfotrain_wv_new.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        WebSettings settings = taskinfotrain_wv_new.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        WebSettings settings = taskinfotrain_wv_new.getSettings();
        settings.setJavaScriptEnabled(false);
    }

    /**
     * 页面缓存：加载一个网页时的html、JS、CSS等页面或者资源数据，这些缓存资源是由浏览器行为而产生，开发者只能通过配置HTTP响应头影响浏览器的行为才能间接地影响到这些缓存数据
     * 而缓存的索引放在：/data/data/<包名>/databases
     * 对应的文件放在：/data/data/package_name/cache/webviewCacheChromunm下
     * 数据缓存：分为AppCache和DOM Storage两种
     * 开发者可以自行控制的缓存资源
     * AppCache：我们能够有选择的缓冲web浏览器中所有的东西，从页面、图片到脚本、css等等，尤其在涉及到应用于网站的多个页面上的CSS和JavaScript文件的时候非常有用，其大小目前通常是5M
     * 在Android上需要手动开启（setAppCacheEnabled），并设置路径（setAppCachePath）和容量（setAppCacheMaxSize），而Android中使用ApplicationCache.db来保存AppCache数据
     * DOM Storage：存储一些简单的用key/value对即可解决的数据，根据作用范围的不同，有SessionStorage和Local Storage两种，分别用于会话级别的存储（页面关闭即消失）和本地化存储（除非主动
     * 删除，否则数据永远不会过期）在Android中可以手动开启DOM Storage（setDomStorageEnabled），
     * 设置存储路径（setDatabasePath）Android中Webkit会为DOMStorage产生两个文件（my_path/localstorage/http_blog.csdn.net_0.localstorage和my_path/Databases.db）
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对WebView而是针对整个应用程序.
        taskinfotrain_wv_new.clearCache(true);
        //清除当前webView访问历史记录里的所有历史记录除了当前访问记录
        taskinfotrain_wv_new.clearHistory();
        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        taskinfotrain_wv_new.clearFormData();
    }
}
