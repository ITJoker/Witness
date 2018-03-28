package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class RobTaskLoginPop extends CommentPopUtils implements View.OnClickListener {
    private TextView robtaskloginpop_qx_tv, robtaskloginpop_ok_tv;

    private Context context;

    public RobTaskLoginPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        robtaskloginpop_ok_tv = (TextView) view.findViewById(R.id.robtaskloginpop_ok_tv);
        robtaskloginpop_qx_tv = (TextView) view.findViewById(R.id.robtaskloginpop_qx_tv);

        robtaskloginpop_ok_tv.setOnClickListener(this);
        robtaskloginpop_qx_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (view.getId() == R.id.robtaskloginpop_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
