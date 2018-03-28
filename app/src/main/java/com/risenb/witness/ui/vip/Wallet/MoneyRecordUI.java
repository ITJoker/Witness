package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.adapter.MoneyRecordAdapter;
import com.risenb.witness.beans.HistoryCrashBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.MoneyRecordPop;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.views.newViews.XListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.moneyrecord)
public class MoneyRecordUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;
    @ViewInject(R.id.moneyrecord_mlv)
    private XListView moneyrecord_mlv;

    private MoneyRecordAdapter moneyRecordAdapter;

    private MoneyRecordPop moneyRecordPop;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("历史记录");
        rightVisible(R.drawable.money_record_shuaixuan);
        moneyRecordAdapter = new MoneyRecordAdapter();
        moneyrecord_mlv.setAdapter(moneyRecordAdapter);
    }

    @Override
    protected void prepareData() {
        history("0");
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 筛选
     */
    @OnClick(R.id.iv_right)
    private void iv_right(View v) {
        moneyRecordPop = new MoneyRecordPop(rl_title, this, R.layout.moneyrecordpop);
        moneyRecordPop.showAsDropDown(v);
        moneyRecordPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.moneyrecordpop_quanbu:
                        setTitle("全部");
                        moneyRecordPop.dismiss();
                        history("0");
                        break;
                    case R.id.moneyrecordpop_shixiao:
                        setTitle("失效");
                        moneyRecordPop.dismiss();
                        history("5");
                        break;
                    case R.id.moneyrecordpop_tixianzhong:
                        setTitle("提现中");
                        moneyRecordPop.dismiss();
                        history("3");
                        break;
                    case R.id.moneyrecordpop_yitixian:
                        setTitle("已提现");
                        moneyRecordPop.dismiss();
                        history("4");
                        break;
                    case R.id.moneyrecordpop_shenhezhong:
                        setTitle("审核中");
                        moneyRecordPop.dismiss();
                        history("2");
                        break;
                    case R.id.moneyrecordpop_shouru:
                        setTitle("任务收入");
                        moneyRecordPop.dismiss();
                        history("1");
                        break;
                }
            }
        });
    }

    private void history(String type) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.history));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("type", type);
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                HistoryCrashBean historyCrashBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), HistoryCrashBean.class);
                if (historyCrashBean.getSuccess() == 1) {
                    moneyRecordAdapter.setList(historyCrashBean.getData());
                } else {
                    if (historyCrashBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                    makeText(historyCrashBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
