package com.risenb.witness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.HomeDistanceFrgBean;
import com.risenb.witness.ui.home.HomeMapsUI;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.ArrayList;
import java.util.List;

public class HomeDistanceAdapter extends BaseAdapter {
    List<HomeDistanceFrgBean> list = new ArrayList<>();
    Context context;

    public HomeDistanceAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<HomeDistanceFrgBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<HomeDistanceFrgBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public HomeDistanceFrgBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_item, null);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.home_item_map_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeMapsUI.class);
                intent.putExtra("type", "3");
                intent.putExtra("Latitude", list.get(position).getLatitude());
                intent.putExtra("Longitude", list.get(position).getLongitude());
                context.startActivity(intent);
            }
        });
        ImageLoader.getInstance().displayImage(list.get(position).getImage(), viewHolder.home_item_logo_iv, MyConfig.options);
        viewHolder.textView.setText(list.get(position).getAddress());
        viewHolder.home_item_price_tv.setText("￥" + list.get(position).getPrice());
        viewHolder.home_item_distance_tv.setText("距离：" + list.get(position).getDistance() + "km");
        viewHolder.home_item_pb.setMax(Integer.valueOf(list.get(position).getValidity()));
        viewHolder.home_item_pb.setProgress(Integer.valueOf(list.get(position).getRemainTime()));
        String time = TimesUtils.timeDifference(String.valueOf(list.get(position).getRemainTime() * 1000));
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            viewHolder.count_down_timer.setTime(dd, hh, mm, ss);
        } else {
            viewHolder.count_down_timer.setTime(0, 0, 0, 0);
        }
        // 开始倒计时
        viewHolder.count_down_timer.start();
        return convertView;
    }

    class MyViewHolder {
        private CountDownTimerView count_down_timer;
        private TextView textView, home_item_price_tv;
        private TextView home_item_distance_tv;
        private ImageView home_item_logo_iv, home_item_map_iv;
        private ProgressBar home_item_pb;

        public MyViewHolder(View convertView) {
            count_down_timer = convertView.findViewById(R.id.count_down_timer);
            textView = convertView.findViewById(R.id.home_title);
            home_item_price_tv = convertView.findViewById(R.id.home_item_price_tv);
            home_item_distance_tv = convertView.findViewById(R.id.home_item_distance_tv);
            home_item_logo_iv = convertView.findViewById(R.id.home_item_logo_iv);
            home_item_map_iv = convertView.findViewById(R.id.home_item_map_iv);
            home_item_pb = convertView.findViewById(R.id.home_item_pb);
        }
    }
}
