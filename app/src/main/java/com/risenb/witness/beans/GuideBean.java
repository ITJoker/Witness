package com.risenb.witness.beans;

import com.risenb.witness.utils.newNetwork.BaseBannerBean;

/**
 * 引导页
 */
public class GuideBean extends BaseBannerBean {
    private int drawable;

    public GuideBean(int drawable) {
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public String getBannerBeanID() {
        return "";
    }

    @Override
    public String getBannerBeanImage() {
        return "";
    }

    @Override
    public String getBannerBeanTitle() {
        return "";
    }

}
