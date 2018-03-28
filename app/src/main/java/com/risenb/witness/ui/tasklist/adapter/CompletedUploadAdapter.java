package com.risenb.witness.ui.tasklist.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.TaskListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedUploadAdapter extends RecyclerView.Adapter<CompletedUploadAdapter.ViewHolder> implements View.OnClickListener {
    private List<TaskListBean.TaskList> list;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public CompletedUploadAdapter(Activity activity, List<TaskListBean.TaskList> list, String flag) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completeduploadadapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.completedUploadImage, MyConfig.options);*/
        ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.completedUploadImage);
        if ("3".equals(list.get(position).getState()) || "2".equals(list.get(position).getFixedTaskState())) {
            // 已完成
            holder.completeduploadAuditing.setVisibility(View.GONE);
            holder.completedupload_Audited.setVisibility(View.VISIBLE);
        } else if ("2".equals(list.get(position).getState()) || "1".equals(list.get(position).getFixedTaskState())) {
            // 审核中
            holder.completeduploadAuditing.setVisibility(View.VISIBLE);
            holder.completedupload_Audited.setVisibility(View.GONE);
        }
        holder.uploadtime.setText(list.get(position).getUploadtime());
        if (!TextUtils.isEmpty(list.get(position).getTitle())) {
            holder.completedUploadTitle.setText(list.get(position).getTitle());
        } else {
            holder.completedUploadTitle.setText(list.get(position).getCity().concat(list.get(position).getAddress()));
        }
        if (!TextUtils.isEmpty(list.get(position).getNote())) {
            holder.completedUploadMark.setText("备注：".concat(list.get(position).getNote()));
        } else {
            holder.completedUploadMark.setText("备注：无");
        }
        if ("3".equals(list.get(position).getExecution())) {
            holder.completed_upload_money_ll.setVisibility(View.VISIBLE);
            holder.completedUploadMoney.setText("￥".concat(list.get(position).getPrice()));
        } else {
            holder.completed_upload_money_ll.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(list.get(position).getTaskId()) && !TextUtils.isEmpty(list.get(position).getEndtime())) {
            holder.completedUploadTaskID.setText("ID：".concat(list.get(position).getTaskId()));
            holder.completed_upload_task_lose_effect_time_tv.setText("结束时间：".concat(list.get(position).getEndtime()));
        }
        holder.mLayoutCompletedUploadItem.setOnClickListener(this);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_completedupload_item)
        LinearLayout mLayoutCompletedUploadItem;
        //审核中标示
        @BindView(R.id.completedupload_Auditing)
        TextView completeduploadAuditing;
        //已完成标识
        @BindView(R.id.completedupload_Audited)
        TextView completedupload_Audited;
        //图片
        @BindView(R.id.completed_upload_image)
        ImageView completedUploadImage;
        //标题
        @BindView(R.id.completed_upload_title)
        TextView completedUploadTitle;
        //备注
        @BindView(R.id.completed_upload_mark)
        TextView completedUploadMark;
        //积分
        @BindView(R.id.completed_upload_money_ll)
        LinearLayout completed_upload_money_ll;
        @BindView(R.id.completed_upload_money)
        TextView completedUploadMoney;
        //上传时间
        @BindView(R.id.uploadtime)
        TextView uploadtime;
        //任务ID
        @BindView(R.id.completed_upload_task_id)
        TextView completedUploadTaskID;
        @BindView(R.id.completed_upload_task_lose_effect_time_tv)
        TextView completed_upload_task_lose_effect_time_tv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnRecyclerViewItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
