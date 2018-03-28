package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class UserOutPop extends CommentPopUtils implements View.OnClickListener {

    private TextView useroutpop_qx_tv, useroutpop_ok_tv ;

    private Context context;

    public UserOutPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }


    @Override
    public void initLayout(View view, Context context) {
        useroutpop_ok_tv = (TextView) view.findViewById(R.id.useroutpop_ok_tv);
        useroutpop_qx_tv = (TextView) view.findViewById(R.id.useroutpop_qx_tv);
        useroutpop_ok_tv.setOnClickListener(this);
        useroutpop_qx_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
