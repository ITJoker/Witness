package com.risenb.witness.ui.tasklist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.TaskListBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.tasklist.BaseLazyFragment;
import com.risenb.witness.ui.tasklist.UncertainExecEvidence;
import com.risenb.witness.ui.tasklist.adapter.RecentlyDistanceAdapter;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExecMoneyAtMostFragment extends BaseLazyFragment {
    private Activity mActivity;
    private RecentlyDistanceAdapter mMoneyAdapter;
    @BindView(R.id.moneyatmost_xrecyclerview)
    XRecyclerView mMamXRecyclerView;
    private String layoutmark;
    private int pagecount = 1;

    public static List<TaskListBean.TaskList> moneySearchAddressList = new ArrayList<>();
    public static boolean moneyClickSign = false;
    public static String moneyQueryText = "";

    public static String moneyCityId = "";
    public static String moneyMediaId = "";
    public static String moneyCompanyId = "";

    private boolean isSearchRefresh = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    public static ExecMoneyAtMostFragment newInstance(String mark) {
        ExecMoneyAtMostFragment lf = new ExecMoneyAtMostFragment();
        final Bundle args = new Bundle();
        args.putString("mark", mark);
        lf.setArguments(args);
        return lf;
    }

    //初始化数据
    @Override
    protected void initData() {
        onNetWorkTask();
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moneyatmostfragment, container, false);
        layoutmark = getArguments() != null ? getArguments().getString("mark") : null;
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMamXRecyclerView.setLayoutManager(layoutManager);
        mMamXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mMamXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mMamXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mMamXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (null != moneySearchAddressList && moneySearchAddressList.size() > 0) {
                    /*moneySearchAddressList.clear();*/
                    SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
                }
                pagecount = 1;
                onNetWorkTask();
                mMamXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                pagecount++;
                onNetWorkTask();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getBoolean(getContext(), layoutmark, false)) {
            if (isSearchRefresh) {
                // 点击任务项进入任务详情再返回当前页不将pageCount置为1且屏蔽页面刷新
                isSearchRefresh = false;
                return;
            }
            pagecount = 1;
            if (mMoneyAdapter != null) {
                // 已刷新完所有数据搜索不能再进行上拉加载操作
                mMoneyAdapter.notifyDataSetChanged();
            } else {
                mMoneyAdapter = new RecentlyDistanceAdapter(mActivity, moneySearchAddressList, layoutmark);
                mMamXRecyclerView.setAdapter(mMoneyAdapter);
            }
            mMamXRecyclerView.setNoMore(false);
        } else {
            pagecount = 1;
            onNetWorkTask();
        }
    }

    public void onNetWorkTask() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskList));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(mActivity, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }

        if (isVisible) {
            Utils.getUtils().showProgressDialog(mActivity);
        }

        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("pagecount", String.valueOf(pagecount));
        param.put("pagesize", "10");
        param.put("sort", "1");
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            param.put("type", "0");
            if (SharedPreferencesUtil.getBoolean(getContext(), "NonExecution", false)) {
                // 说明进行过搜索
                param.put("query", moneyQueryText);
            }
        }
        if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            param.put("type", "1");
            if (SharedPreferencesUtil.getBoolean(getContext(), "Executioning", false)) {
                param.put("query", moneyQueryText);
                param.put("CityId", moneyCityId);
                param.put("MediaTreeID", moneyMediaId);
                param.put("CompanyId", moneyCompanyId);
                param.put("state", "1");
                url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskListSearch));
            }
        }

        MyOkHttp.get().post(mActivity, url, param, new GsonResponseHandler<BaseBeans<List<TaskListBean.TaskList>>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskListBean.TaskList>> response) {
                if (isVisible) {
                    MyApplication.handler.postDelayed(new Runnable() {
                        public void run() {
                            Utils.getUtils().dismissDialog();
                        }
                    }, 600);
                }

                List<TaskListBean.TaskList> data = response.getData();
                if (null != data && data.size() > 0) {
                    if (pagecount == 1) {
                        moneySearchAddressList = data;
                        mMoneyAdapter = new RecentlyDistanceAdapter(mActivity, moneySearchAddressList, layoutmark);
                        mMoneyAdapter.setOnItemClickListener(new RecentlyDistanceAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, boolean isGuideClick) {
                                isSearchRefresh = true;
                                Intent intent = new Intent(mActivity, UncertainExecEvidence.class);
                                if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                                    intent.putExtra(NonExecutionTaskFragment.NONEXECUTIONFRAGMENTTASKID, moneySearchAddressList.get(position).getTaskId());
                                } else if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                                    intent.putExtra(ExecutingTaskFragment.EXECUTIONINGFRAGMENT, moneySearchAddressList.get(position).getTaskId());
                                }
                                intent.putExtra("taskid", moneySearchAddressList.get(position).getTaskId());
                                intent.putExtra("modeltype", "3");
                                intent.putExtra("currentpage", 1);
                                intent.putExtra("mark", layoutmark);

                                intent.putExtra("execution", moneySearchAddressList.get(position).getExecution());
                                intent.putExtra("address", moneySearchAddressList.get(position).getCity().concat(moneySearchAddressList.get(position).getAddress()));
                                if (!TextUtils.isEmpty(moneySearchAddressList.get(position).getNote())) {
                                    intent.putExtra("note", moneySearchAddressList.get(position).getNote());
                                }
                                intent.putExtra("isType", moneySearchAddressList.get(position).getShootTpye());
                                intent.putExtra("price", moneySearchAddressList.get(position).getPrice());
                                intent.putStringArrayListExtra("require", moneySearchAddressList.get(position).getRequire());
                                /*intent.putExtra("taskInfo", moneySearchAddressList.get(position));*/

                                intent.putExtra("waterMarkID", moneySearchAddressList.get(position).getWatermark_taskid());
                                intent.putExtra("waterMarkNT", moneySearchAddressList.get(position).getWatermark_meshtime());
                                intent.putExtra("waterMarkXY", moneySearchAddressList.get(position).getWatermark_xydata());
                                intent.putExtra("waterMarkST", moneySearchAddressList.get(position).getWatermark_systemtime());
                                startActivity(intent);
                            }
                        });
                        mMamXRecyclerView.setAdapter(mMoneyAdapter);
                    } else {
                        int startPosition = moneySearchAddressList.size();
                        moneySearchAddressList.addAll(data);
                        /*mMoneyAdapter.notifyItemRangeChanged(startPosition, moneySearchAddressList.size() - 1);*/
                        mMoneyAdapter.notifyDataSetChanged();
                    }
                } else {
                    mMamXRecyclerView.setNoMore(true);
                }
                mMamXRecyclerView.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void setData(List<TaskListBean.TaskList> homeDistanceFrgBeen) {
        if (mMoneyAdapter != null) {
            mMoneyAdapter.setList(homeDistanceFrgBeen);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
    }
}
