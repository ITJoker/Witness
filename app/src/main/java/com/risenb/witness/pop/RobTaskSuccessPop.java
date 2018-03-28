package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class RobTaskSuccessPop extends CommentPopUtils implements View.OnClickListener {

    private TextView robtasksuccess_ok_tv, robtasksuccess_qx_tv ;
    private Context context;

    public RobTaskSuccessPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        robtasksuccess_ok_tv = view.findViewById(R.id.robtasksuccess_ok_tv);
        robtasksuccess_qx_tv = view.findViewById(R.id.robtasksuccess_qx_tv);

        robtasksuccess_ok_tv.setOnClickListener(this);
        robtasksuccess_qx_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (view.getId() == R.id.robtasksuccess_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
