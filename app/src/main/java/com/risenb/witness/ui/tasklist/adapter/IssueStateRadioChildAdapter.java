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
import com.risenb.witness.beans.RadioBean;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueStateRadioChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> list;
    private LayoutInflater mLayoutInflater;
    private int index = -1;
    private String selectstr = null;
    private RadioBean radioBean;
    private String TestRadioId;
    private boolean selected=true;
    private HashMap<String, Boolean> states = new HashMap<String, Boolean>();

    public IssueStateRadioChildAdapter(Context context, List<String> list, String id) {
        this.mContext = context;
        this.list = list;
        this.TestRadioId=id;
        radioBean=new RadioBean();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setSelectStr(String str){
        this.selectstr=str;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderRadioTest holderRadio = null;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.lasttestanwer, parent, false);
            holderRadio = new ViewHolderRadioTest(convertView);
            convertView.setTag(holderRadio);
        } else {
            holderRadio = (ViewHolderRadioTest) convertView.getTag();
        }


        holderRadio.anwerIssueItem.setText(list.get(position));

        if (selected ==true&&null != selectstr && list.get(position).equals(selectstr) ) {
            index = position;
        }


        holderRadio.textIssueRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

            holderRadio.textIssueRadio.setChecked(true);
            holderRadio.LayoutIssueBackgroud.setBackgroundResource(R.drawable.task_situation_select);
            radioBean.setStr(list.get(position));
            radioBean.setId(TestRadioId);
            setSelectstr(list.get(position), false);
            selected=false;
        } else {
            holderRadio.textIssueRadio.setChecked(false);
            holderRadio.LayoutIssueBackgroud.setBackgroundResource(R.drawable.task_situation_normal);
        }
        return convertView;
    }


    public String getSelectstr() {
        return selectstr;
    }

    public void setSelectstr(String selectstr, boolean flag) {
        this.selectstr = selectstr;
    }


    static class ViewHolderRadioTest {
        @BindView(R.id.anwer_issue_item)
        TextView anwerIssueItem;
        @BindView(R.id.text_issue_radio)
        RadioButton textIssueRadio;
        @BindView(R.id.Layout_issue_backgroud)
        RelativeLayout LayoutIssueBackgroud;

        ViewHolderRadioTest(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public RadioBean getRadioBean() {
        return radioBean;
    }

    public void setRadioBean(RadioBean radioBean) {
        this.radioBean = radioBean;
    }
}
