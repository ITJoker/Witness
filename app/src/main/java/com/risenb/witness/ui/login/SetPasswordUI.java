package com.risenb.witness.ui.login;

import android.content.Intent;
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
import com.risenb.witness.beans.RegisterBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.TabUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.PwdCheckUtil;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.set_password_activity)
public class SetPasswordUI extends BaseUI {
    @ViewInject(R.id.register_register_tv)
    private TextView register_register_tv;

    @ViewInject(R.id.register_password_et)
    private EditText register_password_et;
    @ViewInject(R.id.register_passwordqr_et)
    private EditText register_passwordqr_et;
    private String userPhoneNumber = "";
    private String verificationCode = "";
    private String type;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("");
        backGone();
    }

    @Override
    protected void prepareData() {
        type = getIntent().getStringExtra("type");
        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");
        verificationCode = getIntent().getStringExtra("verificationCode");
        register_passwordqr_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入文本之前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入文字中的状态，count是一次性输入字符数
                if (s.length() >= 6 && register_password_et.getText().toString().trim().length() >= 6) {
                    register_register_tv.setBackgroundResource(R.drawable.circular_green_login_bg);
                } else {
                    register_register_tv.setBackgroundResource(R.drawable.circular_gray_login_bg);
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
     * 注册
     */
    @OnClick(R.id.register_register_tv)
    private void register_register_tv(View v) {
        if (TextUtils.isEmpty(register_password_et.getText().toString().trim())) {
            makeText("请输入密码");
            return;
        }
        if (register_password_et.getText().toString().trim().length() < 6 || register_password_et.getText().toString().trim().length() > 16) {
            makeText("密码为包含字母和数字的6-16个字符");
            return;
        }
        if (!PwdCheckUtil.isLetterDigit(register_password_et.getText().toString().trim())) {
            makeText("密码为包含字母和数字的6-16个字符");
            return;
        }
        if (TextUtils.isEmpty(register_passwordqr_et.getText().toString().trim())) {
            makeText("请确认密码");
            return;
        }
        if (!register_password_et.getText().toString().trim().equals(register_passwordqr_et.getText().toString().trim())) {
            makeText("两次密码不一致");
            return;
        }

        if (!NetWorksUtils.isNetworkAvailable(SetPasswordUI.this)) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("2".equals(type)) {
            changePassword();
        } else if ("1".equals(type)) {
            registerAccount();
        }
    }

    private void registerAccount() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.reg));
        Map<String, String> param = new HashMap<>();
        param.put("tel", userPhoneNumber);
        param.put("password", register_password_et.getText().toString().trim());
        param.put("repassword", register_passwordqr_et.getText().toString().trim());
        param.put("code", verificationCode);
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    RegisterBean loginBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), RegisterBean.class);
                    application.setC(loginBean.getC());
                    Intent intent = new Intent(getActivity(), TabUI.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    finish();
                }
                makeText(wBaseBean.getErrorMsg());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void changePassword() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.getPassword));
        Map<String, String> param = new HashMap<>();
        param.put("tel", userPhoneNumber);
        param.put("pwd", register_password_et.getText().toString().trim());
        param.put("confirmpwd", register_passwordqr_et.getText().toString().trim());
        param.put("code", verificationCode);
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
}
