package com.risenb.witness.utils.newUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class BaseFragmentAdapter<T extends BaseMenuBean> extends FragmentPagerAdapter {
    private List<T> list;

    public BaseFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int position) {
        return ((BaseMenuBean)this.list.get(position)).getFragment();
    }

    public int getCount() {
        return this.list != null?this.list.size():0;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}