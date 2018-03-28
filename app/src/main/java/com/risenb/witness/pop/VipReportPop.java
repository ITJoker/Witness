package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.risenb.witness.R;

public class VipReportPop extends CommentPopUtils implements View.OnClickListener {
    private TextView vipreportpop_qx_tv;
    private Context context;
    private LinearLayout fenxiang_qq_ll ,fenxiang_wx_ll ,fenxiang_pyq_ll ,fenxiang_wb_ll ;

    public VipReportPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        fenxiang_qq_ll = view.findViewById(R.id.fenxiang_qq_ll);
        fenxiang_wx_ll = view.findViewById(R.id.fenxiang_wx_ll);
        fenxiang_pyq_ll = view.findViewById(R.id.fenxiang_pyq_ll);
        fenxiang_wb_ll = view.findViewById(R.id.fenxiang_wb_ll);
        vipreportpop_qx_tv = view.findViewById(R.id.vipreportpop_qx_tv);
        vipreportpop_qx_tv.setOnClickListener(this);
        fenxiang_qq_ll.setOnClickListener(this);
        fenxiang_wx_ll.setOnClickListener(this);
        fenxiang_pyq_ll.setOnClickListener(this);
        fenxiang_wb_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            dismiss();
        }
        if (view.getId() == R.id.vipreportpop_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
}
