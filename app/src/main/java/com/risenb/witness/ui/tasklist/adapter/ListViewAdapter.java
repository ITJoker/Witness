package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<String> mlist;
    private int index = -1;
    private String selectstr=null;
    public ListViewAdapter(Context context, List<String> list){

        this.mContext=context;
        this.mlist=list;
        inflater = LayoutInflater.from(context);
    }

    public  void setselectstr(String str){
        this.selectstr=str;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    public void setSelectItem(int selectItem) {
        this.index = selectItem;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.item_listview, null,false);
            holder.selectBtn = (RadioButton) convertView.findViewById(R.id.text_radio);
            holder.nameTxt = (TextView) convertView.findViewById(R.id.anwer_text_item);
            holder.rootlayout= (RelativeLayout) convertView.findViewById(R.id.Layout_backgroud);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.nameTxt.setText(mlist.get(position));

        if(null!=selectstr&&mlist.get(position)==selectstr){
            index = position;
        }
        holder.selectBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                          index = position;
                       notifyDataSetChanged();
                        }
                    }
                });

        if (index == position) {// 选中的条目和当前的条目是否相等

            holder.selectBtn.setChecked(true);
            holder.rootlayout.setBackgroundResource(R.drawable.task_situation_select);
            setSelectstr( holder.nameTxt.getText().toString());

        } else {
            holder.selectBtn.setChecked(false);
            holder.rootlayout.setBackgroundResource(R.drawable.task_situation_normal);
        }
        return convertView;
    }
    public class viewHolder {
        public RelativeLayout rootlayout;
        public TextView nameTxt;
        public RadioButton selectBtn;
    }

    public String getSelectstr() {
        return selectstr;
    }

    public void setSelectstr(String selectstr) {
        this.selectstr = selectstr;
    }
}
