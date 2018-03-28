package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.UIManager;
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
import com.risenb.witness.pop.VipWalletMoneyOkPop;
import com.risenb.witness.pop.VipWalletMoneyPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.money)
public class MoneyUI extends BaseUI {
    public static final int SET_ACCOUNT_NAME = 2017;

    @ViewInject(R.id.back)
    private RelativeLayout back;

    @ViewInject(R.id.money_price_tv)
    private TextView money_price_tv;
    @ViewInject(R.id.money_balance_tv)
    private TextView money_balance_tv;

    private VipWalletMoneyPop vipWalletMoneyPop;

    private VipWalletMoneyOkPop vipWalletMoneyOkPop;

    private MoneyBean moneyBean;

    private String alipayusername;
    private String alipaytruename;
    private String price;

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
        setTitle("金额");
    }

    @Override
    protected void prepareData() {
        myPrice();
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
                    moneyBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), MoneyBean.class);
                    money_price_tv.setText("￥".concat(moneyBean.getPrice()));
                    money_balance_tv.setText("余额￥".concat(moneyBean.getBalance()));
                    alipayusername = moneyBean.getAlipayusername();
                    alipaytruename = moneyBean.getAlipaytruename();
                    price = moneyBean.getPrice();
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

    @Override
    public void onLoadOver() {

    }

    /**
     * 取现规则
     */
    @OnClick(R.id.money_balance_ll)
    private void money_balance_ll(View v) {
        startActivity(new Intent(this, MoneyRuleUI.class));
    }

    /**
     * 历史记录
     */
    @OnClick(R.id.money_lishijilu)
    private void money_lishijilu(View v) {
        startActivity(new Intent(this, MoneyRecordUI.class));
    }

    /**
     * 提现中
     */
    @OnClick(R.id.money_tixianzhong)
    private void money_tixianzhong(View v) {
        // startActivity(new Intent(this, MoneyRecordTxUI.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_ACCOUNT_NAME) {
            String account = data.getStringExtra("account");
            vipWalletMoneyPop.vipwalletmoneypop_username_et.setText(account);
            String name = data.getStringExtra("name");
            vipWalletMoneyPop.vipwalletmoneypop_name_et.setText(name);
        }
    }

    /**
     * 提现
     */
    @OnClick(R.id.wallet_money_tixian)
    private void wallet_money_tixian(View v) {
        if (Double.parseDouble(moneyBean.getBalance()) <= 5.00) {
            makeText("当前余额不可提现");
            return;
        }
        vipWalletMoneyPop = new VipWalletMoneyPop(v, this, R.layout.vipwalletmoneypop);
        vipWalletMoneyPop.setType(getIntent().getIntExtra("type", -1), alipayusername, alipaytruename, price);
        vipWalletMoneyPop.showAsDropDownInstance();
        vipWalletMoneyPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.moneypop_tijiao_tv:
                        View withdrawView = View.inflate(v.getContext(), R.layout.vipwalletmoneypop, null);
                        // 提现金额
                        String price = vipWalletMoneyPop.vipwalletmoneypop_price_et.getText().toString();
                        // 支付宝姓名
                        String name = vipWalletMoneyPop.vipwalletmoneypop_name_et.getText().toString();
                        // 支付宝账号
                        String account = vipWalletMoneyPop.vipwalletmoneypop_username_et.getText().toString();
                        if (!TextUtils.isEmpty(price)) {
                            if (Double.parseDouble(price) >= 5.00 && Double.parseDouble(price) <= Double.parseDouble(moneyBean.getBalance())) {
                                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(account)) {
                                    makeText("需设置支付宝账号方可提现");
                                    Intent intent = new Intent(v.getContext(), ZhiFuBaoUI.class);
                                    startActivityForResult(intent, SET_ACCOUNT_NAME);
                                    return;
                                } else {
                                    // 未实名认证
                                    if (vipWalletMoneyPop.trueNameCertification.getText().toString().equals("未实名认证")) {
                                        makeText("请您先进行实名认证");
                                        return;
                                    }
                                }
                            } else {
                                makeText("请参考帐户余额进行提现操作");
                                return;
                            }
                        } else {
                            makeText("请输入取现金额");
                            return;
                        }
                        // 申请提现
                        Utils.getUtils().showProgressDialog(getActivity());
                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.withDrawal));
                        Map<String, String> param = new HashMap<>();
                        // token
                        param.put("c", application.getC());
                        // 申请提现金额
                        param.put("price", price);
                        // 支付宝账号
                        param.put("alipayusername", account);
                        // 支付宝真实姓名
                        param.put("alipaytruename", name);
                        MyOkHttp.get().post(application, url, param, new JsonResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                makeText("网络异常");
                                Utils.getUtils().dismissDialog();
                            }

                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                // 请求成功PupWindow消失
                                vipWalletMoneyPop.dismiss();
                                vipWalletMoneyOkPop = new VipWalletMoneyOkPop(v, MoneyUI.this, R.layout.vipwalletmoneyokpop);
                                vipWalletMoneyOkPop.showAsDropDownInstance();
                                Utils.getUtils().dismissDialog();
                                myPrice();
                            }
                        });
                        break;
                    case R.id.moneypop_qurenzheng_tv:
                        startActivity(new Intent(getActivity(), RealNameUI.class));
                        vipWalletMoneyPop.dismiss();
                        break;
                }
            }
        });
    }
}
