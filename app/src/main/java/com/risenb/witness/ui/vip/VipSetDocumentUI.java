package com.risenb.witness.ui.vip;

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
import com.risenb.witness.adapter.VipSetDocumentAdapter;
import com.risenb.witness.beans.VipSetDocumentBeans;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.views.newViews.MyListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.vipsetdocument)
public class VipSetDocumentUI extends BaseUI {
    @ViewInject(R.id.vipsetdocument_mlv)
    private MyListView vipsetdocument_mlv;

    private VipSetDocumentAdapter vipSetDocumentAdapter;

    private List<VipSetDocumentBeans> vipSetDocumentBeanses;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("帮助文档");
        vipSetDocumentAdapter = new VipSetDocumentAdapter();
        vipsetdocument_mlv.setAdapter(vipSetDocumentAdapter);
    }

    @Override
    protected void prepareData() {
        vipSetDocumentAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), VipSetDocumentInfoUI.class).putExtra("getTypeId", vipSetDocumentBeanses.get(position).getTypeId()));
            }
        });
        helptype();
    }

    private void helptype() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.helptype));
        Map<String, String> param = new HashMap<>();
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    vipSetDocumentBeanses = JSONArray.parseArray(wBaseBean.getData(), VipSetDocumentBeans.class);
                    vipSetDocumentAdapter.setList(vipSetDocumentBeanses);
                } else {
                    makeText(wBaseBean.getErrorMsg());
                }
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
