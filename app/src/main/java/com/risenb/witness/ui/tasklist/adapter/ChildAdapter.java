package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.R;
import com.risenb.witness.ui.tasklist.CheckBoxInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<CheckBoxInfo> mList;

    public ChildAdapter(Context context, List<CheckBoxInfo> boxInfo) {
        this.mList = boxInfo;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        CheckBoxInfo info = mList.get(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_content, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            viewHolder.itemCheckboxButton.setTag(info);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.itemCheckboxButton.setTag(info);
        }

        viewHolder.itemCheckboxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBoxInfo info = (CheckBoxInfo) viewHolder.itemCheckboxButton.getTag();
                info.setSelected(buttonView.isChecked());
            }
        });

        viewHolder.itemCheckboxText.setText(info.getTextContent());
        viewHolder.itemCheckboxButton.setChecked(info.isSelected());
        return convertView;
    }


    public static class ViewHolder {
        @BindView(R.id.item_checkbox_text)
        TextView itemCheckboxText;
        @BindView(R.id.item_checkbox_button)
        CheckBox itemCheckboxButton;
        @BindView(R.id.layout_backgroud_checkbox)
        RelativeLayout layoutBackgroudCheckbox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
