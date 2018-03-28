package com.risenb.witness.beans;

import com.risenb.witness.utils.newNetwork.BaseBannerBean;

public class BannerBean extends BaseBannerBean {
    private String BannerImg ;

    public String getBannerImg() {
        return BannerImg;
    }

    public void setBannerImg(String bannerImg) {
        BannerImg = bannerImg;
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
