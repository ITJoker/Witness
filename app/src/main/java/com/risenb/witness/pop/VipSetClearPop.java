package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class VipSetClearPop extends CommentPopUtils implements View.OnClickListener {
    private TextView vipsetclearpop_qx_tv, vipsetclearpop_ok_tv;
    private Context context;


    public VipSetClearPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        vipsetclearpop_ok_tv = view.findViewById(R.id.vipsetclearpop_ok_tv);
        vipsetclearpop_qx_tv = view.findViewById(R.id.vipsetclearpop_qx_tv);
        vipsetclearpop_ok_tv.setOnClickListener(this);
        vipsetclearpop_qx_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vipsetclearpop_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
