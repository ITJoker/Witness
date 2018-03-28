package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.OftensiteBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.commonaddress)
public class CommonAddressUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.vipinfo_commonaddress_home_tv)
    private TextView vipinfo_commonaddress_home_tv;
    @ViewInject(R.id.vipinfo_commonaddress_comany_tv)
    private TextView vipinfo_commonaddress_comany_tv;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("常用地址");
        oftensite();
    }

    private void oftensite() {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.oftensite));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    OftensiteBean oftensite = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), OftensiteBean.class);
                    vipinfo_commonaddress_home_tv.setText(oftensite.getHomesite());
                    vipinfo_commonaddress_comany_tv.setText(oftensite.getCompanysite());
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
    protected void prepareData() {
        // Intent intent = new Intent(getActivity(), ZUI.class);
        // startActivity(intent);
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.back)
    private void test(View v) {
        back();
    }

    /**
     * 家
     */
    @OnClick(R.id.vipinfo_commonaddress_home_ll)
    private void vipinfo_commonaddress_home_ll(View v) {
        Intent intent = new Intent(getActivity(), HomeSearchUI.class);
        startActivityForResult(intent, 1);
    }

    /**
     * 公司
     */
    @OnClick(R.id.vipinfo_commonaddress_company_ll)
    private void vipinfo_commonaddress_company_ll(View v) {
        Intent intent = new Intent(getActivity(), CompanySearchUI.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                vipinfo_commonaddress_home_tv.setText(data.getStringExtra("data"));
                oftenAddress(1, data.getStringExtra("latitude"), data.getStringExtra("longitude"));
            }
        }
        if (requestCode == 2) {
            if (resultCode == 2) {
                vipinfo_commonaddress_comany_tv.setText(data.getStringExtra("data"));
                oftenAddress(2, data.getStringExtra("latitude"), data.getStringExtra("longitude"));
            }
        }
    }

    private void oftenAddress(int styp, String latitude, String longitude) {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.oftenAddress));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        if (styp == 1) {
            param.put("Homelatitude", latitude);
            param.put("Homelongitude", longitude);
            param.put("Homesite", vipinfo_commonaddress_home_tv.getText().toString());
        } else {
            param.put("Companylatitude", latitude);
            param.put("Companylongitude", longitude);
            param.put("Companysite", vipinfo_commonaddress_comany_tv.getText().toString());
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

            }
        });
    }
}
