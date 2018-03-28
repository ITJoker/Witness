package com.risenb.witness.utils.newUtils;

import android.support.v4.app.Fragment;

public abstract class BaseMenuBean {
    private Fragment fragment;

    public BaseMenuBean() {
    }

    public abstract String getBaseMenuBeanID();

    public abstract String getBaseMenuBeanTitle();

    public Fragment getFragment() {
        return this.fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
