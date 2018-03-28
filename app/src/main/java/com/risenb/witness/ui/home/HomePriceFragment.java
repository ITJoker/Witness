package com.risenb.witness.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.HomePriceAdapter;
import com.risenb.witness.beans.HomeDistanceFrgBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.tasklist.BaseLazyFragment;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.XListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金额最高
 */
public class HomePriceFragment extends BaseLazyFragment implements XListView.IXListViewListener {
    @ViewInject(R.id.home_homeprice_lv)
    private XListView home_homeprice_lv;

    private View view;
    private HomePriceAdapter adapter;
    private MyApplication application;
    private Activity activity;
    private Handler mHandler;
    public static boolean isHomePriceSearch = false;
    public static String cityId = "";
    public static String mediaId = "";
    public static String companyId = "";
    public static String searchContent = "";

    private List<HomeDistanceFrgBean> homeDistanceFrgBeens = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.from(getActivity()).inflate(R.layout.homepricefragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        ViewUtils.inject(this, view);
        mHandler = new Handler();
        application = (MyApplication) getActivity().getApplication();
        home_homeprice_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id != -1) {
                    startActivityForResult(new Intent(getContext(), HomeTaskInfoUI.class).putExtra("taskId", homeDistanceFrgBeens.get((int) id).getTaskId()), 2);
                }
            }
        });
        adapter = new HomePriceAdapter(getActivity());
        home_homeprice_lv.setAdapter(adapter);
        return view;
    }

    @Override
    protected void initData() {
        home_homeprice_lv.setXListViewListener(this);
    }

    private void taskList(final int page) {
        if (!NetWorksUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isVisible) {
            Utils.getUtils().showProgressDialog(getActivity());
        }
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskList));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        param.put("sort", "1");
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.json.JSONObject response) {
                if (isVisible) {
                    MyApplication.handler.postDelayed(new Runnable() {
                        public void run() {
                            Utils.getUtils().dismissDialog();
                        }
                    }, 600);
                }

                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<HomeDistanceFrgBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), HomeDistanceFrgBean.class);
                if (page == 1) {
                    isHomePriceSearch = false;
                    homeDistanceFrgBeens.clear();
                    adapter.setList(homeDistanceFrgBeen);
                } else {
                    adapter.addList(homeDistanceFrgBeen);
                }
                homeDistanceFrgBeens.addAll(homeDistanceFrgBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void setData(List<HomeDistanceFrgBean> homeDistanceFrgBeen) {
        homeDistanceFrgBeens.clear();
        if (adapter != null) {
            adapter.setList(homeDistanceFrgBeen);
        }
        homeDistanceFrgBeens.addAll(homeDistanceFrgBeen);
    }

    @Override
    public void onLoad(int i) {
        if (isHomePriceSearch) {
            switch (i) {
                case 1:
                    taskList(i);
                    break;
                default:
                    loadSearch(i);
                    break;
            }
        } else {
            taskList(i);
        }
    }

    public void loadSearch(int page) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskSearch));
        Map<String, String> param = new HashMap<>();
        param.put("query", searchContent);
        if (!TextUtils.isEmpty(cityId)) {
            param.put("CityId", cityId);
        }
        if (!TextUtils.isEmpty(mediaId)) {
            param.put("MediaTreeID", mediaId);
        }
        if (!TextUtils.isEmpty(companyId)) {
            param.put("CompanyId", companyId);
        }
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        param.put("sort", "2");
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        MyOkHttp.get().post(getContext(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                MyApplication.handler.postDelayed(new Runnable() {
                    public void run() {
                        Utils.getUtils().dismissDialog();
                    }
                }, 600);

                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<HomeDistanceFrgBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), HomeDistanceFrgBean.class);
                if (homeDistanceFrgBeen != null && homeDistanceFrgBeen.size() > 0) {
                    isHomePriceSearch = true;
                    homeDistanceFrgBeens.addAll(homeDistanceFrgBeen);
                    adapter.addList(homeDistanceFrgBeen);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
