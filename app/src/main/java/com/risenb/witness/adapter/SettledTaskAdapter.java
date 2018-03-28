package com.risenb.witness.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.SettledTaskBean;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.List;

public class SettledTaskAdapter extends BaseAdapter {
    private List<SettledTaskBean.DataBean> settledTaskDataList;
    private Context context;

    public SettledTaskAdapter(Context context, List<SettledTaskBean.DataBean> data) {
        this.settledTaskDataList = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return settledTaskDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return settledTaskDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_settled_task_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(settledTaskDataList.get(position).getImage(), viewHolder.settled_task_image_iv, MyConfig.options);
        // 任务名称
        viewHolder.settled_task_title_tv.setText(settledTaskDataList.get(position).getFixedname());
        // 任务所在省
        if (!TextUtils.isEmpty(settledTaskDataList.get(position).getProvince_name())) {
            viewHolder.settled_task_province_tv.setText(settledTaskDataList.get(position).getProvince_name());
        } else {
            viewHolder.settled_task_province_tv.setText("中国");
        }
        // 任务所在市
        if (!TextUtils.isEmpty(settledTaskDataList.get(position).getCity_name())) {
            viewHolder.settled_task_city_tv.setText(settledTaskDataList.get(position).getCity_name());
        } else {
            viewHolder.settled_task_city_tv.setText("");
        }
        // 任务价格
        viewHolder.settled_task_price_tv.setText("￥" + settledTaskDataList.get(position).getPrice());
        // 任务可执行次数
        viewHolder.settled_task_number_tv.setText("可执行次数：" + settledTaskDataList.get(position).getCount());
        // 任务倒计时进度条
        viewHolder.settled_task_time_pb.setMax(Integer.valueOf(settledTaskDataList.get(position).getValidity()));
        viewHolder.settled_task_time_pb.setProgress(Integer.valueOf(settledTaskDataList.get(position).getRemainTime()));
        String time = TimesUtils.timeDifference(String.valueOf(settledTaskDataList.get(position).getRemainTime() * 1000));
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            viewHolder.settled_task_count_down_timer.setTime(dd, hh, mm, ss);
        } else {
            viewHolder.settled_task_count_down_timer.setTime(0, 0, 0, 0);
        }
        // 任务开始倒计时
        viewHolder.settled_task_count_down_timer.start();
        // 调整底部边距
        if (position == settledTaskDataList.size() - 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.topMargin = (int) context.getResources().getDimension(R.dimen.dm018);
            layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.dm018);
            layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.dm018);
            layoutParams.rightMargin = (int) context.getResources().getDimension(R.dimen.dm018);
            viewHolder.settled_task_ll.setLayoutParams(layoutParams);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView settled_task_title_tv, settled_task_province_tv, settled_task_city_tv, settled_task_price_tv, settled_task_number_tv;
        private ImageView settled_task_image_iv;
        private ProgressBar settled_task_time_pb;
        private CountDownTimerView settled_task_count_down_timer;
        private LinearLayout settled_task_ll;

        public ViewHolder(View convertView) {
            settled_task_ll = convertView.findViewById(R.id.settled_task_ll);
            settled_task_title_tv = convertView.findViewById(R.id.settled_task_title_tv);
            settled_task_province_tv = convertView.findViewById(R.id.settled_task_province_tv);
            settled_task_city_tv = convertView.findViewById(R.id.settled_task_city_tv);
            settled_task_price_tv = convertView.findViewById(R.id.settled_task_price_tv);
            settled_task_number_tv = convertView.findViewById(R.id.settled_task_number_tv);
            settled_task_image_iv = convertView.findViewById(R.id.settled_task_image_iv);
            settled_task_count_down_timer = convertView.findViewById(R.id.settled_task_count_down_timer);
            settled_task_time_pb = convertView.findViewById(R.id.settled_task_time_pb);
        }
    }
}
