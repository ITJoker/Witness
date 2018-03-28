package com.risenb.witness.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.risenb.witness.R;

public class VipWalletMoneyPop extends CommentPopUtils implements View.OnClickListener {
    private TextView moneypop_qx_tv, moneypop_tijiao_tv, moneypop_qurenzheng_tv;
    private Context context;
    public EditText vipwalletmoneypop_name_et, vipwalletmoneypop_username_et, vipwalletmoneypop_price_et;
    public TextView trueNameCertification;

    public VipWalletMoneyPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    public void setType(int type, String alipayusername, String alipaytruename, String price) {
        if (type == 1) {
            trueNameCertification.setText("已实名认证");
            moneypop_qurenzheng_tv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(price)) {
            vipwalletmoneypop_price_et.setText(price);
            vipwalletmoneypop_price_et.setSelection(vipwalletmoneypop_price_et.getText().length());
        }

        if (!TextUtils.isEmpty(alipayusername) && !TextUtils.isEmpty(alipaytruename)) {
            vipwalletmoneypop_name_et.setText(alipaytruename);
            vipwalletmoneypop_username_et.setText(alipayusername);
            vipwalletmoneypop_name_et.setSelection(vipwalletmoneypop_name_et.getText().length());
            vipwalletmoneypop_username_et.setSelection(vipwalletmoneypop_username_et.getText().length());
        }
    }

    @Override
    public void initLayout(View view, Context context) {
        vipwalletmoneypop_price_et = view.findViewById(R.id.vipwalletmoneypop_price_et);
        vipwalletmoneypop_username_et = view.findViewById(R.id.vipwalletmoneypop_username_et);
        vipwalletmoneypop_name_et = view.findViewById(R.id.vipwalletmoneypop_name_et);
        trueNameCertification = view.findViewById(R.id.trueNameCertification);
        moneypop_qx_tv = view.findViewById(R.id.moneypop_qx_tv);
        moneypop_tijiao_tv = view.findViewById(R.id.moneypop_tijiao_tv);
        moneypop_qurenzheng_tv = view.findViewById(R.id.moneypop_qurenzheng_tv);
        moneypop_qx_tv.setOnClickListener(this);
        moneypop_tijiao_tv.setOnClickListener(this);
        moneypop_qurenzheng_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.moneypop_qx_tv) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

}
