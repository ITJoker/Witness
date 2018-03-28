package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.beans.WalletBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.wallet)
public class WalletUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.wallet_price_tv)
    private TextView wallet_price_tv;
    @ViewInject(R.id.wallet_Integral_tv)
    private TextView wallet_Integral_tv;

    private int is_approve;

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
        setTitle("钱包");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        myWallet();
    }

    private void myWallet() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myWallet));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    WalletBean walletBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), WalletBean.class);
                    wallet_price_tv.setText("￥" + walletBean.getPrice());
                    is_approve = walletBean.getIs_approve();
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

    @Override
    public void onLoadOver() {

    }

    //金额
    @OnClick(R.id.wallet_money_ll)
    private void Money(View view) {
        startActivity(new Intent(this, MoneyUI.class).putExtra("type", is_approve));
    }

    //支付宝
    @OnClick(R.id.wallet_zhifubao_ll)
    private void wallet_zhifubao_ll(View view) {
        startActivity(new Intent(this, ZhiFuBaoUI.class));
    }

    //实名认证
    @OnClick(R.id.wallet_realname_ll)
    private void wallet_realname_ll(View view) {
        startActivity(new Intent(this, RealNameUI.class).putExtra("type", is_approve));
    }

    //奖励卡券
    @OnClick(R.id.wallet_reward_ll)
    private void wallet_reward_ll(View view) {
        makeText("敬请期待");
//        startActivity(new Intent(this, RewardUI.class));
    }

    //积分商场
    @OnClick(R.id.wallet_jifen_ll)
    private void wallet_jifen_ll(View view) {
        makeText("敬请期待");
//        startActivity(new Intent(this, RewardUI.class));
    }
}
