package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.HomeScreenCityBean;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class HomeScreenCitysAdapter<T extends HomeScreenCityBean> extends BaseListAdapter<T> {
    private boolean flag = false;

    private int itme = -1;
    private int flags = 0;
    private int flagss = 0;
    private int flagsss = 0;

    public void CityFlags(boolean flag) {
        this.flag = flag;
        notifyDataSetChanged();
    }

    public void setInt(int itme) {
        if (itme == 0 && flagss == 0) {
            flagss = 1;
            this.itme = itme;
            flagsss = 1 ;
        } else {
            if (this.itme == itme) {
                if (flagsss == 1){
                    flags = 0;
                    flagsss = 2 ;
                }
                if (flags == 1 && flagsss != 1) {
                    flags = 0;
                } else {
                    flags = 1;
                }
            } else {
                this.itme = itme;
                flags = 0;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (flag) {
            if (list != null && list.size() > 9) {
                return 8;
            } else if (list != null && list.size() < 8) {
                return list.size();
            }
        } else if (!flag && list != null && list.size() > 3) {
            return 3;
        } else if (!flag && list != null && list.size() <= 3) {
            return list.size();
        }
        return list == null ? 0 : list.size();
    }

    @Override
    protected BaseViewHolder<T> loadView(Context context, T bean, int position) {
        return new ViewHolder(context, getViewId(bean, position));
    }

    @Override
    protected int getViewId(T bean, int position) {
        return R.layout.homescreengrid_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {
        @ViewInject(R.id.homescreengrid_tv)
        private TextView homescreengrid_tv;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
        }

        @Override
        protected void prepareData() {
            homescreengrid_tv.setText(bean.getCityname());
            if (position == itme) {
                if (flags == 0) {
                    homescreengrid_tv.setBackgroundResource(R.drawable.circular_bead);
                } else {
                    homescreengrid_tv.setBackgroundResource(R.drawable.circular_time_background);
                }
            } else {
                homescreengrid_tv.setBackgroundResource(R.drawable.circular_time_background);
            }
        }
    }
}
