package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.risenb.witness.R;

public class RobTaskFailPop extends CommentPopUtils implements View.OnClickListener {
    private TextView robtaskfailpop_jixu_tv, RejectTextView;
    private Context context;


    public RobTaskFailPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        robtaskfailpop_jixu_tv = view.findViewById(R.id.robtaskfailpop_jixu_tv);
        ImageView closeDialog = view.findViewById(R.id.closeDialog);
        RejectTextView = view.findViewById(R.id.text_rason);
        robtaskfailpop_jixu_tv.setOnClickListener(this);
        closeDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.robtaskfailpop_jixu_tv:
                dismiss();
                break;
            case R.id.closeDialog:
                dismiss();
                break;
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setData(String str) {
        RejectTextView.setText(str);
    }
}
