package com.risenb.witness.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.SettledTaskAdapter;
import com.risenb.witness.beans.SettledTaskBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.pop.RobTaskLoginPop;
import com.risenb.witness.ui.tasklist.BaseLazyFragment;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;
import com.risenb.witness.views.newViews.XListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 固定任务
 */
public class SettledTaskFragment extends BaseLazyFragment implements XListView.IXListViewListener {
    @ViewInject(R.id.home_homedistance_lv)
    private XListView settledTask_lv;

    private SettledTaskAdapter settledTaskAdapter;
    private List<SettledTaskBean.DataBean> settledTaskDataList;
    public static boolean settledTaskSign;

    private TextView settled_task_title_tv, settled_task_province_tv, settled_task_city_tv, settled_task_price_tv, settled_task_number_tv;
    private ImageView settled_task_image_iv;
    private ProgressBar settled_task_time_pb;
    private CountDownTimerView settled_task_count_down_timer;

    private View view;
    private String c;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.from(getActivity()).inflate(R.layout.homedistancefragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        ViewUtils.inject(this, view);

        /*View settledTaskHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.home_settled_task_item, null, false);
        settled_task_title_tv = (TextView) settledTaskHeaderView.findViewById(R.id.settled_task_title_tv);
        settled_task_province_tv = (TextView) settledTaskHeaderView.findViewById(R.id.settled_task_province_tv);
        settled_task_city_tv = (TextView) settledTaskHeaderView.findViewById(R.id.settled_task_city_tv);
        settled_task_price_tv = (TextView) settledTaskHeaderView.findViewById(R.id.settled_task_price_tv);
        settled_task_number_tv = (TextView) settledTaskHeaderView.findViewById(R.id.settled_task_number_tv);
        settled_task_image_iv = (ImageView) settledTaskHeaderView.findViewById(R.id.settled_task_image_iv);
        settled_task_count_down_timer = (CountDownTimerView) settledTaskHeaderView.findViewById(R.id.settled_task_count_down_timer);
        settled_task_time_pb = (ProgressBar) settledTaskHeaderView.findViewById(R.id.settled_task_time_pb);
        settledTaskHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "演示任务", Toast.LENGTH_SHORT).show();
            }
        });
        settledTask_lv.addHeaderView(settledTaskHeaderView);*/

        settledTask_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1) {
                    String settledTaskProvince = settledTaskDataList.get((int) id).getProvince_name();
                    String settledTaskCity = settledTaskDataList.get((int) id).getCity_name();
                    if (!TextUtils.isEmpty(settledTaskProvince)) {
                        if (!TextUtils.isEmpty(settledTaskCity)) {
                            if (HomeUI.address.contains(settledTaskProvince) && HomeUI.address.contains(settledTaskCity)) {
                                allowEnteringNextUI(view, (int) id);
                            } else {
                                Toast.makeText(getContext(), "当前定位与任务位置不匹配", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (HomeUI.address.contains(settledTaskProvince)) {
                                allowEnteringNextUI(view, (int) id);
                            } else {
                                Toast.makeText(getContext(), "当前定位与任务位置不匹配", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(settledTaskCity)) {
                            allowEnteringNextUI(view, (int) id);
                        }
                    }
                }
            }
        });
        return view;
    }

    private void allowEnteringNextUI(View view, int id) {
        if ("true".equals(settledTaskDataList.get(id).getIsdemo())) {
            startActivity(new Intent(getContext(), SettledTaskExecutionUI.class)
                    .putExtra("fixedID", settledTaskDataList.get(id).getFixedid())
                    .putExtra("isDemo", settledTaskDataList.get(id).getIsdemo()));
        } else {
            if (TextUtils.isEmpty(MyApplication.getInstance().getC())) {
                final RobTaskLoginPop robTaskLoginPop = new RobTaskLoginPop(view, getActivity(), R.layout.robtaskloginpop);
                robTaskLoginPop.showAsDropDownInstance();
                robTaskLoginPop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.robtaskloginpop_ok_tv:
                                startActivity(new Intent(getContext(), LoginUI.class));
                                robTaskLoginPop.dismiss();
                                break;
                        }
                    }
                });
            } else {
                startActivity(new Intent(getContext(), SettledTaskExecutionUI.class).putExtra("fixedID", settledTaskDataList.get(id).getFixedid()));
            }
        }
    }

    @Override
    protected void initData() {
        settledTask_lv.setXListViewListener(this);
    }

    private void taskList(final int page) {
        if (!NetWorksUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isVisible) {
            Utils.getUtils().showProgressDialog(getActivity());
        }
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.settledTask));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        c = MyApplication.getInstance().getC();
        param.put("c", MyApplication.getInstance().getC());

        MyOkHttp.get().post(getContext(), url, param, new GsonResponseHandler<SettledTaskBean>() {
            @Override
            public void onSuccess(int statusCode, SettledTaskBean response) {
                if (isVisible) {
                    MyApplication.handler.postDelayed(new Runnable() {
                        public void run() {
                            Utils.getUtils().dismissDialog();
                        }
                    }, 600);
                }

                if (response.getSuccess() == 1) {
                    List<SettledTaskBean.DataBean> data = response.getData();
                    if (data != null && data.size() > 0) {
                        if (page == 1) {
                            settledTaskDataList = data;
                            settledTaskAdapter = new SettledTaskAdapter(getContext(), settledTaskDataList);
                            settledTask_lv.setAdapter(settledTaskAdapter);
                        } else {
                            settledTaskDataList.addAll(data);
                            settledTaskAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (response.getErrorMsg().equals("登录异常")) {
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
    public void onLoad(int i) {
        taskList(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settledTaskSign) {
            taskList(1);
            settledTaskSign = false;
            return;
        }
        if (!c.equals(MyApplication.getInstance().getC())) {
            if (settledTaskDataList != null) {
                settledTaskDataList.clear();
            }
            taskList(1);
        }
    }
}


