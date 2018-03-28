package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class RealNamePop extends CommentPopUtils implements View.OnClickListener {

    private TextView realname_qx_tv, realname_ok_tv;
    private TextView realnamepop_name_tv, realnamepop_code_tv;

    private Context context;


    public RealNamePop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    public void setData(String name, String code) {
        realnamepop_name_tv.setText(name);
        realnamepop_code_tv.setText(code);
    }

    @Override
    public void initLayout(View view, Context context) {

        realname_qx_tv = (TextView) view.findViewById(R.id.realname_qx_tv);
        realname_ok_tv = (TextView) view.findViewById(R.id.realname_ok_tv);

        realnamepop_name_tv = (TextView) view.findViewById(R.id.realnamepop_name_tv);
        realnamepop_code_tv = (TextView) view.findViewById(R.id.realnamepop_code_tv);

        realname_qx_tv.setOnClickListener(this);
        realname_ok_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (view.getId() == R.id.realname_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
