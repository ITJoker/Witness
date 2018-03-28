package com.risenb.witness.views.newViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.utils.newNetwork.BannerBean;
import com.risenb.witness.utils.newUtils.BaseBannerView;

import java.util.List;

public class BannerView extends BaseBannerView {

    private List<BannerBean> list;
    private Context context;

    public BannerView(Context context, List<BannerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    protected View getView(int i) {
        View photoView = LayoutInflater.from(context).inflate(R.layout.hometaskinfobanner_item, null);
        ImageView iv = (ImageView) photoView.findViewById(R.id.hometaskinfobanner_iv);
        BannerBean bean = list.get(i);
        ImageLoader.getInstance().displayImage(bean.getBannerImg(), iv);
        return photoView;
    }

}
