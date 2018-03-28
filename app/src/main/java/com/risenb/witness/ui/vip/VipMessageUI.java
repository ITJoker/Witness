package com.risenb.witness.ui.vip;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.Remark;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.views.newViews.SwipeLayout;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;
import com.risenb.witness.views.newViews.MyListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.vipmessge)
public class VipMessageUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.vipmessge_mlv)
    private MyListView vipmessge_mlv;

    private VipMessagesAdapter vipMessgeAdapter;

    private List<Remark> remark;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("消息通知");
        vipMessgeAdapter = new VipMessagesAdapter();
        vipmessge_mlv.setAdapter(vipMessgeAdapter);
        messageUpdate();
        messageCenter();
    }

    @Override
    protected void prepareData() {

    }

    /**
     * 后台将用户消息全部置为已读状态
     */
    private void messageUpdate() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.messageUpdate));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<String>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                System.out.println(error_msg);
            }

            @Override
            public void onSuccess(int statusCode, String response) {
                System.out.println(response);
            }
        });
    }

    private void messageCenter() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.messageCenter));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    remark = JSONArray.parseArray(wBaseBean.getData(), Remark.class);
                    vipMessgeAdapter.setList(remark);
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

    // 用来存放打开的swipeLayout
    private ArrayList<SwipeLayout> swipeLayouts = new ArrayList<>();

    public class VipMessagesAdapter<T extends Remark> extends BaseListAdapter<T> {

        @Override
        protected BaseViewHolder<T> loadView(Context context, T bean, int position) {
            return new ViewHolder(context, getViewId(bean, position));
        }

        @Override
        protected int getViewId(T bean, int position) {
            return R.layout.vipmessges;
        }

        private class ViewHolder extends BaseViewHolder<T> implements SwipeLayout.OnSwipeStateChangeListener {
            @ViewInject(R.id.vipmessge_item_tv)
            private TextView vipmessge_item_tv;

            @ViewInject(R.id.item_delete_tv)
            private LinearLayout item_delete_tv;
            @ViewInject(R.id.vipmessge_sv)
            private SwipeLayout vipmessge_sv;

            public ViewHolder(Context context, int layoutID) {
                super(context, layoutID);
            }

            @Override
            protected void prepareData() {
                vipmessge_sv.setOnSwipeStateChangeListener(this);
                vipmessge_item_tv.setText(bean.getRemark());
                item_delete_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delMessage(remark.get(position).getMessgid());
                    }
                });
            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                // 当swipeLayout关闭的时候，应该从swipeLayouts清除
                if (swipeLayouts.contains(swipeLayout)) {
                    swipeLayouts.remove(swipeLayout);
                }
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                // 当每次打开一个swipeLayout的时候，都需要存放swipeLayout
                if (!swipeLayouts.contains(swipeLayout)) {
                    swipeLayouts.add(swipeLayout);
                }
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                // 当开始打开一个的时候，让swipeLayouts所有打开的layout都关闭
                closeSwipeLayout();
            }
        }
    }

    private void closeSwipeLayout() {
        for (SwipeLayout swipeLayout : swipeLayouts) {
            swipeLayout.close();
        }
    }

    private void delMessage(String messgid) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.delMessage));
        Map<String, String> param = new HashMap<>();
        param.put("messgid", messgid);
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    makeText(wBaseBean.getErrorMsg());
                    messageCenter();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

}
