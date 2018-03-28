package com.risenb.witness.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.risenb.witness.ui.home.AuthenticationAdministratorFragment;
import com.risenb.witness.ui.home.AuthenticationMediaOwnerFragment;

public class AuthenticationFragmentsPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"认证媒体主端", "认证管理员端"};
    private Fragment[] mFragment;

    public AuthenticationFragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragment = new Fragment[]{new AuthenticationMediaOwnerFragment(), new AuthenticationAdministratorFragment()};
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    /**
     * 设置要显示的Fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    /**
     * 设置ViewPager的显示标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //不作任何操作，Fragment切换时不重复刷新数据
    }
}
