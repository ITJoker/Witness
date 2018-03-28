package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.MoneyBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.zhifubao)
public class ZhiFuBaoUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.wallet_zhifubao_user_et)
    private EditText wallet_zhifubao_user_et;
    @ViewInject(R.id.wallet_zhifubao_true_et)
    private EditText wallet_zhifubao_true_et;

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
        setTitle("取现帐户");
        myPrice();
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 常见问题
     */
    @OnClick(R.id.wallet_zhifubao_ll)
    private void wallet_zhifubao_ll(View v) {
        startActivity(new Intent(this, ZhiFuBaoProblemUI.class));
    }

    private void myPrice() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myPrice));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    MoneyBean moneyBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), MoneyBean.class);
                    wallet_zhifubao_user_et.setText(moneyBean.getAlipayusername());
                    wallet_zhifubao_true_et.setText(moneyBean.getAlipaytruename());
                    wallet_zhifubao_user_et.setSelection(wallet_zhifubao_user_et.getText().length());
                    wallet_zhifubao_true_et.setSelection(wallet_zhifubao_true_et.getText().length());

                    Intent intent = new Intent();
                    intent.putExtra("account", wallet_zhifubao_user_et.getText().toString());
                    intent.putExtra("name", wallet_zhifubao_true_et.getText().toString());
                    // 设置结果，并进行传送
                    setResult(20170217, intent);
                } else {
                    makeText(wBaseBean.getErrorMsg());
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
     * 保存
     */
    @OnClick(R.id.wallet_zhifubaobc_tv)
    private void wallet_zhifubaobc_tv(View v) {
        if (TextUtils.isEmpty(wallet_zhifubao_user_et.getText().toString().trim())) {
            makeText("请输入支付宝账号");
            return;
        }
        if (TextUtils.isEmpty(wallet_zhifubao_true_et.getText().toString().trim())) {
            makeText("请输入支付宝实名");
            return;
        }
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.setAlipayNumber));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("payUserName", wallet_zhifubao_user_et.getText().toString().trim());
        param.put("payTrueName", wallet_zhifubao_true_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
                if (wBaseBean.getSuccess().equals("1")) {
                    // 在服务器中修改完成后传递最新的数据
                    Intent intent = new Intent();
                    intent.putExtra("account", wallet_zhifubao_user_et.getText().toString());
                    intent.putExtra("name", wallet_zhifubao_true_et.getText().toString());
                    // 设置结果，并进行传送
                    setResult(20170217, intent);
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
                makeText(error_msg);
            }
        });
    }

}
