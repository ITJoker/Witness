package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class RobTaskTrainPop extends CommentPopUtils implements View.OnClickListener {

    private TextView robtasktrainpop_qx_tv, robtasktrainpop_ok_tv;

    private Context context;


    public RobTaskTrainPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        robtasktrainpop_ok_tv = (TextView) view.findViewById(R.id.robtasktrainpop_ok_tv);
        robtasktrainpop_qx_tv = (TextView) view.findViewById(R.id.robtasktrainpop_qx_tv);

        robtasktrainpop_ok_tv.setOnClickListener(this);
        robtasktrainpop_qx_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (view.getId() == R.id.robtasktrainpop_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
