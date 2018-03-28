package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class PopListAdapter<T extends String> extends BaseListAdapter<T> {
    @Override
    protected BaseViewHolder<T> loadView(Context context, T bean, int position) {
        return new ViewHolder(context, getViewId(bean, position));
    }

    @Override
    protected int getViewId(T bean, int position) {
        return R.layout.pop_list_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {

        @ViewInject(R.id.tv_pop_list_item)
        private TextView tv_pop_list_item;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
        }

        @Override
        protected void prepareData() {
            tv_pop_list_item.setText(bean);
        }
    }
}
