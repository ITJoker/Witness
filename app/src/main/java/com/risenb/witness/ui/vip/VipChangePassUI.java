package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.risenb.witness.ui.login.RegisterUI;
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
import com.risenb.witness.utils.PwdCheckUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.vipchangepass)
public class VipChangePassUI extends BaseUI {
    @ViewInject(R.id.vipchangepass_jiu_et)
    private EditText vipchangepass_jiu_et;
    @ViewInject(R.id.vipchangepass_xin_et)
    private EditText vipchangepass_xin_et;
    @ViewInject(R.id.vipchangepass_qrxin_et)
    private EditText vipchangepass_qrxin_et;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("修改密码");
        /*rightVisible("忘记密码");*/
        rightVisible("");
    }

    @Override
    protected void prepareData() {
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 保存
     */
    @OnClick(R.id.vipchangepass_ok_tv)
    private void vipChangePass_ok_tv(View v) {
        if (TextUtils.isEmpty(application.getC())) {
            makeText("请登录");
            return;
        }
        if (TextUtils.isEmpty(vipchangepass_jiu_et.getText().toString().trim())) {
            makeText("请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(vipchangepass_xin_et.getText().toString().trim())) {
            makeText("请输入新密码");
            return;
        }
        if (vipchangepass_xin_et.getText().toString().trim().length() < 6 || vipchangepass_xin_et.getText().toString().trim().length() > 16) {
            makeText("密码为包含字母和数字的6-16个字符");
            return;
        }
        if (!PwdCheckUtil.isLetterDigit(vipchangepass_xin_et.getText().toString().trim())) {
            makeText("密码为包含字母和数字的6-16个字符");
            return;
        }
        if (TextUtils.isEmpty(vipchangepass_qrxin_et.getText().toString().trim())) {
            makeText("请确认新密码");
            return;
        }
        if (!vipchangepass_qrxin_et.getText().toString().trim().equals(vipchangepass_xin_et.getText().toString().trim())) {
            makeText("两次密码不一致");
            return;
        }

        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.editPassword));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("oldPwd", vipchangepass_jiu_et.getText().toString().trim());
        param.put("newPwd", vipchangepass_xin_et.getText().toString().trim());
        param.put("ConfirmPwd", vipchangepass_qrxin_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
                if (wBaseBean.getSuccess().equals("1")) {
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

    /**
     * 找回密码
     */
    @OnClick(R.id.tv_right)
    private void tv_right(View v) {
        startActivity(new Intent(this, RegisterUI.class).putExtra("type", "2"));
        finish();
    }
}
