package com.risenb.witness.ui.tasklist;

import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.adapter.OverdueTaskInfoAdapter;
import com.risenb.witness.beans.OverdueTaskInfoBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.XListView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

@ContentView(R.layout.overdueinfo)
public class OverdueTaskinfo extends BaseUI implements XListView.IXListViewListener {
    @ViewInject(R.id.overdueTaskinfo_xrecyclerview)
    private XListView mOverdueTask_XRecyclerView;
    private OverdueTaskInfoAdapter mOverAdapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("过期/放弃任务");
    }

    @Override
    protected void prepareData() {
        mOverAdapter = new OverdueTaskInfoAdapter();
        mOverdueTask_XRecyclerView.setAdapter(mOverAdapter);
        mOverdueTask_XRecyclerView.setXListViewListener(this);
    }

    @Override
    public void onLoadOver() {

    }

    private void taskList(final int page) {
        if (!NetWorksUtils.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            Utils.getUtils().dismissDialog();
            return;
        }
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.disableTask));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        Utils.getUtils().showProgressDialog(getActivity());
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<OverdueTaskInfoBean>() {
            @Override
            public void onSuccess(int statusCode, OverdueTaskInfoBean response) {
                Utils.getUtils().dismissDialog();
                if (response.getSuccess() == 1) {
                    if (page == 1) {
                        mOverAdapter.clearList();
                        mOverAdapter.setList(response.getData());
                    } else {
                        mOverAdapter.addList(response.getData());
                    }
                } else {
                    makeText(response.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
        /*MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                System.out.println(response.toString());
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<OverdueTaskInfoBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), OverdueTaskInfoBean.class);
                if (page == 1) {
                    overdueTaskInfoBeen.clear();
                    mOverAdapter.setList(homeDistanceFrgBeen);
                } else {
                    mOverAdapter.addList(homeDistanceFrgBeen);
                }
                overdueTaskInfoBeen.addAll(homeDistanceFrgBeen);
                makeText(wBaseBean.getErrorMsg());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });*/
    }

    @Override
    public void onLoad(int i) {
        taskList(i);
    }
}
