package com.risenb.witness.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.GuideBean;
import com.risenb.witness.utils.newUtils.BannerUtils;
import com.risenb.witness.utils.newUtils.BaseBannerView;
import com.risenb.witness.utils.newUtils.FixedSpeedScroller;
import com.risenb.witness.utils.newUtils.MUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 引导页
 */
@ContentView(R.layout.guide)
public class GuideUI extends BaseUI {
    @ViewInject(R.id.vp_guide)
    private ViewPager vp_guide;

    @ViewInject(R.id.ll_guide)
    private LinearLayout ll_guide;

    private List<GuideBean> list = new ArrayList<>();

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void onCreate() {
        // 去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void setControlBasis() {
        backGone();
        setTitle("引导页");
        MUtils.getMUtils().machineInformation();
    }

    @Override
    protected void prepareData() {
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        if (!application.getOne()) {
            Intent intent = new Intent(getActivity(), TabUI.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            back();
            return;
        } else {
            Intent intent = new Intent(getActivity(), TabUI.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            back();
            return;
        }
        /*list.add(new GuideBean(R.drawable.guide1));
        list.add(new GuideBean(R.drawable.guide2));
        list.add(new GuideBean(R.drawable.guide3));
        BannerUtils<GuideBean> bannerUtils = new BannerUtils<>();
        bannerUtils.setActivity(getActivity());
        bannerUtils.setViewPager(vp_guide);
        bannerUtils.setDianGroup(ll_guide);
        bannerUtils.setList(list);
        bannerUtils.setLoop(false);
        bannerUtils.setDefaultImg(R.drawable.default_image);
        bannerUtils.setColorTrue(0xffffffff);
        bannerUtils.setColorFalse(0xffffffff);
        bannerUtils.setMarginTrue(Utils.getUtils().getDimen(R.dimen.dm001));
        bannerUtils.setMarginFalse(Utils.getUtils().getDimen(R.dimen.dm003));
        bannerUtils.setDianSize(Utils.getUtils().getDimen(R.dimen.dm020));
        bannerUtils.setBaseBannerView(new BannerView());
        bannerUtils.info();
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(vp_guide.getContext(), new AccelerateInterpolator());
            mField.set(vp_guide, mScroller);
            mScroller.setmDuration(100);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onLoadOver() {
    }

    private class BannerView extends BaseBannerView {
        @Override
        protected View getView(int i) {
            final ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(list.get(i).getDrawable());
            if (i == list.size() - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TabUI.class);
                        intent.putExtra("type", 1);
                        getActivity().startActivity(intent);
                        back();
                    }
                });
            }
            return imageView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
