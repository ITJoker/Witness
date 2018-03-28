package com.risenb.witness.ui.tasklist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.risenb.witness.ui.tasklist.HistoryTask;
import com.risenb.witness.ui.tasklist.adapter.CompletedUploadAdapter;
import com.risenb.witness.ui.tasklist.adapter.CompletedUploadAdapter.OnRecyclerViewItemClickListener;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommonTaskUploadedFragment extends Fragment {
    public static final String COMUPLOADFRAGMENT = "CompletedUpload";
    @BindView(R.id.completedupload_xrecyclerview)
    XRecyclerView mCompLoadXrecyclerview;
    private CompletedUploadAdapter mComUpAdapter;
    private String layoutmark;
    private Activity mActivity;
    private Unbinder unbinder;
    int pagecount = 1;

    public static List<TaskListBean.TaskList> taskLists = new ArrayList<>();

    public static String completedUploadQueryText = "";
    public static String completedUploadCityId = "";
    public static String completedUploadMediaId = "";
    public static String completedUploadCompanyId = "";
    public static String completedUploadStartTime = "";
    public static String completedUploadEndTime = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    public static CommonTaskUploadedFragment newInstance(String mark) {
        CommonTaskUploadedFragment commonTaskUploadedFragment = new CommonTaskUploadedFragment();
        final Bundle args = new Bundle();
        args.putString("mark", mark);
        commonTaskUploadedFragment.setArguments(args);
        return commonTaskUploadedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getBoolean(getContext(), layoutmark, false)) {
            /*
             * 进行过搜索
             */
            pagecount = 1;
            // 已上传Fragment搜索到数据后重新将List传递给Adapter并设置给RecyclerView
            mComUpAdapter = new CompletedUploadAdapter(mActivity, taskLists, layoutmark);
            mCompLoadXrecyclerview.setAdapter(mComUpAdapter);
            mComUpAdapter.notifyDataSetChanged();
            mCompLoadXrecyclerview.refreshComplete();
            // 可以继续上拉加载数据
            mCompLoadXrecyclerview.setNoMore(false);
        } else {
            /*
             * 未进行过搜索
             */
            if (mComUpAdapter != null) {
                mComUpAdapter.notifyDataSetChanged();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completedupload, container, false);
        unbinder = ButterKnife.bind(this, view);
        layoutmark = getArguments() != null ? getArguments().getString("mark") : null;
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCompLoadXrecyclerview.setLayoutManager(layoutManager);
        mCompLoadXrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mCompLoadXrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mCompLoadXrecyclerview.setArrowImageView(R.drawable.iconfont_downgrey);
        mCompLoadXrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (null != taskLists && taskLists.size() > 0) {
                            /*taskLists.clear();*/
                    SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
                }
                pagecount = 1;
                initData();
                mCompLoadXrecyclerview.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                pagecount++;
                initData();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.uploadList));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(mActivity, "无效路径", Toast.LENGTH_LONG).show();
            return;
        }

        Utils.getUtils().showProgressDialog(mActivity);

        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("pagecount", String.valueOf(pagecount));
        param.put("pagesize", "10");
        if (SharedPreferencesUtil.getBoolean(getContext(), "CompletedUpload", false)) {
            // 说明进行过搜索
            param.put("query", completedUploadQueryText);
            param.put("CityId", completedUploadCityId);
            param.put("MediaTreeID", completedUploadMediaId);
            param.put("CompanyId", completedUploadCompanyId);
            param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
            param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
            param.put("state", "2");
            param.put("starttime", completedUploadStartTime);
            param.put("endtime", completedUploadEndTime);
            url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskListSearch));
        }

        MyOkHttp.get().post(mActivity, url, param, new GsonResponseHandler<BaseBeans<List<TaskListBean.TaskList>>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskListBean.TaskList>> response) {
                MyApplication.handler.postDelayed(new Runnable() {
                    public void run() {
                        Utils.getUtils().dismissDialog();
                    }
                }, 600);

                List<TaskListBean.TaskList> data = response.getData();
                if (null != data && data.size() > 0) {
                    if (pagecount == 1) {
                        taskLists = data;
                        mComUpAdapter = new CompletedUploadAdapter(mActivity, taskLists, layoutmark);
                        mCompLoadXrecyclerview.setAdapter(mComUpAdapter);
                    } else {
                        int startPosition = taskLists.size() - 1;
                        taskLists.addAll(data);
                        /*mComUpAdapter.notifyItemRangeChanged(startPosition, taskLists.size() - 1);*/
                        mComUpAdapter.notifyDataSetChanged();
                    }
                    mComUpAdapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(mActivity, HistoryTask.class);
                            intent.putExtra(UploadedTaskFragment.COMUPLOADFRAGMENT, taskLists.get(position).getTaskId());
                            intent.putExtra("mark", layoutmark);
                            intent.putExtra("modeltype", "3");
                            intent.putExtra("taskid", taskLists.get(position).getTaskId());
                            intent.putExtra("note", taskLists.get(position).getNote());
                            startActivity(intent);
                        }
                    });
                } else {
                    mCompLoadXrecyclerview.setNoMore(true);
                }
                mCompLoadXrecyclerview.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
    }
}
