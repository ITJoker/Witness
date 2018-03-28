package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.HistoryCrashBean;
import com.risenb.witness.utils.Date_U;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class MoneyRecordAdapter<T extends HistoryCrashBean.DataBean> extends BaseListAdapter<T> {
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    protected BaseViewHolder<T> loadView(Context context, T bean, int position) {
        return new ViewHolder(context, getViewId(bean, position));
    }

    @Override
    protected int getViewId(T bean, int position) {
        return R.layout.moneyrecord_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {

        @ViewInject(R.id.moneyrecord_item_time_tv)
        private TextView moneyrecord_item_time_tv;
        @ViewInject(R.id.moneyrecord_item_price_tv)
        private TextView moneyrecord_item_price_tv;
        @ViewInject(R.id.moneyrecord_item_type_tv)
        private TextView moneyrecord_item_type_tv;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
            // bitmapInfo(R.drawable.default_image);
        }

        @Override
        protected void prepareData() {
            Date_U date_u = new Date_U();
            moneyrecord_item_time_tv.setText(date_u.times(String.valueOf(bean.getCreateTime())));
            moneyrecord_item_price_tv.setText(bean.getPrice() + "");
            switch (bean.getType()) {
                case "1":
                    moneyrecord_item_type_tv.setText("任务收入");
                    break;
                case "2":
                    moneyrecord_item_type_tv.setText("审核中");
                    break;
                case "3":
                    moneyrecord_item_type_tv.setText("提现中");
                    break;
                case "4":
                    moneyrecord_item_type_tv.setText("提现成功");
                    break;
                case "5":
                    moneyrecord_item_type_tv.setText("失效");
                    break;
            }
        }
    }

}
