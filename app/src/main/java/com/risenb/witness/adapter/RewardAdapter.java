package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.RewardBean;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class RewardAdapter<T extends RewardBean> extends BaseListAdapter<T> {
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
        return R.layout.reward_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {

        @ViewInject(R.id.reward_name_tv)
        private TextView reward_name_tv;
        @ViewInject(R.id.reward_logo_iv)
        private ImageView reward_logo_iv;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
            // bitmapInfo(R.drawable.default_image);
        }

        @Override
        protected void prepareData() {
//            ViewDataUtils.inject(bean, convertView);
            reward_name_tv.setText(bean.getCardName());
            // bitmapUtils.display(back, "");
//             ImageLoader.getInstance().displayImage(bean.getImageBig(), iv_home_item, MyConfig.options);
        }
    }
}
