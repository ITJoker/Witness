package com.risenb.witness.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.LoginBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@ContentView(R.layout.login)
public class LoginUI extends BaseUI {
    @ViewInject(R.id.login_username_et)
    private EditText login_username_et;
    @ViewInject(R.id.login_password_et)
    private EditText login_password_et;

    @ViewInject(R.id.login_login_tv)
    private TextView login_login_tv;
    @ViewInject(R.id.login_register_tv)
    private TextView login_register_tv;
    @ViewInject(R.id.login_retrieve_tv)
    private TextView login_retrieve_tv;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("");
        setTitleBackground(255, 255, 255);
        backGone();
    }

    @Override
    protected void prepareData() {
        login_username_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入文本之前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入文字中的状态，count是一次性输入字符数
                if (s.length() == 11) {
                    /*// 关闭键盘
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(login_username_et.getWindowToken(), 0);*/
                    login_username_et.clearFocus();
                    login_password_et.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入文字后的状态
            }
        });

        login_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入文本之前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入文字中的状态，count是一次性输入字符数
                if (s.length() >= 6) {
                    login_login_tv.setBackgroundResource(R.drawable.circular_green_login_bg);
                    login_register_tv.setTextColor(Color.rgb(119, 119, 119));
                    login_retrieve_tv.setTextColor(Color.rgb(55, 155, 160));
                } else {
                    login_login_tv.setBackgroundResource(R.drawable.circular_gray_login_bg);
                    login_register_tv.setTextColor(Color.rgb(55, 155, 160));
                    login_retrieve_tv.setTextColor(Color.rgb(119, 119, 119));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入文字后的状态
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 登录
     */
    @OnClick(R.id.login_login_tv)
    private void login_login_tv(View v) {
        if (TextUtils.isEmpty(login_username_et.getText().toString().trim())) {
            makeText("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(login_password_et.getText().toString().trim())) {
            makeText("请输入密码");
            return;
        }
        if (!NetWorksUtils.isNetworkAvailable(LoginUI.this)) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.login));
        Map<String, String> param = new HashMap<>();
        param.put("UserName", login_username_et.getText().toString().trim());
        param.put("Pwd", login_password_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    LoginBean loginBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), LoginBean.class);
                    application.setC(loginBean.getC());
                    JPushInterface.setAlias(LoginUI.this, login_username_et.getText().toString().trim(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int responseCode, String s, Set<String> set) {
                            System.out.println("------------->" + responseCode);
                        }
                    });
                    /*Intent intent = new Intent(getActivity(), TabUI.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);*/
                    finish();
                } else {
                    makeText(wBaseBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    /**
     * 重置密码
     */
    @OnClick(R.id.login_retrieve_tv)
    private void login_retrieve_tv(View v) {
        Intent intent = new Intent(getActivity(), RegisterUI.class);
        intent.putExtra("type", "2");
        startActivity(intent);
    }

    /**
     * 注册
     */
    @OnClick(R.id.login_register_tv)
    private void login_register_tv(View v) {
        Intent intent = new Intent(getActivity(), RegisterUI.class);
        intent.putExtra("type", "1");
        startActivity(intent);
        finish();
    }
}
