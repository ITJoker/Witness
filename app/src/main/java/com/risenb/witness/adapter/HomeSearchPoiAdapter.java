package com.risenb.witness.adapter;

import android.content.Context;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.views.newViews.BaseListAdapter;
import com.risenb.witness.views.newViews.BaseViewHolder;

public class HomeSearchPoiAdapter<T extends PoiInfo> extends BaseListAdapter<T> {
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
        return R.layout.homesearchpoi_item;
    }

    private class ViewHolder extends BaseViewHolder<T> {

        @ViewInject(R.id.homesearchpoi_tv)
        private TextView homesearchpoi_tv;
        //
        // @ViewInject(R.id.back)
        // private ImageView back;

        public ViewHolder(Context context, int layoutID) {
            super(context, layoutID);
            // bitmapInfo(R.drawable.default_image);
        }

        @Override
        protected void prepareData() {
//            ViewDataUtils.inject(bean, convertView);
            homesearchpoi_tv.setText(bean.address + bean.name);
            // bitmapUtils.display(back, "");
//             ImageLoader.getInstance().displayImage(bean.getImageBig(), iv_home_item, MyConfig.options);
        }
    }

}
