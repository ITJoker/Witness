package com.risenb.witness.ui.tasklist.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.TaskListBean;
import com.risenb.witness.ui.tasklist.RejectTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.utils.MapUtils;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentlyDistanceAdapter extends Adapter<RecentlyDistanceAdapter.ViewHolder> implements OnClickListener {
    private Activity mActivity;
    private List<TaskListBean.TaskList> mlist;
    private String mFragemntMark;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private String id;
    private boolean isGuideClick;

    public RecentlyDistanceAdapter(Activity mActivity, List<TaskListBean.TaskList> list, String layoutMark) {
        this.mActivity = mActivity;
        this.mFragemntMark = layoutMark;
        this.mlist = list;
    }

    public void setList(List<TaskListBean.TaskList> list) {
        this.mlist.clear();
        this.mlist.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentdistancexrecyerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mFragemntMark != null && mFragemntMark.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            holder.mDistanc_Exe_New.setVisibility(View.VISIBLE);
        }
        if (mFragemntMark != null && mFragemntMark.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            holder.mDistanc_Exe_Continue.setVisibility(View.VISIBLE);
        }
        if (mFragemntMark != null && mFragemntMark.equals(RejectTaskFragment.REJECTASKFRAGEMNTFLAG)) {
            holder.mDistanc_Exe_New.setVisibility(View.GONE);
            holder.mDistanc_Exe_Complete.setVisibility(View.GONE);
            holder.recent_distance_guide_iv.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(mlist.get(position).getImage(), holder.mRecentdistancImage);
        /*holder.mRecentdistancDistanc.setText("距离:".concat(mlist.get(position).getDistance()).concat("km   ID：").concat(mlist.get(position).getTaskId()).concat("   结束时间：".concat(mlist.get(position).getEndtime())));*/
        holder.mRecentdistancAddress.setText(mlist.get(position).getCity().concat(mlist.get(position).getAddress()));
        if (!TextUtils.isEmpty(mlist.get(position).getNote())) {
            holder.recentdistanc_mark.setText("备注：".concat(mlist.get(position).getNote()));
        }
        if ("3".equals(mlist.get(position).getExecution())) {
            //执行方式(1.专业人员、2.客户检测员、3.奖池)
            holder.mRecentdistancDistanc.setText("ID：".concat(mlist.get(position).getTaskId()));
            holder.mRecentdistancMoney_ll.setVisibility(View.VISIBLE);
            holder.mDistancCountDownTimer.setVisibility(View.VISIBLE);
            holder.lose_effect_countdown_tv.setText("失效倒计时：");
            holder.mRecentdistancMoney.setText("￥".concat(mlist.get(position).getPrice()));
            holder.distanc_count_down_timer_pb.setMax(Integer.valueOf(mlist.get(position).getValidity()));
            holder.distanc_count_down_timer_pb.setProgress(mlist.get(position).getRemainTime());
            /*holder.text_execution_info_ll.measure(0, 0);
            ViewGroup.LayoutParams layoutParams = holder.text_execution_info_ll.getLayoutParams();
            layoutParams.height = holder.text_execution_info_ll.getMeasuredHeight();
            holder.text_execution_info_ll.setLayoutParams(layoutParams);*/
        } else {
            holder.mRecentdistancMoney_ll.setVisibility(View.GONE);
            holder.mDistancCountDownTimer.setVisibility(View.GONE);
            holder.mRecentdistancDistanc.setText("ID：".concat(mlist.get(position).getTaskId()));
            holder.lose_effect_countdown_tv.setText("结束时间：".concat(mlist.get(position).getEndtime()));
        }

        int times = mlist.get(position).getRemainTime();
        String realTime = String.valueOf(times * 1000);
        String time = TimesUtils.timeDifference(realTime);
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            holder.mDistancCountDownTimer.setTime(dd, hh, mm, ss);
        } else {
            holder.mDistancCountDownTimer.setTime(0, 0, 0, 0);
        }
        // 开始倒计时
        holder.mDistancCountDownTimer.start();
        holder.mLayout_RecentlyDistance.setOnClickListener(this);
        // holder.itemView.setTag();
        id = mlist.get(position).getTaskId();
        holder.itemView.setTag(position);
        // 将数据保存在itemView的Tag中，以便点击时进行获取
        // viewHolder.itemView.setTag(datas[position]);

        /*
         * 导航
         */
        holder.recent_distance_guide_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MapUtils.mapGuide(mActivity, mlist.get(position).getLongitude(), mlist.get(position).getLatitude(), mlist.get(position).getAddress());
                isGuideClick = true;
            }
        });

        /*// 调整底部边距
        if (position == mlist.size() - 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Application application = mActivity.getApplication();
            layoutParams.bottomMargin = (int) application.getResources().getDimension(R.dimen.dm020);
            layoutParams.leftMargin = (int) application.getResources().getDimension(R.dimen.dm020);
            layoutParams.rightMargin = (int) application.getResources().getDimension(R.dimen.dm020);
            holder.recently_distance_ll.setLayoutParams(layoutParams);
        }*/
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (int) v.getTag(), isGuideClick);
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_recentlydistance_item)
        LinearLayout mLayout_RecentlyDistance;
        @BindView(R.id.recently_distance_ll)
        LinearLayout recently_distance_ll;
        @BindView(R.id.text_execution_info_ll)
        LinearLayout text_execution_info_ll;
        //图片
        @BindView(R.id.recentdistanc_image)
        ImageView mRecentdistancImage;
        //地址
        @BindView(R.id.recentdistanc_title)
        TextView mRecentdistancAddress;
        @BindView(R.id.recentdistanc_mark)
        TextView recentdistanc_mark;
        //价格
        @BindView(R.id.recentdistanc_money_ll)
        LinearLayout mRecentdistancMoney_ll;
        @BindView(R.id.recentdistanc_money)
        TextView mRecentdistancMoney;
        //距离
        @BindView(R.id.recentdistanc_distanc)
        TextView mRecentdistancDistanc;

        @BindView(R.id.lose_effect_countdown_tv)
        TextView lose_effect_countdown_tv;
        //计时器
        @BindView(R.id.distanc_count_down_timer)
        CountDownTimerView mDistancCountDownTimer;

        @BindView(R.id.recentdis_executioning_complete)
        ImageView mDistanc_Exe_Complete;

        @BindView(R.id.recentdis_executioning_new)
        ImageView mDistanc_Exe_New;

        @BindView(R.id.recentdis_executioning_continue)
        ImageView mDistanc_Exe_Continue;

        @BindView(R.id.distanc_count_down_timer_pb)
        ProgressBar distanc_count_down_timer_pb;

        @BindView(R.id.recent_distance_guide_iv)
        ImageView recent_distance_guide_iv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, boolean isGuideClick);
    }

    public OnRecyclerViewItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public String getData() {
        return id;
    }
}
