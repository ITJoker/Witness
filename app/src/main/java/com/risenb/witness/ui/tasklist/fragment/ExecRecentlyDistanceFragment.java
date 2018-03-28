package com.risenb.witness.ui.tasklist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.risenb.witness.ui.tasklist.OnRecyclerItemClickListener;
import com.risenb.witness.ui.tasklist.UncertainExecEvidence;
import com.risenb.witness.ui.tasklist.UploadTask;
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
import butterknife.Unbinder;

public class ExecRecentlyDistanceFragment extends BaseLazyFragment {
    private Activity mActivity;
    private RecentlyDistanceAdapter mAdapter;
    @BindView(R.id.non_recentdistance_xrecyclerview)
    XRecyclerView mNrdXRecyclerView;
    private String layoutmark;
    private View view;
    private int pagecount = 1;

    public static List<TaskListBean.TaskList> searchAddressList = new ArrayList<>();
    public static boolean distanceClickSign = false;
    public static String distanceQueryText = "";
    public static boolean isNetRefresh = false;

    public static String distanceCityId = "";
    public static String distanceMediaId = "";
    public static String distanceCompanyId = "";

    private boolean isSearchRefresh = false;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    public static ExecRecentlyDistanceFragment newInstance(String mark) {
        ExecRecentlyDistanceFragment lf = new ExecRecentlyDistanceFragment();
        final Bundle args = new Bundle();
        args.putString("mark", mark);
        lf.setArguments(args);
        return lf;
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
            if (isSearchRefresh) {
                isSearchRefresh = false;
                return;
            }
            pagecount = 1;
            // 未执行/执行Fragment中搜索到数据后重新将List传递给Adapter并设置给RecyclerView
            mAdapter = new RecentlyDistanceAdapter(mActivity, searchAddressList, layoutmark);
            mNrdXRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mNrdXRecyclerView.refreshComplete();
            // 可以继续上拉加载数据
            mNrdXRecyclerView.setNoMore(false);
        } else {
            if (isNetRefresh) {
                searchAddressList.clear();
                pagecount = 1;
                onNetWorkData();
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                    mNrdXRecyclerView.refreshComplete();
                }
                isNetRefresh = false;
            } else {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void initData() {
        onNetWorkData();
    }

    @Override
    protected View initView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.recentdistancefragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        layoutmark = getArguments() != null ? getArguments().getString("mark") : null;
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNrdXRecyclerView.setLayoutManager(layoutManager);
        mNrdXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mNrdXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mNrdXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mNrdXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (null != searchAddressList && searchAddressList.size() > 0) {
                    /*searchAddressList.clear();*/
                    SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
                }
                pagecount = 1;
                onNetWorkData();
                mNrdXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                pagecount++;
                onNetWorkData();
            }
        });

        mNrdXRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mNrdXRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                /*
                 * 记录点击位置，返回后根据任务状态是否将该Item从List中移除，之后根据位置进行局部刷新
                 */
                if (viewHolder.itemView.getTag() != null) {
                    isSearchRefresh = true;
                    int position = (int) viewHolder.itemView.getTag();
                    Intent intent = new Intent(mActivity, UncertainExecEvidence.class);
                    if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                        intent.putExtra(NonExecutionTaskFragment.NONEXECUTIONFRAGMENTTASKID, searchAddressList.get(position).getTaskId());
                    } else if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                        intent.putExtra(ExecutingTaskFragment.EXECUTIONINGFRAGMENT, searchAddressList.get(position).getTaskId());
                    }
                /*int adapterPosition = viewHolder.getAdapterPosition() - 1;*/
                    intent.putExtra("taskid", searchAddressList.get(position).getTaskId());
                    intent.putExtra("modeltype", "3");
                    intent.putExtra("currentpage", 1);
                    intent.putExtra("mark", layoutmark);

                    intent.putExtra("execution", searchAddressList.get(position).getExecution());
                    intent.putExtra("address", searchAddressList.get(position).getCity().concat(searchAddressList.get(position).getAddress()));
                    if (!TextUtils.isEmpty(searchAddressList.get(position).getNote())) {
                        intent.putExtra("note", searchAddressList.get(position).getNote());
                    }
                    intent.putExtra("isType", searchAddressList.get(position).getShootTpye());
                    intent.putExtra("price", searchAddressList.get(position).getPrice());
                    intent.putStringArrayListExtra("require", searchAddressList.get(position).getRequire());
                    /*intent.putExtra("taskInfo", searchAddressList.get(position));*/

                    intent.putExtra("waterMarkID", searchAddressList.get(position).getWatermark_taskid());
                    intent.putExtra("waterMarkNT", searchAddressList.get(position).getWatermark_meshtime());
                    intent.putExtra("waterMarkXY", searchAddressList.get(position).getWatermark_xydata());
                    intent.putExtra("waterMarkST", searchAddressList.get(position).getWatermark_systemtime());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                    Intent intent = new Intent(mActivity, UploadTask.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    public void onNetWorkData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskList));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(mActivity, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }

        if (isVisible) {
            Utils.getUtils().showProgressDialog(mActivity);
        }

        Map<String, String> param = new HashMap<>();
        param.put("c", mApplication.getC());
        param.put("pagecount", String.valueOf(pagecount));
        param.put("pagesize", "10");
        param.put("sort", "2");
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        if (layoutmark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            // 未执行
            param.put("type", "0");
            if (SharedPreferencesUtil.getBoolean(getContext(), "NonExecution", false)) {
                // 说明进行过搜索
                param.put("query", distanceQueryText);
            }
        } else if (layoutmark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            // 执行中
            param.put("type", "1");
            if (SharedPreferencesUtil.getBoolean(getContext(), "Executioning", false)) {
                param.put("query", distanceQueryText);
                param.put("CityId", distanceCityId);
                param.put("MediaTreeID", distanceMediaId);
                param.put("CompanyId", distanceCompanyId);
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
                        searchAddressList = data;
                        mAdapter = new RecentlyDistanceAdapter(mActivity, searchAddressList, layoutmark);
                        mNrdXRecyclerView.setAdapter(mAdapter);
                    } else {
                        int startPosition = searchAddressList.size();
                        searchAddressList.addAll(data);
                        /*mAdapter.notifyItemRangeInserted(startPosition, searchAddressList.size() - 1);*/
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mNrdXRecyclerView.setNoMore(true);
                }
                mNrdXRecyclerView.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void setData(List<TaskListBean.TaskList> homeDistanceFrgBeen) {
        if (mAdapter != null) {
            mAdapter.setList(homeDistanceFrgBeen);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        // 下次进入Fragment时跳过搜索判断
        SharedPreferencesUtil.saveBoolean(getContext(), layoutmark, false);
    }
}
