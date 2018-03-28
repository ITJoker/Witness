package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.adapter.MoneyRecordAdapter;
import com.risenb.witness.beans.MoneyRecordBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.views.newViews.MyListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.moneyrecordtx)
public class MoneyRecordTxUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;

    @ViewInject(R.id.moneyrecordtx_mlv)
    private MyListView moneyrecordtx_mlv;

    private MoneyRecordAdapter moneyRecordAdapter;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("提现中");
        moneyRecordAdapter = new MoneyRecordAdapter();
        moneyrecordtx_mlv.setAdapter(moneyRecordAdapter);
    }

    @Override
    protected void prepareData() {
        withdrawOf();
    }

    @Override
    public void onLoadOver() {

    }

    private void withdrawOf() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.withdrawOf));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    List<MoneyRecordBean> moneyRecordBean = JSONArray.parseArray(wBaseBean.getData(), MoneyRecordBean.class);
                    moneyRecordAdapter.setList(moneyRecordBean);
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

}
