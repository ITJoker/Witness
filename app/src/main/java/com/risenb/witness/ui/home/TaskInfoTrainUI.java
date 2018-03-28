package com.risenb.witness.ui.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.taskinfotrain)
public class TaskInfoTrainUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.taskinfotrain_wv)
    private WebView taskinfotrain_wv;

    private String trainid;
    private String fixedID;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("任务指南");
        trainid = getIntent().getStringExtra("id");
        fixedID = getIntent().getStringExtra("fixedID");
    }

    @Override
    protected void prepareData() {
        String url = "";
        if (!TextUtils.isEmpty(trainid)) {
            // 一般任务
            url = "http://www.adexmall.net/Task/taskTrain?taskId=" + trainid;
        } else if (!TextUtils.isEmpty(fixedID)) {
            // 固定任务
            url = "http://www.adexmall.net/Task/FixedtaskTrain?fixedId=" + fixedID;
        }
        // 指定的垂直滚动条有叠加样式
        taskinfotrain_wv.setVerticalScrollbarOverlay(true);
        WebSettings settings = taskinfotrain_wv.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        taskinfotrain_wv.getSettings().setDomStorageEnabled(true);
        taskinfotrain_wv.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = application.getPath();
        taskinfotrain_wv.getSettings().setAppCachePath(appCachePath);
        taskinfotrain_wv.getSettings().setAllowFileAccess(true);
        taskinfotrain_wv.getSettings().setAppCacheEnabled(true);
        taskinfotrain_wv.setOnCreateContextMenuListener(this);
        taskinfotrain_wv.loadUrl(url);
        taskinfotrain_wv.setWebViewClient(new WebViewClient() {
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

    /**
     * 培训完成
     */
    @OnClick(R.id.taskinfotrain_ok_tv)
    private void taskInfoTrainOK_tv(View v) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.istrain));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        if (!TextUtils.isEmpty(trainid)) {
            // 一般任务
            param.put("trainid", trainid);
        } else if (!TextUtils.isEmpty(fixedID)) {
            // 固定任务
            finish();
            return;
        }
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    Intent intent = getIntent();
                    setResult(1, intent);
                    finish();
                } else {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

}
