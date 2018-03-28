package com.risenb.witness.ui.tasklist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ExecFragmentPagerAdapter extends FragmentPagerAdapter {
    private  List<Fragment> list;
    public ExecFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //不作任何操作，Fragment切换时不重复刷新数据
    }
}
