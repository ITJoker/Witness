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

public class ExecWillExpireFragment extends BaseLazyFragment {
    private Activity mActivity;
    @BindView(R.id.willexpire_xrecyclerview)
    XRecyclerView mWeXrecyclerview;
    private RecentlyDistanceAdapter mWeAdapter;
    private String layoutmark;
    private int pagecount = 1;

    public static List<TaskListBean.TaskList> willExpireSearchAddressList = new ArrayList<>();
    public static boolean willExpireClickSign = false;
    public static String willExpireQueryText = "";

    public static String willExpireCityId = "";
    public static String willExpireMediaId = "";
    public static String willExpireCompanyId = "";

    private boolean isSearchRefresh = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    public static ExecWillExpireFragment newInstance(String mark) {
        ExecWillExpireFragment lf = new ExecWillExpireFragment();
        final Bundle args = new Bundle();
        args.putString("mark", mark);
        lf.setArguments(args);
        return lf;
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
            if (mWeAdapter != null) {
                // 已刷新完所有数据搜索不能再进行上拉加载操作
                mWeAdapter.notifyDataSetChanged();
            } else {
                mWeAdapter = new RecentlyDistanceAdapter(mActivity, willExpireSearchAddressList, layoutmark);
                mWeXrecyclerview.setAdapter(mWeAdapter);
            }
            mWeXrecyclerview.setNoMore(false);
        } else {
            pagecount = 1;
            initData();
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.willexpirefragment, container, false);
        layoutmark = getArguments() != null ? getArguments().getString("mark") : null;
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWeXrecyclerview.setLayoutManager(layoutManager);
        mWeXrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mWeXrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mWeXrecyclerview.setArrowImageView(R.drawable.iconfont_downgrey);
        mWeXrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                        if (null != willExpireSearchAddressList && willExpireSearchAddressList.size() > 0) {
                            /*willExpireSearchAddressList.clear();*/
                            SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
                        }
                        pagecount = 1;
                        initData();
                        mWeXrecyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                        pagecount++;
                        initData();
            }
        });
        return view;
    }

    /**
     * 在onCreateView中执行
     */
    public void initData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskList));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(mActivity, "网络路径无效", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isVisible) {
            Utils.getUtils().showProgressDialog(mActivity);
        }

        Map<String, String> param = new HashMap<>();
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        param.put("c", MyApplication.getInstance().getC());
        param.put("pagecount", String.valueOf(pagecount));
        param.put("pagesize", "10");
        param.put("sort", "3");
        //未执行任务
        if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            param.put("type", "0");
            if (SharedPreferencesUtil.getBoolean(getContext(), "NonExecution", false)) {
                // 说明进行过搜索
                param.put("query", willExpireQueryText);
            }
        }
        //执行中
        if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            //即将到期 时间排序
            param.put("type", "1");
            if (SharedPreferencesUtil.getBoolean(getContext(), "Executioning", false)) {
                param.put("query", willExpireQueryText);
                param.put("CityId", willExpireCityId);
                param.put("MediaTreeID", willExpireMediaId);
                param.put("CompanyId", willExpireCompanyId);
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
                        willExpireSearchAddressList = data;
                        mWeAdapter = new RecentlyDistanceAdapter(mActivity, willExpireSearchAddressList, layoutmark);
                        mWeAdapter.setOnItemClickListener(new RecentlyDistanceAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, boolean isGuideClick) {
                                isSearchRefresh = true;
                                Intent intent = new Intent(mActivity, UncertainExecEvidence.class);
                                if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                                    intent.putExtra(NonExecutionTaskFragment.NONEXECUTIONFRAGMENTTASKID, willExpireSearchAddressList.get(position).getTaskId());
                                } else if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                                    intent.putExtra(ExecutingTaskFragment.EXECUTIONINGFRAGMENT, willExpireSearchAddressList.get(position).getTaskId());
                                }
                                intent.putExtra("taskid", willExpireSearchAddressList.get(position).getTaskId());
                                intent.putExtra("modeltype", "3");
                                intent.putExtra("currentpage", 1);
                                intent.putExtra("mark", layoutmark);

                                intent.putExtra("execution", willExpireSearchAddressList.get(position).getExecution());
                                intent.putExtra("address", willExpireSearchAddressList.get(position).getCity().concat(willExpireSearchAddressList.get(position).getAddress()));
                                if (!TextUtils.isEmpty(willExpireSearchAddressList.get(position).getNote())) {
                                    intent.putExtra("note", willExpireSearchAddressList.get(position).getNote());
                                }
                                intent.putExtra("isType", willExpireSearchAddressList.get(position).getShootTpye());
                                intent.putExtra("price", willExpireSearchAddressList.get(position).getPrice());
                                intent.putStringArrayListExtra("require", willExpireSearchAddressList.get(position).getRequire());
                                /*intent.putExtra("taskInfo", willExpireSearchAddressList.get(position));*/

                                intent.putExtra("waterMarkID", willExpireSearchAddressList.get(position).getWatermark_taskid());
                                intent.putExtra("waterMarkNT", willExpireSearchAddressList.get(position).getWatermark_meshtime());
                                intent.putExtra("waterMarkXY", willExpireSearchAddressList.get(position).getWatermark_xydata());
                                intent.putExtra("waterMarkST", willExpireSearchAddressList.get(position).getWatermark_systemtime());
                                startActivity(intent);
                            }
                        });
                        mWeXrecyclerview.setAdapter(mWeAdapter);
                    } else {
                        int startPosition = willExpireSearchAddressList.size();
                        willExpireSearchAddressList.addAll(data);
                        /*mWeAdapter.notifyItemRangeChanged(startPosition, willExpireSearchAddressList.size() - 1);*/
                        mWeAdapter.notifyDataSetChanged();
                    }
                } else {
                    mWeXrecyclerview.setNoMore(true);
                }
                mWeXrecyclerview.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void setData(List<TaskListBean.TaskList> homeDistanceFrgBeen) {
        if (mWeAdapter != null) {
            mWeAdapter.setList(homeDistanceFrgBeen);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
    }
}
