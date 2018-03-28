package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class JournalsStateAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mlist;

    public JournalsStateAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size()>0 ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }

    public class viewHolder {
        public TextView nameTxt;
        public RadioButton selectBtn;
    }
}
