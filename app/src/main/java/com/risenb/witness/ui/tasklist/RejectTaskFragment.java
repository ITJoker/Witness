package com.risenb.witness.ui.tasklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.risenb.witness.ui.tasklist.adapter.RecentlyDistanceAdapter;
import com.risenb.witness.ui.tasklist.adapter.RecentlyDistanceAdapter.OnRecyclerViewItemClickListener;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RejectTaskFragment extends Fragment {
    public static final String REJECTASKFRAGEMNTFLAG = "RejectTask";
    private Activity mActivity;
    @BindView(R.id.reject_task_xrecyclerview)
    XRecyclerView mRejectTaskXrecyclerview;
    private String layoutmark;
    private RecentlyDistanceAdapter mAdapter;
    private View view;
    private int pagecount = 1;
    private List<TaskListBean.TaskList> taskLists;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.rejecttaskfragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        layoutmark = getArguments() != null ? getArguments().getString("mark") : null;
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRejectTaskXrecyclerview.setLayoutManager(layoutManager);
        mRejectTaskXrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRejectTaskXrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRejectTaskXrecyclerview.setArrowImageView(R.drawable.iconfont_downgrey);
        mRejectTaskXrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                /*if (null != taskLists && taskLists.size() > 0) {
                    taskLists.clear();
                }*/
                pagecount = 1;
                initData();
                mRejectTaskXrecyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                pagecount++;
                initData();
            }
        });
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pagecount = 1;
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskList));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(mActivity, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }

        Utils.getUtils().showProgressDialog(mActivity);

        Map<String, String> param = new HashMap<>(7);
        param.put("c", MyApplication.getInstance().getC());
        param.put("pagecount", String.valueOf(pagecount));
        param.put("pagesize", "10");
        param.put("type", "4");
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));

        MyOkHttp.get().post(mActivity, url, param, new GsonResponseHandler<BaseBeans<List<TaskListBean.TaskList>>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskListBean.TaskList>> response) {
                MyApplication.handler.postDelayed(new Runnable() {
                    public void run() {
                        Utils.getUtils().dismissDialog();
                    }
                }, 600);

                List<TaskListBean.TaskList> data = response.getData();
                if (null != data && !data.isEmpty()) {
                    if (pagecount == 1) {
                        taskLists = data;
                        mAdapter = new RecentlyDistanceAdapter(mActivity, taskLists, layoutmark);
                        mRejectTaskXrecyclerview.setAdapter(mAdapter);
                    } else {
                        int startPosition = taskLists.size() - 1;
                        taskLists.addAll(data);
                        /*mAdapter.notifyItemRangeChanged(startPosition, taskLists.size() - 1);*/
                        mAdapter.notifyDataSetChanged();
                    }

                    mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, boolean isGuideClick) {
                            Intent intent;
                            // ★★★更正任务
                            if (SharedPreferencesUtil.getBoolean(MyApplication.getInstance(), taskLists.get(position).getTaskId() + "Reject", false)) {
                                // 说明在被驳回页有过操作,直接跳转到未执行页读取数据
                                intent = new Intent(mActivity, UncertainExecEvidence.class);
                            } else {
                                // 跳转到被驳回页面
                                intent = new Intent(mActivity, RejectUI.class);
                            }
                            intent.putExtra(RejectTaskFragment.REJECTASKFRAGEMNTFLAG, taskLists.get(position).getTaskId());

                            intent.putExtra("taskid", taskLists.get(position).getTaskId());
                            intent.putExtra("modeltype", "3");
                            intent.putExtra("currentpage", 1);
                            intent.putExtra("mark", layoutmark);

                            intent.putExtra("execution", taskLists.get(position).getExecution());
                            intent.putExtra("address", taskLists.get(position).getAddress());
                            if (!TextUtils.isEmpty(taskLists.get(position).getNote())) {
                                intent.putExtra("note", taskLists.get(position).getNote());
                            }
                            intent.putExtra("isType", taskLists.get(position).getShootTpye());
                            intent.putExtra("price", taskLists.get(position).getPrice());
                            intent.putStringArrayListExtra("require", taskLists.get(position).getRequire());
                            /*intent.putExtra("taskInfo", taskLists.get(position));*/

                            intent.putExtra("waterMarkID", taskLists.get(position).getWatermark_taskid());
                            intent.putExtra("waterMarkNT", taskLists.get(position).getWatermark_meshtime());
                            intent.putExtra("waterMarkXY", taskLists.get(position).getWatermark_xydata());
                            intent.putExtra("waterMarkST", taskLists.get(position).getWatermark_systemtime());
                            startActivity(intent);
                        }
                    });
                } else {
                    mRejectTaskXrecyclerview.setNoMore(true);
                }
                mRejectTaskXrecyclerview.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
