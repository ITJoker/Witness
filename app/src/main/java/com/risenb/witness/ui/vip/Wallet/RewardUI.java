package com.risenb.witness.ui.vip.Wallet;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.adapter.RewardAdapter;
import com.risenb.witness.beans.RewardBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.views.newViews.MyListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.reward)
public class RewardUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.reward_mlv)
    private MyListView reward_mlv;

    private RewardAdapter rewardAdapter;

    private List<RewardBean> rewardBeen;

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
        rewardAdapter = new RewardAdapter();
        reward_mlv.setAdapter(rewardAdapter);
        CardList();
    }

    @Override
    protected void prepareData() {
        rewardAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RewardInfoUI.class);
                intent.putExtra("id", rewardBeen.get(position).getCardId());
                startActivity(intent);
            }
        });
    }

    private void CardList() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.CardList));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                rewardBeen = JSONArray.parseArray(wBaseBean.getData(), RewardBean.class);
                rewardAdapter.setList(rewardBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText(error_msg);
            }
        });
    }

    @Override
    public void onLoadOver() {

    }
}
