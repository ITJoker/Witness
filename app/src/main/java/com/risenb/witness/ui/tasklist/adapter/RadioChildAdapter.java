package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.beans.RadioBean;
import com.risenb.witness.beans.RadioButtonInfo;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RadioChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<RadioButtonInfo> list;
    private LayoutInflater mLayoutInflater;
    private int index = -1;
    private String selectstr = null;
    private RadioBean radioBean;
    private String TestRadioId;
    private boolean selected = true;
    private HashMap<String, Boolean> states = new HashMap<String, Boolean>();

    public RadioChildAdapter(Context context, List<RadioButtonInfo> list, String id) {

        this.mContext = context;
        this.list = list;
        this.TestRadioId = id;
        radioBean = new RadioBean();
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    public void setSelectStr(String str) {
        this.selectstr = str;
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
//        ViewHolderRadioTest holderRadio = null;
//        if (null == convertView) {
//            convertView = mLayoutInflater.inflate(R.layout.lasttestanwer, parent, false);
//           // holderRadio = new ViewHolderRadioTest(convertView);
//            convertView.setTag(holderRadio);
//        } else {
//            holderRadio = (ViewHolderRadioTest) convertView.getTag();
//        }
//
//
//        holderRadio.mTestAnwerText.setText(list.get(position).getRaidotextContent());
//        if (selected ==true&&null != selectstr && list.get(position).equals(selectstr) ) {
//            index = position;
//        }
//
//        holderRadio.mTestRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    index = position;
//                    notifyDataSetChanged();
//                }
//            }
//        });
//        RadioButtonInfo  info=list.get(position);
//        if (index == position) {// 选中的条目和当前的条目是否相等
//            info.setSelected(true);
//            holderRadio.mTestRadioBtn.setChecked(true);
//            holderRadio.mTestRootLayout.setBackgroundResource(R.drawable.task_situation_select);
//            radioBean.setStr(list.get(position).getRaidotextContent());
//            radioBean.setId(TestRadioId);
//            setSelectstr(list.get(position).getRaidotextContent(), false);
//            selected=false;
//        } else {
//            holderRadio.mTestRadioBtn.setChecked(false);
//            holderRadio.mTestRootLayout.setBackgroundResource(R.drawable.task_situation_normal);
//        }
        final RadioButtonInfo info = list.get(position);
        convertView = mLayoutInflater.inflate(R.layout.lasttestanwer, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.test_anwer_text);
        final RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.test_radioBtn);
        LinearLayout mrootlayout = (LinearLayout) convertView.findViewById(R.id.test_layout_select_radiobutton);
        textView.setText(list.get(position).getRaidotextContent());
        if (null != selectstr && list.get(position).equals(selectstr)) {
            // index = position;
            radioButton.setChecked(true);
        }

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    index = position;
                    for (int i = 0; i < getCount(); i++) {
                        list.get(i).setSelected(false);
                        //   radioBean.setSelected(false);
                    }
                    info.setSelected(true);
//                    radioBean.setStr(list.get(position).getRaidotextContent());
//                    radioBean.setSelected(true);
//                    radioBean.setId(TestRadioId);
                    notifyDataSetChanged();
                }
            }
        });


        radioButton.setChecked(info.isSelected());


//        if (index == position) {// 选中的条目和当前的条目是否相等
//
//
//            mrootlayout.setBackgroundResource(R.drawable.task_situation_select);
//            radioBean.setStr(list.get(position));
//            radioBean.setId(TestRadioId);
//            setSelectstr(list.get(position), false);
//            selected=false;
//        } else {
//            radioButton.setChecked(false);
//            mrootlayout.setBackgroundResource(R.drawable.task_situation_normal);
//        }


        return convertView;
    }


    public String getSelectstr() {
        return selectstr;
    }

    public void setSelectstr(String selectstr, boolean flag) {
        this.selectstr = selectstr;
    }


    static class ViewHolderRadioTest {
        //单选文本
        @BindView(R.id.test_anwer_text)
        TextView mTestAnwerText;
        //单选按钮
        @BindView(R.id.test_radioBtn)
        RadioButton mTestRadioBtn;
        //单选布局
        @BindView(R.id.test_layout_select_radiobutton)
        LinearLayout mTestRootLayout;

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
