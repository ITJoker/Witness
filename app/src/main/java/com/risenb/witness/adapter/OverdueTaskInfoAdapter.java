package com.risenb.witness.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.OverdueTaskInfoBean;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class OverdueTaskInfoAdapter<T extends OverdueTaskInfoBean.DataBean> extends BaseListAdapter<T> {
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
        return R.layout.overdueinfoitem;
    }

    private class ViewHolder extends BaseViewHolder<T> {
        @ViewInject(R.id.layout_overdue_ll)
        LinearLayout layout_overdue_ll;
        @ViewInject(R.id.overduetaskinfo_money)
        private TextView overduetaskinfo_money;
        @ViewInject(R.id.overduetaskinfo_image)
        private ImageView overduetaskinfo_image;
        @ViewInject(R.id.overduetaskinfo_title)
        private TextView overduetaskinfo_title;
        @ViewInject(R.id.overduetaskinfo_note)
        private TextView overduetaskinfo_note;
        @ViewInject(R.id.overduetaskinfo_task_id)
        private TextView overduetaskinfo_task_id;
        @ViewInject(R.id.overdue_image)
        private ImageView overdue_image;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
        }

        @Override
        protected void prepareData() {
            overduetaskinfo_title.setText(bean.getCity().concat(bean.getAddress()));
            overduetaskinfo_note.setText("备注：".concat(bean.getNote()));
            overduetaskinfo_task_id.setText("ID：".concat(bean.getTaskid()));
            /*overduetaskinfo_money.setText(bean.getPrice() + "积分");*/
            ImageLoader.getInstance().displayImage(bean.getImage(), overduetaskinfo_image, MyConfig.options);
            if ("5".equals(bean.getState())) {
                overdue_image.setImageResource(R.drawable.overtime_image);
            } else if ("6".equals(bean.getState())) {
                overdue_image.setImageResource(R.drawable.overgiveup_image);
            }
            overduetaskinfo_note.measure(0, 0);
            int measuredHeight = overduetaskinfo_note.getMeasuredHeight();
            layout_overdue_ll.measure(0, 0);
            ViewGroup.LayoutParams layoutParams = layout_overdue_ll.getLayoutParams();
            layoutParams.height = layout_overdue_ll.getMeasuredHeight() + measuredHeight * 1;
            layout_overdue_ll.setLayoutParams(layoutParams);
        }
    }
}
