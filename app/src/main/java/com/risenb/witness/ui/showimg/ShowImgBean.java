package com.risenb.witness.ui.showimg;

import com.risenb.witness.utils.newUtils.BaseMenuBean;

public class ShowImgBean extends BaseMenuBean {
    private String url;// 分类id,

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getBaseMenuBeanID() {
        return "";
    }

    @Override
    public String getBaseMenuBeanTitle() {
        return "";
    }

}
