package com.risenb.witness.ui.vip.Wallet;

import android.widget.ImageView;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.RewardInfoBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.MyConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.rewardinfo)
public class RewardInfoUI extends BaseUI {
    @ViewInject(R.id.rewardinfo_logo_iv)
    private ImageView rewardinfo_logo_iv;
    @ViewInject(R.id.rewardinfo_name_tv)
    private TextView rewardinfo_name_tv;

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
        setTitle("奖励卡券");
        CardContent();
    }

    @Override
    protected void prepareData() {

    }

    private void CardContent() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.CardContent));
        Map<String, String> param = new HashMap<>();
        param.put("cardId", getIntent().getStringExtra("id"));
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                RewardInfoBean rewardInfoBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), RewardInfoBean.class);
                rewardinfo_name_tv.setText(rewardInfoBean.getCardName());
                ImageLoader.getInstance().displayImage(rewardInfoBean.getCardLogo(), rewardinfo_logo_iv, MyConfig.optionss);
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
}
