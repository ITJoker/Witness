package com.risenb.witness.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.risenb.witness.R;

public class MoneyRecordPop extends CommentPopUtils implements View.OnClickListener {

    private TextView moneyrecordpop_quanbu, moneyrecordpop_shixiao, moneyrecordpop_tixianzhong, moneyrecordpop_yitixian, moneyrecordpop_shenhezhong, moneyrecordpop_shouru;

    private Context context;


    public MoneyRecordPop(View view, Context context, int layout) {
        super(view, context, layout);
        this.context = context;
    }

    @Override
    public void initLayout(View view, Context context) {

        moneyrecordpop_quanbu = (TextView) view.findViewById(R.id.moneyrecordpop_quanbu);
        moneyrecordpop_shixiao = (TextView) view.findViewById(R.id.moneyrecordpop_shixiao);
        moneyrecordpop_tixianzhong = (TextView) view.findViewById(R.id.moneyrecordpop_tixianzhong);
        moneyrecordpop_yitixian = (TextView) view.findViewById(R.id.moneyrecordpop_yitixian);
        moneyrecordpop_shenhezhong = (TextView) view.findViewById(R.id.moneyrecordpop_shenhezhong);
        moneyrecordpop_shouru = (TextView) view.findViewById(R.id.moneyrecordpop_shouru);

        moneyrecordpop_quanbu.setOnClickListener(this);
        moneyrecordpop_shixiao.setOnClickListener(this);
        moneyrecordpop_tixianzhong.setOnClickListener(this);
        moneyrecordpop_shenhezhong.setOnClickListener(this);
        moneyrecordpop_shouru.setOnClickListener(this);
        moneyrecordpop_yitixian.setOnClickListener(this);

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
