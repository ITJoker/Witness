package com.risenb.witness.views.newViews;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.risenb.witness.utils.newUtils.MUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> list;
    protected T[] tArr;
    protected OnItemClickListener onItemClickListener;
    private FragmentActivity activity;

    public BaseListAdapter() {

    }

    public void clearList() {
        if (list != null && list.size() > 0) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void setList(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addBean(T bean) {
        if (this.list == null) {
            this.list = new ArrayList();
        }

        this.list.add(bean);
        this.notifyDataSetChanged();
    }

    public ArrayList<T> getList() {
        return (ArrayList) this.list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FragmentActivity getActivity() {
        return this.activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public int getCount() {
        return this.list != null ? this.list.size() : (this.tArr != null ? this.tArr.length : 0);
    }

    public T getItem(int position) {
        return this.list != null && position < this.list.size() ? this.list.get(position) : (this.tArr != null && position < this.tArr.length ? this.tArr[position] : null);
    }

    public T getItem(long position) {
        return this.getItem((int) position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        T bean = this.getItem(position);
        BaseViewHolder baseView;
        if (convertView == null) {
            baseView = this.loadView(parent.getContext(), bean, position);
        } else {
            baseView = (BaseViewHolder) convertView.getTag(this.getViewId(bean, position));
            if (baseView == null) {
                baseView = this.loadView(parent.getContext(), bean, position);
            }
        }

        baseView.prepareData(bean, position);
        if (this.onItemClickListener != null) {
            baseView.getView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BaseListAdapter.this.onItemClickListener.onItemClick((AdapterView) null, v, position, (long) position);
                }
            });
        }

        return baseView.getView();
    }

    protected abstract BaseViewHolder<T> loadView(Context var1, T var2, int var3);

    protected abstract int getViewId(T var1, int var2);

    protected void makeText(final String content) {
        MUtils.getMUtils().getHandler().post(new Runnable() {
            public void run() {
                Toast.makeText(BaseListAdapter.this.getActivity(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
