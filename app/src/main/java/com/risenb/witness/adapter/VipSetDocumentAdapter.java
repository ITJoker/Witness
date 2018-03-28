package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.VipSetDocumentBeans;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class VipSetDocumentAdapter<T extends VipSetDocumentBeans> extends BaseListAdapter<T> {
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
        return R.layout.vipsetdocument_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {
        @ViewInject(R.id.vipsetdocument_item_tv)
        private TextView vipsetdocument_item_tv;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
        }

        @Override
        protected void prepareData() {
            vipsetdocument_item_tv.setText(bean.getTypeName());
        }
    }
}
