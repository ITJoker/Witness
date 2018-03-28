package com.risenb.witness.ui.vip;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@ContentView(R.layout.vipchangephone)
public class VipChangePhoneUI extends BaseUI {
    @ViewInject(R.id.vipchangephone_phone_tv)
    private TextView vipchangephone_phone_tv;
    @ViewInject(R.id.vipchangephone_phone_et)
    private EditText vipchangephone_phone_et;
    @ViewInject(R.id.vipchangephone_code_tv)
    private TextView vipchangephone_code_tv;
    @ViewInject(R.id.vipchangephone_code_et)
    private EditText vipchangephone_code_et;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("更改手机号");
        vipchangephone_phone_tv.setText(application.getTel());
    }

    @Override
    protected void prepareData() {
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.vipchangephone_save_tv)
    private void vipChangePhone_save_tv(View v) {
        if (TextUtils.isEmpty(vipchangephone_phone_et.getText().toString().trim())) {
            makeText("请输入新手机号");
            return;
        }
        if (TextUtils.isEmpty(vipchangephone_code_et.getText().toString().trim())) {
            makeText("请输入验证码");
            return;
        }
        editTel();
    }

    private void editTel() {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.editTel));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("newTel", vipchangephone_phone_et.getText().toString().trim());
        param.put("code", vipchangephone_code_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.json.JSONObject response) {
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
            }
        });
    }

    //验证码
    @OnClick(R.id.vipchangephone_code_tv)
    private void codeOnclick(View v) {
        if (TextUtils.isEmpty(vipchangephone_phone_et.getText().toString().trim())) {
            makeText("手机号码不能为空！");
            return;
        }
        if (!application.isMobileNO(vipchangephone_phone_et.getText().toString().trim())) {
            makeText("手机号码不规范！");
            return;
        }
        sendCode();
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.getCode));
        Map<String, String> param = new HashMap<>();
        param.put("tel", vipchangephone_phone_et.getText().toString().trim());
        param.put("type", "3");
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.json.JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void sendCode() {
        MyCount time = new MyCount(60000, 1000);
        time.start();
        Utils.getUtils().dismissDialog();
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            vipchangephone_code_tv.setText("获取验证码");
            vipchangephone_code_tv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            vipchangephone_code_tv.setText(millisUntilFinished / 1000 + "秒后重发");
            vipchangephone_code_tv.setClickable(false);

        }
    }

    class SearchWeather implements TextWatcher {
        // 监听改变的文本框
        private EditText editText;

        public SearchWeather(EditText username) {
            this.editText = username;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            String editable = editText.getText().toString();
            String str = stringFilter(editable.toString());
            if (!editable.equals(str)) {
                editText.setText(str);
                // 设置新的光标所在位置
                editText.setSelection(str.length());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        public String stringFilter(String str) throws PatternSyntaxException {
            // 只允许字母和数字
            String regEx = "[^[a-zA-Z][a-zA-Z0-9_]{2,16}$]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        }
    }
}
