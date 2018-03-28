package com.risenb.witness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.HomeMapsMakerBeans;
import com.risenb.witness.ui.home.HomeTaskInfoUI;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class HomeMapsMakerAdapter<T extends HomeMapsMakerBeans> extends BaseListAdapter<T> {
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    protected BaseViewHolder<T> loadView(Context context, T bean, int position) {
        return new ViewHolder(context, getViewId(bean, position));
    }

    @Override
    protected int getViewId(T bean, int position) {
        return R.layout.homemapsmaker_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {

        @ViewInject(R.id.homeMapsMaker_LL)
        private LinearLayout homeMapsMaker_LL;
        @ViewInject(R.id.homemapsmaker_name_tv)
        private TextView homemapsmaker_name_tv;
        @ViewInject(R.id.homemapsmaker_price_tv)
        private TextView homemapsmaker_price_tv;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
        }

        @Override
        protected void prepareData() {
            homemapsmaker_name_tv.setText(bean.getAddress());
            homemapsmaker_price_tv.setText(bean.getPrice() + "积分");
            homeMapsMaker_LL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeTaskInfoUI.class);
                    intent.putExtra("taskId", bean.getTaskId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
