package com.risenb.witness.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
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
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.TabUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.register)
public class RegisterUI extends BaseUI {
    @ViewInject(R.id.rgister_code_tv)
    private TextView rgister_code_tv;
    @ViewInject(R.id.register_register_tv)
    private TextView register_register_tv;
    @ViewInject(R.id.register_title_show)
    private TextView register_title_show;

    @ViewInject(R.id.register_username_et)
    private EditText register_username_et;
    @ViewInject(R.id.register_code_et)
    private EditText register_code_et;
    private String type;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("");
        setTitleBackground(255, 255, 255);
        backGone();
    }

    @Override
    protected void prepareData() {
        type = getIntent().getStringExtra("type");
        if ("2".equals(type)) {
            register_title_show.setText("找回密码");
        } else if ("1".equals(type)) {
            register_title_show.setText("注册账号");
        }
        register_username_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    rgister_code_tv.setBackgroundColor(Color.rgb(55, 155, 160));
                    rgister_code_tv.setTextColor(Color.rgb(255, 255, 255));
                    register_username_et.clearFocus();
                    register_code_et.requestFocus();
                } else {
                    rgister_code_tv.setBackgroundColor(Color.rgb(236, 236, 236));
                    rgister_code_tv.setTextColor(Color.rgb(169, 169, 169));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("1".equals(type)) {
                    CharSequence ss = temp;
                    if (temp.length() == 11) {
                        if (!NetWorksUtils.isNetworkAvailable(RegisterUI.this)) {
                            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.PhoneVerify));
                        Map<String, String> param = new HashMap<>();
                        param.put("tel", ss.toString());
                        MyOkHttp.get().post(RegisterUI.this, url, param, new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                                if (!TextUtils.isEmpty(wBaseBean.getErrorMsg())) {
                                    makeText(wBaseBean.getErrorMsg());
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {

                            }
                        });
                    }
                }
            }
        });

        register_code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(register_code_et.getWindowToken(), 0);*/
                    register_code_et.clearFocus();
                    register_register_tv.setBackgroundResource(R.drawable.circular_green_login_bg);
                } else {
                    register_register_tv.setBackgroundResource(R.drawable.circular_gray_login_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 下一步
     */
    @OnClick(R.id.register_register_tv)
    private void register_register_tv(View v) {
        if (TextUtils.isEmpty(register_username_et.getText().toString().trim())) {
            makeText("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(register_code_et.getText().toString().trim())) {
            makeText("请输入验证码");
            return;
        }
        Intent intent = new Intent(this, SetPasswordUI.class);
        intent.putExtra("userPhoneNumber", register_username_et.getText().toString().trim());
        intent.putExtra("verificationCode", register_code_et.getText().toString().trim());
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }

    /**
     * 登录
     */
    @OnClick(R.id.register_login_tv)
    private void register_login_tv(View v) {
        Intent intent = new Intent(getActivity(), LoginUI.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳过直达任务大厅
     */
    @OnClick(R.id.register_tiaoguo_tv)
    private void register_tiaoguo_tv(View v) {
        Intent intent = new Intent(getActivity(), TabUI.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        finish();
    }

    /**
     * 验证码
     */
    @OnClick(R.id.rgister_code_tv)
    private void register_code_tv(View v) {
        if (TextUtils.isEmpty(register_username_et.getText().toString().trim())) {
            makeText("请输入用户名");
            return;
        }
        if (register_username_et.getText().toString().trim().length() != 11) {
            makeText("请输入正确的手机号");
            return;
        }
        if (!NetWorksUtils.isNetworkAvailable(RegisterUI.this)) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        sendCode();
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.getCode));
        Map<String, String> param = new HashMap<>();
        param.put("tel", register_username_et.getText().toString().trim());
        param.put("type", type);
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    makeText("验证码发送成功");
                } else {
                    // 该手机号未被注册
                    makeText(wBaseBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    private void sendCode() {
        RegisterUI.MyCount time = new RegisterUI.MyCount(60000, 1000);
        time.start();
        Utils.getUtils().dismissDialog();
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            rgister_code_tv.setText("获取验证码");
            rgister_code_tv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            rgister_code_tv.setText(millisUntilFinished / 1000 + "秒后重发");
            rgister_code_tv.setClickable(false);
        }
    }
}
