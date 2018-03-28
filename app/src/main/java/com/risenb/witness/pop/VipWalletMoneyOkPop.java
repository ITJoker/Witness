package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class VipWalletMoneyOkPop extends CommentPopUtils implements View.OnClickListener {

    private TextView moneypop_pop_qr_tv ;

    private Context context;


    public VipWalletMoneyOkPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {
        moneypop_pop_qr_tv = (TextView) view.findViewById(R.id.moneypop_pop_qr_tv);
        moneypop_pop_qr_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.moneypop_pop_qr_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
    
}
