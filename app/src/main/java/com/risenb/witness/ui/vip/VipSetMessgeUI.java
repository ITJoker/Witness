package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.vipsetmessge)
public class VipSetMessgeUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.vipsetmessge_mdr_cb)
    private CheckBox vipsetmessge_mdr_cb;
    @ViewInject(R.id.vipsetmessge_rwdq_cb)
    private CheckBox vipsetmessge_rwdq_cb;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("消息推送");
    }

    @Override
    protected void prepareData() {
        if (application.getNightNotDisturb().equals("false")) {
            vipsetmessge_mdr_cb.setChecked(false);
        } else {
            vipsetmessge_mdr_cb.setChecked(true);
        }
        if (application.getTaskExpireOnOff().equals("false")) {
            vipsetmessge_rwdq_cb.setChecked(false);
        } else {
            vipsetmessge_rwdq_cb.setChecked(true);
        }
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 夜间免打扰
     */
    @OnClick(R.id.vipsetmessge_mdr_cb)
    private void vipsetmessge_mdr_cb(View v) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.nightNotDisturb));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        if (vipsetmessge_mdr_cb.isChecked()) {
            param.put("state", "1");
            application.setNightNotDisturb("true");
        } else {
            param.put("state", "0");
            application.setNightNotDisturb("false");
        }
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
                if (wBaseBean.getErrorMsg().equals("登录异常")) {
                    errorLogin();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText(error_msg);
            }
        });

    }

    /**
     * 任务到期
     */
    @OnClick(R.id.vipsetmessge_rwdq_cb)
    private void vipsetmessge_rwdq_cb(View v) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskExpireOnOff));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        if (vipsetmessge_rwdq_cb.isChecked()) {
            param.put("state", "1");
            application.setTaskExpireOnOff("true");
        } else {
            param.put("state", "0");
            application.setTaskExpireOnOff("false");
        }
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
                if (wBaseBean.getErrorMsg().equals("登录异常")) {
                    errorLogin();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText(error_msg);
            }
        });
    }
}
