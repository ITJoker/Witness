package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.BatchUploadTaskBean;
import com.risenb.witness.ui.tasklist.UploadTask;
import com.risenb.witness.ui.tasklist.interUpload;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadTaskAdapter extends BaseAdapter implements interUpload {
    private Context mContext;
    private List<BatchUploadTaskBean.DataBean> mList;
    private LayoutInflater mInflater;

    public UploadTaskAdapter(Context mContext, List<BatchUploadTaskBean.DataBean> list) {
        this.mContext = mContext;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        ((UploadTask) mContext).setListener(this);
    }

    public void setPosition(int pos) {
        mList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mList && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        BatchUploadTaskBean.DataBean info = mList.get(position);
        if (view == null) {
            view = mInflater.inflate(R.layout.uploadtask_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            holder.uploadRadioButton.setTag(info);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.uploadRadioButton.setTag(info);
        }
        holder.uploadRadioButton.setChecked(info.isCheckBox());
        holder.uploadRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BatchUploadTaskBean.DataBean info = (BatchUploadTaskBean.DataBean) holder.uploadRadioButton.getTag();
                info.setCheckBox(buttonView.isChecked());
            }
        });
        holder.mLayout_RecentlyDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.uploadRadioButton.isChecked()) {
                    holder.uploadRadioButton.setChecked(false);
                    mList.get(position).setCheckBox(false);
                } else {
                    holder.uploadRadioButton.setChecked(true);
                    mList.get(position).setCheckBox(true);
                }
            }
        });

        int times = mList.get(position).getRemainTime();
        String realTime = String.valueOf(times * 1000);
        String time = TimesUtils.timeDifference(realTime);
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            holder.mBatchUpload_Countdowntimer.setTime(dd, hh, mm, ss);
        } else {
            holder.mBatchUpload_Countdowntimer.setTime(0, 0, 0, 0);
        }
        // 开始倒计时
        holder.mBatchUpload_Countdowntimer.start();

        /*holder.mBatchUpload_Distanc.setText(String.valueOf(info.getDistance()));*/
        holder.mBatchUpload_Distanc.setText("ID：".concat(info.getTaskId()));
        /*ImageLoader.getInstance().displayImage(info.getImage(), holder.mBatchUpload_Image, MyConfig.options);*/
        ImageLoader.getInstance().displayImage(info.getImage(), holder.mBatchUpload_Image);
        holder.mBatchUpload_Money.setText(String.valueOf(info.getPrice()));
        holder.mBatchUpload_Title.setText(info.getAddress());
        holder.uploadRadioButton.setChecked(info.isCheckBox());
        return view;
    }

    @Override
    public void uploadTaskVedio(String path) {
        if ("1".equals(path)) {
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        @BindView(R.id.layout_recentlydistance_item)
        LinearLayout mLayout_RecentlyDistance;
        //选择
        @BindView(R.id.uploadRadioButton)
        RadioButton uploadRadioButton;
        //图片
        @BindView(R.id.upload_batch_image)
        ImageView mBatchUpload_Image;
        //标题
        @BindView(R.id.upload_batch_title)
        TextView mBatchUpload_Title;
        //价格
        @BindView(R.id.upload_batch_money)
        TextView mBatchUpload_Money;
        //距离
        @BindView(R.id.upload_batch_distanc)
        TextView mBatchUpload_Distanc;
        //时间
        @BindView(R.id.upload_batch_countdowntimer)
        CountDownTimerView mBatchUpload_Countdowntimer;
        //进度条
        @BindView(R.id.progressBar)
        ProgressBar mBatchUpload_ProgressBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
