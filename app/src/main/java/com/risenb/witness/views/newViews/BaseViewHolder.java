package com.risenb.witness.views.newViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lidroid.xutils.ViewUtils;

public abstract class BaseViewHolder<T> {
    protected View convertView;
    protected T bean;
    protected int position;
    protected int groupPosition;
    protected int childPosition;
    protected Context context;

    protected BaseViewHolder(Context context, int layoutID) {
        this.context = context;
        this.convertView = LayoutInflater.from(context).inflate(layoutID, null);
        ViewUtils.inject(this, this.convertView);
        this.convertView.setTag(layoutID, this);
        this.convertView.setTag(this);
    }

    protected abstract void prepareData();

    protected <E extends View> E findViewById(int viewId) {
        return (E) this.convertView.findViewById(viewId);
    }

    public final View getView() {
        return this.convertView;
    }

    public void prepareData(T bean, int position) {
        this.bean = bean;
        this.position = position;
        this.prepareData();
    }

    public void prepareData(T bean, int groupPosition, int childPosition) {
        this.bean = bean;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.prepareData();
    }
}
